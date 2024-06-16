/*
 * SPDX-FileCopyrightText:  © 2023-2024 Opencast Software Europe Ltd <https://opencastsoftware.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers.graphical;

import com.opencastsoftware.prettier4j.RenderOptions;
import com.opencastsoftware.yvette.*;
import com.opencastsoftware.yvette.handlers.ReportHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.Failable;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GraphicalReportHandler implements ReportHandler {
    private final LinkStyle linkStyle;
    private final int terminalWidth;
    private final GraphicalTheme theme;
    private final String footer;
    private final int contextLines;
    private final boolean renderCauseChain;
    private final RenderOptions renderOptions;

    private static final Pattern ansiEscapePattern = Pattern.compile("\\u001B\\[[;\\d]*m");

    public GraphicalReportHandler(LinkStyle linkStyle, int terminalWidth, GraphicalTheme theme, String footer,
            int contextLines, boolean renderCauseChain) {
        this.linkStyle = linkStyle;
        this.terminalWidth = terminalWidth;
        this.theme = theme;
        this.footer = footer;
        this.contextLines = contextLines;
        this.renderCauseChain = renderCauseChain;
        this.renderOptions = RenderOptions.builder()
                .emitAnsiEscapes(!theme.styles().equals(ThemeStyles.none()))
                .build();
    }

    void renderHeader(Ansi ansi, Diagnostic diagnostic) {
        UnaryOperator<Ansi> severityStyle = diagnostic.severity() == null
                ? theme.styles().error()
                : theme.styles().forSeverity(diagnostic.severity());

        if (linkStyle.equals(LinkStyle.HYPERLINK) && diagnostic.url() != null) {
            /*
             * Escape sequences for creating hyperlinks in the terminal.
             *
             * See https://gist.github.com/egmontkob/eb114294efbcd5adb1944c9f3cb5feda for
             * details of the hyperlink sequences.
             *
             * See https://en.wikipedia.org/wiki/ANSI_escape_code#Fe_Escape_sequences for
             * details of the escape sequences.
             *
             * The opening sequence is of the form: OSC 8 ; params ; URI ST
             */
            ansi.format("\u001b]8;;%s\u001b\\", diagnostic.url());

            if (diagnostic.code() != null) {
                theme.withStyle(
                        ansi, severityStyle,
                        builder -> builder.a(diagnostic.code() + " "));
            }

            theme.withStyle(
                    ansi, theme.styles().link(),
                    builder -> builder.a("(link)"));

            /*
             * The closing sequence is of the form OSC 8 ; ; ST
             */
            ansi.format("\u001b]8;;\u001b\\%n");

        } else if (diagnostic.code() != null) {
            theme.withStyle(
                    ansi, severityStyle,
                    builder -> builder.a(diagnostic.code()));

            if (linkStyle.equals(LinkStyle.TEXT) && diagnostic.url() != null) {
                theme.withStyle(
                        ansi, theme.styles().link(),
                        builder -> builder.format(" (%s)", diagnostic.url()));
            }

            ansi.a(System.lineSeparator());
        }
    }

    void renderCauses(Ansi ansi, Diagnostic diagnostic) throws IOException {
        if (diagnostic.message() != null) {
            UnaryOperator<Ansi> severityStyle = diagnostic.severity() == null
                    ? theme.styles().error()
                    : theme.styles().forSeverity(diagnostic.severity());

            String severityIcon = diagnostic.severity() == null
                    ? theme.characters().error()
                    : theme.characters().forSeverity(diagnostic.severity());

            int lineWidth = Arithmetic.unsignedSaturatingSub(terminalWidth, 2);

            String initialIndent = theme.withStyle(
                    Ansi.ansi(), severityStyle,
                    builder -> builder.format("  %s ", severityIcon))
                    .toString();

            String subsequentIndent = theme.withStyle(
                    Ansi.ansi(), severityStyle,
                    builder -> builder.format("%n  %s ", theme.characters().vBar()))
                    .toString();

            TextWrap.fill(
                    ansi, lineWidth,
                    initialIndent, subsequentIndent,
                    diagnostic.message().render(renderOptions));

            ansi.a(System.lineSeparator());

            if (!renderCauseChain) {
                return;
            }

            Throwable cause = diagnostic.getCause();

            while (cause != null) {
                Throwable nextCause = cause.getCause();

                if (cause.getMessage() == null) {
                    cause = nextCause;
                    continue;
                }

                String initialCauseIndent = theme.withStyle(
                        Ansi.ansi(), severityStyle, builder -> {
                            return builder.format(
                                    "  %s%s%s ",
                                    nextCause != null &&
                                            nextCause.getMessage() != null
                                                    ? theme.characters().leftCross()
                                                    : theme.characters().leftBottom(),
                                    theme.characters().hBar(),
                                    theme.characters().rightArrow());
                        })
                        .toString();

                String subsequentCauseIndent = theme.withStyle(
                        Ansi.ansi(), severityStyle, builder -> {
                            return builder.format(
                                    "%n  %s   ",
                                    nextCause != null && nextCause.getMessage() != null
                                            ? theme.characters().vBar()
                                            : " ");
                        })
                        .toString();

                TextWrap.fill(
                        ansi, lineWidth,
                        initialCauseIndent, subsequentCauseIndent,
                        cause.getMessage());

                ansi.a(System.lineSeparator());

                cause = nextCause;
            }
        }
    }

    void renderSnippets(Ansi ansi, Diagnostic diagnostic, SourceCode source) {
        if (source != null) {
            if (diagnostic.labels() != null) {
                List<LabelledRange> labels = diagnostic.labels().stream()
                        .sorted(Comparator.comparing(Range::start))
                        .collect(Collectors.toList());

                List<LabelledRange> contexts = new ArrayList<>();

                labels.forEach(rightLabel -> {
                    if (contexts.isEmpty()) {
                        contexts.add(rightLabel);
                    } else {
                        LabelledRange leftLabel = contexts.get(contexts.size() - 1);
                        Position leftEnd = leftLabel.end();
                        Position rightEnd = rightLabel.end();
                        // Snippets will overlap
                        int leftSnippetEnd = leftLabel.end().line() + contextLines;
                        int rightSnippetStart = Arithmetic.unsignedSaturatingSub(
                                rightLabel.start().line(), contextLines);
                        if (leftSnippetEnd >= rightSnippetStart) {
                            contexts.remove(contexts.size() - 1);
                            contexts.add(new LabelledRange(leftLabel.label(), new Range(
                                    leftLabel.start(),
                                    ObjectUtils.max(leftEnd, rightEnd))));
                        } else {
                            contexts.add(rightLabel);
                        }
                    }
                });

                Failable.stream(contexts).forEach(context -> {
                    renderContext(ansi, source, context, labels);
                });
            }
        }
    }

    void renderContext(Ansi ansi, SourceCode source, LabelledRange context, List<LabelledRange> labels)
            throws IOException {
        RangeContents contents = source.readRange(context, contextLines, contextLines);

        List<UnaryOperator<Ansi>> labelStyles = Stream
                .generate(theme.styles().highlights()::stream)
                .flatMap(Function.identity())
                .limit(labels.size())
                .collect(Collectors.toList());

        List<StyledRange> styledLabels = IntStream
                .range(0, labels.size())
                .mapToObj(i -> new StyledRange(labelStyles.get(i), labels.get(i)))
                .collect(Collectors.toList());

        int maxGutter = 0;
        for (Line line : contents.lines()) {
            int numHighlights = 0;
            for (StyledRange label : styledLabels) {
                if (!label.spansOnly(line) && label.appliesTo(line)) {
                    numHighlights++;
                }
            }
            maxGutter = Math.max(maxGutter, numHighlights);
        }

        int lastLineNumber = 0;
        if (!contents.lines().isEmpty()) {
            Line lastLine = contents.lines().get(contents.lines().size() - 1);
            lastLineNumber = lastLine.lineNumber() + 1;
        }

        int lineNumberWidth = Integer.toString(lastLineNumber).length();

        ansi.format(
                "%s%s%s",
                StringUtils.repeat(' ', lineNumberWidth + 2),
                theme.characters().leftTop(),
                theme.characters().hBar());

        if (contents.name() != null) {
            theme.withStyle(ansi, theme.styles().link(), builder -> {
                return builder.format(
                        "[%s:%s:%s]%n",
                        contents.name(),
                        contents.range().start().line() + 1,
                        contents.range().start().character() + 1);
            });
        } else if (contents.lines().size() <= 1) {
            ansi.format(
                    "%s%n",
                    StringUtils.repeat(theme.characters().hBar(), 3));
        } else {
            ansi.format(
                    "[%s:%s]%n",
                    contents.range().start().line() + 1,
                    contents.range().start().character() + 1);
        }

        for (Line line : contents.lines()) {
            renderLineNumber(ansi, lineNumberWidth, line.lineNumber());
            renderLineGutter(ansi, maxGutter, line, styledLabels);
            renderLineText(ansi, line);

            List<StyledRange> singleLineLabels = new ArrayList<>();
            List<StyledRange> multiLineLabels = new ArrayList<>();

            styledLabels.stream()
                    .filter(label -> label.appliesTo(line))
                    .forEach(label -> {
                        if (label.spansOnly(line)) {
                            singleLineLabels.add(label);
                        } else {
                            multiLineLabels.add(label);
                        }
                    });

            if (!singleLineLabels.isEmpty()) {
                renderNoLineNumber(ansi, lineNumberWidth);
                renderHighlightGutter(ansi, maxGutter, line, styledLabels);
                renderSingleLineHighlights(ansi, line, lineNumberWidth, maxGutter, singleLineLabels, styledLabels);
            }

            for (StyledRange highlight : multiLineLabels) {
                if (highlight.label() != null &&
                        highlight.finishesBefore(line) &&
                        !highlight.beginsAfter(line)) {
                    renderNoLineNumber(ansi, lineNumberWidth);
                    renderHighlightGutter(ansi, maxGutter, line, styledLabels);
                    renderMultiLineEnd(ansi, highlight);
                }
            }
        }

        ansi.format(
                "%s%s%s%n",
                StringUtils.repeat(' ', lineNumberWidth + 2),
                theme.characters().leftBottom(),
                StringUtils.repeat(theme.characters().hBar(), 4));
    }

    void renderLineNumber(Ansi ansi, int width, int lineNumber) {
        String lineNumberString = theme.withStyle(
                Ansi.ansi(), theme.styles().lineNumber(), builder -> {
                    return builder.format(
                            "%" + width + "s",
                            String.valueOf(lineNumber + 1));
                })
                .toString();

        ansi.format(
                " %s %s ",
                lineNumberString,
                theme.characters().vBar());
    }

    void renderNoLineNumber(Ansi ansi, int width) {
        ansi.format(
                " %s %s ",
                StringUtils.repeat(' ', width),
                theme.characters().vBarBreak());
    }

    void renderLineGutter(Ansi ansi, int maxGutter, Line line, List<StyledRange> labels) {
        if (maxGutter > 0) {
            List<StyledRange> applicableLabels = labels.stream()
                    .filter(label -> label.appliesTo(line))
                    .collect(Collectors.toList());

            Ansi lineGutter = Ansi.ansi();
            boolean arrow = false;

            for (int i = 0; i < applicableLabels.size(); i++) {
                StyledRange highlight = applicableLabels.get(i);

                if (highlight.beginsAfter(line)) {
                    String hBar = StringUtils.repeat(
                            theme.characters().hBar(),
                            Arithmetic.unsignedSaturatingSub(maxGutter, i));

                    theme.withStyle(lineGutter, highlight.style(), builder -> {
                        return builder
                                .a(theme.characters().leftTop())
                                .a(hBar)
                                .a(theme.characters().rightArrow());
                    });

                    arrow = true;
                    break;

                } else if (highlight.finishesBefore(line)) {
                    String hBar = StringUtils.repeat(
                            theme.characters().hBar(),
                            Arithmetic.unsignedSaturatingSub(maxGutter, i));

                    theme.withStyle(lineGutter, highlight.style(), builder -> {
                        if (highlight.label() != null) {
                            builder.a(theme.characters().leftCross());
                        } else {
                            builder.a(theme.characters().leftBottom());
                        }

                        return builder
                                .a(hBar)
                                .a(theme.characters().rightArrow());
                    });

                    arrow = true;
                    break;

                } else if (highlight.contains(line)) {
                    theme.withStyle(
                            lineGutter, highlight.style(),
                            builder -> builder.a(theme.characters().vBar()));
                } else {
                    lineGutter.a(' ');
                }
            }

            String gutterString = lineGutter.toString();
            String strippedGutterString = ansiEscapePattern
                    .matcher(gutterString)
                    .replaceAll("");

            int gutterPaddingSize = (arrow ? 1 : 3)
                    + Arithmetic.unsignedSaturatingSub(maxGutter, strippedGutterString.length());

            String gutterPadding = StringUtils.repeat(' ', gutterPaddingSize);

            ansi.format("%s%s", gutterString, gutterPadding);
        }
    }

    void renderHighlightGutter(Ansi ansi, int maxGutter, Line line, List<StyledRange> labels) {
        if (maxGutter > 0) {
            List<StyledRange> applicableLabels = labels.stream()
                    .filter(label -> label.appliesTo(line))
                    .collect(Collectors.toList());

            Ansi lineGutter = Ansi.ansi();

            for (int i = 0; i < applicableLabels.size(); i++) {
                StyledRange highlight = applicableLabels.get(i);

                if (!highlight.spansOnly(line) && highlight.finishesBefore(line)) {
                    String hBar = StringUtils.repeat(
                            theme.characters().hBar(),
                            Arithmetic.unsignedSaturatingSub(maxGutter, i) + 2);

                    theme.withStyle(lineGutter, highlight.style(), builder -> {
                        return builder
                                .a(theme.characters().leftBottom())
                                .a(hBar);
                    });

                    break;
                } else {
                    theme.withStyle(
                            lineGutter, highlight.style(),
                            builder -> builder.a(theme.characters().vBar()));
                }
            }

            String gutterString = lineGutter.toString();
            String strippedGutterString = ansiEscapePattern
                    .matcher(gutterString)
                    .replaceAll("");

            int gutterPaddingSize = Arithmetic.unsignedSaturatingSub(maxGutter + 3, strippedGutterString.length());

            String gutterPadding = StringUtils.repeat(' ', gutterPaddingSize);

            ansi.format("%s%s", gutterString, gutterPadding);
        }
    }

    void renderLineText(Ansi ansi, Line line) {
        ansi.a(line.text()).a(System.lineSeparator());
    }

    void renderSingleLineHighlights(Ansi ansi, Line line, int lineNumberWidth, int maxGutter,
            List<StyledRange> singleLiners, List<StyledRange> allLabels) {
        Ansi underlines = Ansi.ansi();
        int highest = 0;

        List<SimpleEntry<StyledRange, Integer>> vBarOffsets = new ArrayList<>();

        for (StyledRange highlight : singleLiners) {
            int charStart = highlight.start().character();
            int charEnd = highlight.end().character();
            int start = Math.max(charStart, highest);
            int end = Math.max(charEnd, start + 1);

            int vBarOffset = (start + end) / 2;
            int numLeft = vBarOffset - start;
            int numRight = end - vBarOffset - 1;

            if (start < end) {
                int marginSize = Arithmetic.unsignedSaturatingSub(start, highest);

                theme.withStyle(underlines, highlight.style(), builder -> {
                    String underLineChar = theme.characters().underLine();

                    if ((charEnd - charStart) == 0) {
                        underLineChar = theme.characters().upArrow();
                    } else if (highlight.label() != null) {
                        underLineChar = theme.characters().underBar();
                    }

                    return builder.format(
                            "%s%s%s%s",
                            StringUtils.repeat(' ', marginSize),
                            StringUtils.repeat(theme.characters().underLine(), numLeft),
                            underLineChar,
                            StringUtils.repeat(theme.characters().underLine(), numRight));
                });
            }

            highest = Math.max(highest, end);

            vBarOffsets.add(new SimpleEntry<>(highlight, vBarOffset));
        }

        ansi.a(underlines).a(System.lineSeparator());

        ListIterator<StyledRange> reverseIterator = singleLiners.listIterator(singleLiners.size());
        while (reverseIterator.hasPrevious()) {
            StyledRange highlight = reverseIterator.previous();

            if (highlight.label() != null) {
                renderNoLineNumber(ansi, lineNumberWidth);
                renderHighlightGutter(ansi, maxGutter, line, allLabels);

                int currentOffset = 1;
                for (SimpleEntry<StyledRange, Integer> entry : vBarOffsets) {
                    StyledRange offsetHighlight = entry.getKey();
                    int vBarOffset = entry.getValue();

                    while (currentOffset < (vBarOffset + 1)) {
                        ansi.a(' ');
                        currentOffset++;
                    }

                    if (!offsetHighlight.equals(highlight)) {
                        theme.withStyle(
                                ansi, offsetHighlight.style(),
                                builder -> builder.a(theme.characters().vBar()));
                        currentOffset++;
                    } else {
                        theme.withStyle(ansi, highlight.style(), builder -> {
                            return builder.format(
                                    "%s%s %s%n",
                                    theme.characters().leftBottom(),
                                    StringUtils.repeat(theme.characters().hBar(), 2),
                                    highlight.label());
                        });

                        break;
                    }
                }
            }
        }
    }

    void renderMultiLineEnd(Ansi ansi, StyledRange highlight) {
        theme.withStyle(ansi, highlight.style(), builder -> {
            return builder.format(
                    "%s %s%n",
                    theme.characters().hBar(),
                    Optional.ofNullable(highlight.label()).orElse(""));
        });
    }

    void renderHelp(Ansi ansi, Diagnostic diagnostic) throws IOException {
        if (diagnostic.help() != null) {
            int lineWidth = Arithmetic.unsignedSaturatingSub(terminalWidth, 4);

            String initialIndent = theme.withStyle(
                    Ansi.ansi(), theme.styles().help(),
                    builder -> builder.format("  help: ")).toString();

            String subsequentIndent = String.format("%n        ");

            TextWrap.fill(
                    ansi, lineWidth,
                    initialIndent, subsequentIndent,
                    diagnostic.help().render(renderOptions));

            ansi.a(System.lineSeparator());
        }
    }

    void renderRelated(Ansi ansi, Diagnostic diagnostic, SourceCode source) {
        // TODO: Figure out how to align this with LSP
    }

    void renderFooter(Ansi ansi) throws IOException {
        if (this.footer != null) {
            ansi.a(System.lineSeparator());

            int lineWidth = Arithmetic.unsignedSaturatingSub(terminalWidth, 4);
            String initialIndent = "  ";
            String subsequentIndent = System.lineSeparator() + initialIndent;

            TextWrap.fill(
                    ansi, lineWidth,
                    initialIndent, subsequentIndent,
                    this.footer);

            ansi.a(System.lineSeparator());
        }
    }

    void renderReport(Ansi ansi, Diagnostic diagnostic) throws IOException {
        renderHeader(ansi, diagnostic);
        ansi.a(System.lineSeparator());
        renderCauses(ansi, diagnostic);
        renderSnippets(ansi, diagnostic, diagnostic.sourceCode());
        renderHelp(ansi, diagnostic);
        renderRelated(ansi, diagnostic, diagnostic.sourceCode());
        renderFooter(ansi);
    }

    @Override
    public void display(Diagnostic diagnostic, Appendable output) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        renderReport(Ansi.ansi(stringBuilder), diagnostic);
        output.append(stringBuilder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphicalReportHandler that = (GraphicalReportHandler) o;
        return terminalWidth == that.terminalWidth && contextLines == that.contextLines && renderCauseChain == that.renderCauseChain && linkStyle == that.linkStyle && Objects.equals(theme, that.theme) && Objects.equals(footer, that.footer) && Objects.equals(renderOptions, that.renderOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkStyle, terminalWidth, theme, footer, contextLines, renderCauseChain, renderOptions);
    }

    @Override
    public String toString() {
        return "GraphicalReportHandler[" +
                "linkStyle=" + linkStyle +
                ", terminalWidth=" + terminalWidth +
                ", theme=" + theme +
                ", footer='" + footer + '\'' +
                ", contextLines=" + contextLines +
                ", renderCauseChain=" + renderCauseChain +
                ", renderOptions=" + renderOptions +
                ']';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Supplier<GraphicalReportHandler> {
        private Optional<Boolean> enableTerminalLinks = Optional.empty();
        private Optional<Integer> terminalWidth = Optional.empty();
        private Optional<GraphicalTheme> theme = Optional.empty();
        private Optional<Boolean> enableColours = Optional.empty();
        private RgbColours rgbColours = RgbColours.NEVER;
        private Optional<Boolean> enableUnicode = Optional.empty();
        private String footer = null;
        private int contextLines = 1;
        private boolean renderCauseChain = true;

        public Builder withTerminalLinks(boolean enabled) {
            this.enableTerminalLinks = Optional.of(enabled);
            return this;
        }

        public Builder withTerminalWidth(int width) {
            this.terminalWidth = Optional.of(width);
            return this;
        }

        public Builder withGraphicalTheme(GraphicalTheme theme) {
            this.theme = Optional.of(theme);
            return this;
        }

        public Builder withCauseChain(boolean enabled) {
            this.renderCauseChain = enabled;
            return this;
        }

        public Builder withColours(boolean enabled) {
            this.enableColours = Optional.of(enabled);
            return this;
        }

        public Builder withRgbColours(RgbColours rgbColours) {
            this.rgbColours = rgbColours;
            return this;
        }

        public Builder withUnicode(boolean enabled) {
            this.enableUnicode = Optional.of(enabled);
            return this;
        }

        public Builder withFooter(String footer) {
            this.footer = footer;
            return this;
        }

        public Builder withContextLines(int contextLines) {
            this.contextLines = contextLines;
            return this;
        }

        public GraphicalReportHandler buildFor(Appendable output) {
            int width = terminalWidth.orElseGet(() -> TerminalSupport.terminalWidth(output));

            LinkStyle linkStyle;
            if (enableTerminalLinks.isPresent()) {
                if (enableTerminalLinks.get()) {
                    linkStyle = LinkStyle.HYPERLINK;
                } else {
                    linkStyle = LinkStyle.TEXT;
                }
            } else if (TerminalSupport.supportsHyperlinks(output)) {
                linkStyle = LinkStyle.HYPERLINK;
            } else {
                linkStyle = LinkStyle.TEXT;
            }

            GraphicalTheme graphicalTheme;
            if (theme.isPresent()) {
                graphicalTheme = theme.get();
            } else {
                ThemeCharacters themeCharacters;
                if (enableUnicode.isPresent()) {
                    if (enableUnicode.get()) {
                        themeCharacters = ThemeCharacters.unicode();
                    } else {
                        themeCharacters = ThemeCharacters.ascii();
                    }
                } else if (TerminalSupport.supportsUnicode(output)) {
                    themeCharacters = ThemeCharacters.unicode();
                } else {
                    themeCharacters = ThemeCharacters.ascii();
                }

                ThemeStyles themeStyles;
                if (enableColours.isPresent() && !enableColours.get()) {
                    themeStyles = ThemeStyles.none();
                } else {
                    ColourSupport colourSupport = TerminalSupport.colourSupport(output);

                    if (!ColourSupport.NONE.equals(colourSupport)) {
                        if (RgbColours.ALWAYS.equals(rgbColours)) {
                            themeStyles = ThemeStyles.rgb();
                        } else if (RgbColours.PREFERRED.equals(rgbColours)
                                && colourSupport.has16MColourSupport()) {
                            themeStyles = ThemeStyles.rgb();
                        } else {
                            themeStyles = ThemeStyles.ansi();
                        }
                    } else if (enableColours.isPresent() && enableColours.get()) {
                        if (RgbColours.ALWAYS.equals(rgbColours)) {
                            themeStyles = ThemeStyles.rgb();
                        } else {
                            themeStyles = ThemeStyles.ansi();
                        }
                    } else {
                        themeStyles = ThemeStyles.none();
                    }
                }

                graphicalTheme = new GraphicalTheme(themeCharacters, themeStyles);
            }

            return new GraphicalReportHandler(
                linkStyle,
                width,
                graphicalTheme,
                footer,
                contextLines,
                renderCauseChain);
        }

        @Override
        public GraphicalReportHandler get() {
            return buildFor(System.err);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((enableTerminalLinks == null) ? 0 : enableTerminalLinks.hashCode());
            result = prime * result + ((terminalWidth == null) ? 0 : terminalWidth.hashCode());
            result = prime * result + ((theme == null) ? 0 : theme.hashCode());
            result = prime * result + ((enableColours == null) ? 0 : enableColours.hashCode());
            result = prime * result + ((rgbColours == null) ? 0 : rgbColours.hashCode());
            result = prime * result + ((enableUnicode == null) ? 0 : enableUnicode.hashCode());
            result = prime * result + ((footer == null) ? 0 : footer.hashCode());
            result = prime * result + contextLines;
            result = prime * result + (renderCauseChain ? 1231 : 1237);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Builder other = (Builder) obj;
            if (enableTerminalLinks == null) {
                if (other.enableTerminalLinks != null)
                    return false;
            } else if (!enableTerminalLinks.equals(other.enableTerminalLinks))
                return false;
            if (terminalWidth == null) {
                if (other.terminalWidth != null)
                    return false;
            } else if (!terminalWidth.equals(other.terminalWidth))
                return false;
            if (theme == null) {
                if (other.theme != null)
                    return false;
            } else if (!theme.equals(other.theme))
                return false;
            if (enableColours == null) {
                if (other.enableColours != null)
                    return false;
            } else if (!enableColours.equals(other.enableColours))
                return false;
            if (rgbColours != other.rgbColours)
                return false;
            if (enableUnicode == null) {
                if (other.enableUnicode != null)
                    return false;
            } else if (!enableUnicode.equals(other.enableUnicode))
                return false;
            if (footer == null) {
                if (other.footer != null)
                    return false;
            } else if (!footer.equals(other.footer))
                return false;
            if (contextLines != other.contextLines)
                return false;
            if (renderCauseChain != other.renderCauseChain)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Builder [enableTerminalLinks=" + enableTerminalLinks + ", terminalWidth=" + terminalWidth
                    + ", theme=" + theme + ", enableColours=" + enableColours + ", rgbColours=" + rgbColours
                    + ", enableUnicode=" + enableUnicode + ", footer=" + footer + ", contextLines=" + contextLines
                    + ", renderCauseChain=" + renderCauseChain + "]";
        }
    }

    // Package private; for builder unit tests
    LinkStyle linkStyle() {
        return linkStyle;
    }

    int terminalWidth() {
        return terminalWidth;
    }

    GraphicalTheme theme() {
        return theme;
    }

    String footer() {
        return footer;
    }

    int contextLines() {
        return contextLines;
    }

    boolean renderCauseChain() {
        return renderCauseChain;
    }
}
