package com.opencastsoftware.yvette.handlers.graphical;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.opencastsoftware.yvette.*;
import com.opencastsoftware.yvette.arbitrary.DiagnosticSupplier;
import com.opencastsoftware.yvette.arbitrary.GraphicalThemeSupplier;
import com.opencastsoftware.yvette.arbitrary.LinkStyleSupplier;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import nl.jqno.equalsverifier.EqualsVerifier;

public class GraphicalReportHandlerTest {
    private final GraphicalReportHandler handler = GraphicalReportHandler.builder()
            .withTerminalLinks(true)
            .withTerminalWidth(80)
            .withColours(false)
            .withUnicode(true)
            .withContextLines(1)
            .withCauseChain(true)
            .buildFor(System.err);

    @Test
    void testCauseChain() throws IOException {
        Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
        exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));

        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "Couldn't attach the source code to your diagnostic",
                exc,
                "try doing it better next time?",
                null,
                null,
                Collections.emptyList());

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × Couldn't attach the source code to your diagnostic",
                        "  ├─▶ Couldn't find the file BadFile.java",
                        "  ╰─▶ Access denied to file BadFile.java",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testFooter() throws IOException {
        StringBuilder builder = new StringBuilder();

        GraphicalReportHandler handler = GraphicalReportHandler.builder()
                .withFooter("This is a footer")
                .buildFor(builder);

        Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
        exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));

        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "Couldn't attach the source code to your diagnostic",
                exc,
                "try doing it better next time?",
                null,
                null,
                Collections.emptyList());

        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × Couldn't attach the source code to your diagnostic",
                        "  ├─▶ Couldn't find the file BadFile.java",
                        "  ╰─▶ Access denied to file BadFile.java",
                        "  help: try doing it better next time?",
                        "",
                        "  This is a footer",
                        ""),
                builder.toString());
    }

    @Test
    void testNullMessage() throws IOException {
        Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
        exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));

        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                null,
                exc,
                "try doing it better next time?",
                null,
                null,
                Collections.emptyList());

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testCauseChainIgnoresTrailingNullMessage() throws IOException {
        Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
        exc.initCause(new AccessDeniedException(null));

        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "Couldn't attach the source code to your diagnostic",
                exc,
                "try doing it better next time?",
                null,
                null,
                Collections.emptyList());

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × Couldn't attach the source code to your diagnostic",
                        "  ╰─▶ Couldn't find the file BadFile.java",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testCauseChainIgnoresMiddleNullMessage() throws IOException {
        Throwable exc = new FileNotFoundException(null);
        exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));

        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "Couldn't attach the source code to your diagnostic",
                exc,
                "try doing it better next time?",
                null,
                null,
                Collections.emptyList());

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × Couldn't attach the source code to your diagnostic",
                        "  ╰─▶ Access denied to file BadFile.java",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testEmptySource() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", ""),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(0, 0),
                                new Position(0, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testEmptySourceNullName() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode(null, ""),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(0, 0),
                                new Position(0, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭────",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineSourceNullName() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode(null, "source"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(0, 0),
                                new Position(0, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭────",
                        " 1 │ source",
                        "   · ▲",
                        "   · ╰── this bit here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightFullLineRange() throws IOException {
        Diagnostic err = new TestDiagnostic(
                null,
                Severity.Error,
                "oops!",
                null,
                null,
                new StringSourceCode("Issue.java", "source\ntext"),
                Collections.singletonList(
                        new LabelledRange(
                                "This bit here",
                                new Position(1, 0),
                                new Position(1, 4))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "",
                        "  × oops!",
                        "   ╭─[Issue.java:1:1]",
                        " 1 │ source",
                        " 2 │ text",
                        "   · ──┬─",
                        "   ·   ╰── This bit here",
                        "   ╰────",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightsNonOverlapping() throws IOException {
        Diagnostic err = new TestDiagnostic(
                null,
                Severity.Error,
                "oops!",
                null,
                null,
                new StringSourceCode("Issue.java", "source\ntext\nhere\nand\nsome\nmore"),
                Arrays.asList(
                        new LabelledRange(
                                "This bit here",
                                new Position(1, 0),
                                new Position(1, 4)),
                        new LabelledRange(
                                "And this bit",
                                new Position(4, 0),
                                new Position(4, 4))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "",
                        "  × oops!",
                        "   ╭─[Issue.java:1:1]",
                        " 1 │ source",
                        " 2 │ text",
                        "   · ──┬─",
                        "   ·   ╰── This bit here",
                        " 3 │ here",
                        "   ╰────",
                        "   ╭─[Issue.java:4:1]",
                        " 4 │ and",
                        " 5 │ some",
                        "   · ──┬─",
                        "   ·   ╰── And this bit",
                        " 6 │ more",
                        "   ╰────",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightNullSourceName() throws IOException {
        Diagnostic err = new TestDiagnostic(
                null,
                Severity.Error,
                "oops!",
                null,
                null,
                new StringSourceCode(null, "source\ntext"),
                Collections.singletonList(
                        new LabelledRange(
                                "This bit here",
                                new Position(1, 0),
                                new Position(1, 4))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "",
                        "  × oops!",
                        "   ╭─[1:1]",
                        " 1 │ source",
                        " 2 │ text",
                        "   · ──┬─",
                        "   ·   ╰── This bit here",
                        "   ╰────",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineWideChar() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode(
                        "BadFile.java", "source\n  👼🏼text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 4),
                                new Position(1, 10))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   👼🏼text",
                        "   ·     ───┬──",
                        "   ·        ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Disabled("There is no special handling of tab characters yet")
    @Test
    void testSingleLineTwoTabs() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode(
                        "BadFile.java", "source\n\t\ttext\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 2),
                                new Position(1, 6))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │         text",
                        "   ·         ──┬─",
                        "   ·           ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Disabled("There is no special handling of tab characters yet")
    @Test
    void testSingleLineWithTabInMiddle() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode(
                        "BadFile.java", "source\ntext =\ttext\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 7),
                                new Position(1, 11))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │ text =    text",
                        "   ·           ──┬─",
                        "   ·             ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlight() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 2),
                                new Position(1, 6))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   text",
                        "   ·   ──┬─",
                        "   ·     ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightOffsetZero() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(0, 0),
                                new Position(0, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        "   · ▲",
                        "   · ╰── this bit here",
                        " 2 │   text",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightOffsetEndOfLine() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(0, 6),
                                new Position(0, 6))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        "   ·       ▲",
                        "   ·       ╰── this bit here",
                        " 2 │   text",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightIncludeEndOfLine() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 2),
                                new Position(1, 7))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   text",
                        "   ·   ──┬──",
                        "   ·     ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightIncludeEndOfLineCrLf() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\r\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 2),
                                new Position(1, 7))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   text",
                        "   ·   ──┬──",
                        "   ·     ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightEmptyRange() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 2),
                                new Position(1, 2))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   text",
                        "   ·   ▲",
                        "   ·   ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightNoLabel() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                null,
                                new Position(1, 2),
                                new Position(1, 6))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   text",
                        "   ·   ────",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testSingleLineHighlightAtLineStart() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\ntext\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "this bit here",
                                new Position(1, 0),
                                new Position(1, 4))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │ text",
                        "   · ──┬─",
                        "   ·   ╰── this bit here",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testMultipleSameLineHighlights() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text text text text text\n    here"),
                Arrays.asList(
                        new LabelledRange(
                                "x",
                                new Position(1, 2),
                                new Position(1, 6)),
                        new LabelledRange(
                                "y",
                                new Position(1, 7),
                                new Position(1, 11)),
                        new LabelledRange(
                                "z",
                                new Position(1, 17),
                                new Position(1, 21))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   text text text text text",
                        "   ·   ──┬─ ──┬─      ──┬─",
                        "   ·     │    │         ╰── z",
                        "   ·     │    ╰── y",
                        "   ·     ╰── x",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Disabled("There is no special handling of tab characters yet")
    @Test
    void testMultipleSameLineHighlightsWithTabsInMiddle() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text text text\ttext text\n    here"),
                Arrays.asList(
                        new LabelledRange(
                                "x",
                                new Position(1, 2),
                                new Position(1, 6)),
                        new LabelledRange(
                                "y",
                                new Position(1, 7),
                                new Position(1, 11)),
                        new LabelledRange(
                                "z",
                                new Position(1, 17),
                                new Position(1, 21))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ source",
                        " 2 │   text text text    text text",
                        "   ·   ──┬─ ──┬─         ──┬─",
                        "   ·     │    │            ╰── z",
                        "   ·     │    ╰── y",
                        "   ·     ╰── x",
                        " 3 │     here",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testMultilineHighlightAdjacent() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here"),
                Collections.singletonList(
                        new LabelledRange(
                                "these two lines",
                                new Position(1, 0),
                                new Position(2, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │     source",
                        " 2 │ ╭─▶   text",
                        " 3 │ ├─▶     here",
                        "   · ╰──── these two lines",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testMultilineHighlightFlyby() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "line1\nline2\nline3\nline4\nline5\n"),
                Arrays.asList(
                        new LabelledRange(
                                "block 1",
                                new Position(0, 0),
                                new Position(4, 0)),
                        new LabelledRange(
                                "block 2",
                                new Position(1, 0),
                                new Position(3, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ ╭──▶ line1",
                        " 2 │ │╭─▶ line2",
                        " 3 │ ││   line3",
                        " 4 │ │├─▶ line4",
                        "   · │╰──── block 2",
                        " 5 │ ├──▶ line5",
                        "   · ╰───── block 1",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testMultilineHighlightNoLabel() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "line1\nline2\nline3\nline4\nline5\n"),
                Arrays.asList(
                        new LabelledRange(
                                "block 1",
                                new Position(0, 0),
                                new Position(4, 0)),
                        new LabelledRange(
                                null,
                                new Position(1, 0),
                                new Position(3, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ ╭──▶ line1",
                        " 2 │ │╭─▶ line2",
                        " 3 │ ││   line3",
                        " 4 │ │╰─▶ line4",
                        " 5 │ ├──▶ line5",
                        "   · ╰───── block 1",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testMultilineHighlightsAdjacent() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                null,
                new StringSourceCode("BadFile.java", "source\n  text\n    here\nmore here"),
                Arrays.asList(
                        new LabelledRange(
                                "this bit here",
                                new Position(0, 0),
                                new Position(1, 0)),
                        new LabelledRange(
                                "also this bit",
                                new Position(2, 0),
                                new Position(3, 0))));

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);

        assertEquals(
                String.join(
                        System.lineSeparator(),
                        "oops-my-bad",
                        "",
                        "  × oops!",
                        "   ╭─[BadFile.java:1:1]",
                        " 1 │ ╭─▶ source",
                        " 2 │ ├─▶   text",
                        "   · ╰──── this bit here",
                        " 3 │ ╭─▶     here",
                        " 4 │ ├─▶ more here",
                        "   · ╰──── also this bit",
                        "   ╰────",
                        "  help: try doing it better next time?",
                        ""),
                builder.toString());
    }

    @Test
    void testUrlLinks() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                URI.create("https://example.com"),
                null,
                Collections.emptyList());

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);
        String result = builder.toString();

        assertThat(result, containsString("https://example.com"));
        assertThat(result, containsString("(link)"));
        assertThat(result, containsString("oops-my-bad"));
    }

    @Test
    void testUrlLinksNoCode() throws IOException {
        Diagnostic err = new TestDiagnostic(
                null,
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                URI.create("https://example.com"),
                null,
                Collections.emptyList());

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);
        String result = builder.toString();

        assertThat(result, containsString("https://example.com"));
        assertThat(result, containsString("(link)"));
    }

    @Test
    void testDisableUrlLinks() throws IOException {
        Diagnostic err = new TestDiagnostic(
                "oops-my-bad",
                Severity.Error,
                "oops!",
                "try doing it better next time?",
                URI.create("https://example.com"),
                null,
                Collections.emptyList());

        GraphicalReportHandler handler = new GraphicalReportHandler(
                LinkStyle.TEXT,
                80,
                GraphicalTheme.unicodeNoColour(),
                null,
                1,
                true);

        StringBuilder builder = new StringBuilder();
        handler.display(err, builder);
        String result = builder.toString();

        assertThat(result, containsString("https://example.com"));
        assertThat(result, not(containsString("(link)")));
        assertThat(result, containsString("oops-my-bad"));
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(GraphicalReportHandler.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier
                .forClass(GraphicalReportHandler.class)
                .withIgnoredFields("ansiEscapePattern")
                .verify();
    }

    @Property
    void rendersArbitraryDiagnostics(
            @ForAll(supplier = DiagnosticSupplier.class) Diagnostic diagnostic,
            @ForAll(supplier = LinkStyleSupplier.class) LinkStyle links,
            @ForAll @IntRange(min = 0, max = 200) int terminalWidth,
            @ForAll(supplier = GraphicalThemeSupplier.class) GraphicalTheme theme,
            @ForAll String footer,
            @ForAll @IntRange(min = 0, max = 5) int contextLines,
            @ForAll boolean renderCauseChain) throws IOException {
        GraphicalReportHandler handler = new GraphicalReportHandler(
                links,
                terminalWidth,
                GraphicalTheme.unicodeNoColour(),
                footer,
                contextLines,
                renderCauseChain);

        StringBuilder stringBuilder = new StringBuilder();

        handler.display(diagnostic, stringBuilder);

        assertThat(stringBuilder.toString(), is(not(blankOrNullString())));
    }
}
