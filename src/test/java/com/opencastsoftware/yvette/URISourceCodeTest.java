/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class URISourceCodeTest {
    private static URI testSourceFile;

    static {
        try {
            testSourceFile = Objects.requireNonNull(URISourceCodeTest.class.getResource("TestSourceCode.java")).toURI();
        } catch (NullPointerException | URISyntaxException e) {
            testSourceFile = null;
        }
    }

    @Test
    void testNameString() throws IOException {
        URISourceCode source = new URISourceCode(testSourceFile);
        URIRangeContents contents = source.readRange(new Range(0, 0, 6, 0), 1, 1);
        assertThat(contents.name(), endsWith("TestSourceCode.java"));
        assertThat(contents.uri().toString(), endsWith("TestSourceCode.java"));
    }

    @Test
    void testReadEntireFile() throws IOException {
        URISourceCode source = new URISourceCode(testSourceFile);
        URIRangeContents contents = source.readRange(new Range(0, 0, 6, 0), 1, 1);
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
    void testReadFirstLine() throws IOException {
        URISourceCode source = new URISourceCode(testSourceFile);
        URIRangeContents contents = source.readRange(new Range(0, 0, 0, 0), 1, 1);
        // First line, plus one line of context
        assertThat(contents.range(), is(equalTo(new Range(0, 0, 1, 0))));
        assertThat(contents.lines(), contains(
            new Line(0, "package com.opencastsoftware.yvette;"),
            new Line(1, "")
        ));
    }

    @Test
    void testReadMiddleLine() throws IOException {
        URISourceCode source = new URISourceCode(testSourceFile);
        URIRangeContents contents = source.readRange(new Range(3, 0, 3, 0), 1, 1);
        // Middle line, plus adjacent lines as context
        assertThat(contents.range(), is(equalTo(new Range(2, 0, 4, 36))));
        assertThat(contents.lines(), contains(
            new Line(2, "public class TestSourceCode {"),
            new Line(3, "    public static void main(String[] args) {"),
            new Line(4, "        System.out.println(\"Hello!\");")
        ));
    }

    @Test
    void testReadLastLine() throws IOException {
        URISourceCode source = new URISourceCode(testSourceFile);
        URIRangeContents contents = source.readRange(new Range(6, 0, 6, 0), 1, 1);
        // Last line, plus one line of context
        assertThat(contents.range(), is(equalTo(new Range(5, 0, 6, 0))));
        assertThat(contents.lines(), contains(
            new Line(5, "    }"),
            new Line(6, "}")
        ));
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(URISourceCode.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(URISourceCode.class).verify();
    }
}
