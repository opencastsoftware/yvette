package com.opencastsoftware.yvette;

import java.io.IOException;
import java.net.URI;
import java.util.function.UnaryOperator;

import org.apache.commons.text.WordUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class GraphicalReportHandler implements ReportHandler {
    private int terminalWidth;
    private int contextLines;
    private int tabWidth;
    private LinkStyle linkStyle;
    private GraphicalTheme theme;
    private String footer;

    public GraphicalReportHandler() {
        this.terminalWidth = AnsiConsole.getTerminalWidth();
        this.contextLines = 1;
        this.tabWidth = 4;
        this.linkStyle = LinkStyle.Link;
        this.theme = GraphicalTheme.emoji();
    }

    void renderHeader(Ansi ansi, Diagnostic diagnostic) {
        UnaryOperator<Ansi> severityStyle = diagnostic.severity() == null ? theme.styles().error()
                : theme.styles().forSeverity(diagnostic.severity());
        if (linkStyle.equals(LinkStyle.Link) && diagnostic.url() != null) {
            // OSC 8 ; params ; URI ST
            ansi.format("\u001b]8;;%s\u001b\\", diagnostic.url());

            if (diagnostic.code() != null) {
                severityStyle.apply(ansi)
                        .a(diagnostic.code() + " ")
                        .reset();
            }

            theme.styles().link()
                    .apply(ansi)
                    .a("(link)")
                    .reset();

            // OSC 8 ; ; ST
            ansi.format("\u001b]8;;\u001b\\");

        } else if (diagnostic.code() != null) {
            severityStyle.apply(ansi)
                    .a(diagnostic.code() + " ")
                    .reset();

            if (linkStyle.equals(LinkStyle.Text) && diagnostic.url() != null) {
                ansi.format("(%s)", diagnostic.url());
            }
        }
    }

    void renderMessage(Ansi stringBuilder, Diagnostic diagnostic) {
        UnaryOperator<Ansi> severityStyle = diagnostic.severity() == null
                ? theme.styles().error()
                : theme.styles().forSeverity(diagnostic.severity());

        String severityIcon = diagnostic.severity() == null
                ? theme.characters().error()
                : theme.characters().forSeverity(diagnostic.severity());

        severityStyle
            .apply(stringBuilder)
            .format("  %s %s", severityIcon, diagnostic.message())
            .reset();
    }

    void renderSnippets(Ansi stringBuilder, Diagnostic diagnostic, SourceCode source) {

    }

    void renderFooter(Ansi stringBuilder, Diagnostic diagnostic) {

    }

    void renderRelated(Ansi stringBuilder, Diagnostic diagnostic, SourceCode source) {

    }

    void renderReport(Ansi stringBuilder, Diagnostic diagnostic) {
        renderHeader(stringBuilder, diagnostic);

        stringBuilder.append(System.lineSeparator());

        renderMessage(stringBuilder, diagnostic);
        renderSnippets(stringBuilder, diagnostic, diagnostic.sourceCode());
        renderFooter(stringBuilder, diagnostic);
        renderRelated(stringBuilder, diagnostic, diagnostic.sourceCode());

        if (this.footer != null) {
            stringBuilder.append(System.lineSeparator());

            int lineWidth = 4 > terminalWidth ? 0 : terminalWidth - 4;

            String twoSpaces = "  ";
            stringBuilder.append(twoSpaces);

            String indentString = System.lineSeparator() + twoSpaces;
            String[] lines = footer.split("\\r?\\n");

            for (String line : lines) {
                String wrapped = WordUtils.wrap(line, lineWidth, indentString, false);
                stringBuilder.append(wrapped);
            }
        }
    }

    @Override
    public void display(Diagnostic diagnostic, Appendable output) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        renderReport(Ansi.ansi(stringBuilder), diagnostic);
        output.append(stringBuilder.toString());
    }

    public static void main(String[] args) throws IOException {
        GraphicalReportHandler handler = new GraphicalReportHandler();

        Diagnostic diagnostic = new Diagnostic() {
            @Override
            public String code() {
                return "E01";
            }

            @Override
            public Severity severity() {
                return Severity.Error;
            }

            @Override
            public String help() {
                return null;
            }

            @Override
            public URI url() {
                return URI.create("https://doc.rust-lang.org/error_codes/E0001.html");
            }

            @Override
            public SourceCode sourceCode() {
                return null;
            }

            @Override
            public Iterable<LabelledRange> labels() {
                return null;
            }

            @Override
            public Iterable<Diagnostic> related() {
                return null;
            }

            @Override
            public String message() {
                return "Oh poopies";
            }
        };

        handler.display(diagnostic, System.out);
    }
}
