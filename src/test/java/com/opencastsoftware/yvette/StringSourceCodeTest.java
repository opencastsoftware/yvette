/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StringSourceCodeTest {
    private static final String TEST_SOURCE = String.join(
        System.lineSeparator(),
        "package com.opencastsoftware.yvette;",
        "",
        "public class TestSourceCode {",
        "    public static void main(String[] args) {",
        "        System.out.println(\"Hello!\");",
        "    }",
        "}"
    );

    @Test
    void testNameString() {
        StringSourceCode source = new StringSourceCode("TestSourceCode.java", TEST_SOURCE);
        assertThat(source.name(), is(equalTo("TestSourceCode.java")));
        StringRangeContents contents = source.readRange(new Range(0, 0, 6, 0), 1, 1);
        assertThat(contents.name(), is(equalTo("TestSourceCode.java")));
    }

    @Test
    void testReadEntireFile() {
        StringSourceCode source = new StringSourceCode("TestSourceCode.java", TEST_SOURCE);
        StringRangeContents contents = source.readRange(new Range(0, 0, 6, 0), 1, 1);
        assertThat(contents.range(), is(equalTo(new Range(0, 0, 6, 0))));
        assertThat(contents.lines(), contains(
            new Line(0, "package com.opencastsoftware.yvette;"),
            new Line(1, ""),
            new Line(2, "public class TestSourceCode {"),
            new Line(3, "    public static void main(String[] args) {"),
            new Line(4, "        System.out.println(\"Hello!\");"),
            new Line(5, "    }"),
            new Line(6, "}")
        ));
    }

    @Test
    void testReadFirstLine() {
        StringSourceCode source = new StringSourceCode("TestSourceCode.java", TEST_SOURCE);
        StringRangeContents contents = source.readRange(new Range(0, 0, 0, 0), 1, 1);
        // First line, plus one line of context
        assertThat(contents.range(), is(equalTo(new Range(0, 0, 1, 0))));
        assertThat(contents.lines(), contains(
            new Line(0, "package com.opencastsoftware.yvette;"),
            new Line(1, "")
        ));
    }

    @Test
    void testReadMiddleLine() {
        StringSourceCode source = new StringSourceCode("TestSourceCode.java", TEST_SOURCE);
        StringRangeContents contents = source.readRange(new Range(3, 0, 3, 0), 1, 1);
        // Middle line, plus adjacent lines as context
        assertThat(contents.range(), is(equalTo(new Range(2, 0, 4, 36))));
        assertThat(contents.lines(), contains(
            new Line(2, "public class TestSourceCode {"),
            new Line(3, "    public static void main(String[] args) {"),
            new Line(4, "        System.out.println(\"Hello!\");")
        ));
    }

    @Test
    void testReadLastLine() {
        StringSourceCode source = new StringSourceCode("TestSourceCode.java", TEST_SOURCE);
        StringRangeContents contents = source.readRange(new Range(6, 0, 6, 0), 1, 1);
        // Last line, plus one line of context
        assertThat(contents.range(), is(equalTo(new Range(5, 0, 6, 0))));
        assertThat(contents.lines(), contains(
            new Line(5, "    }"),
            new Line(6, "}")
        ));
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(StringSourceCode.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(StringSourceCode.class).verify();
    }
}
