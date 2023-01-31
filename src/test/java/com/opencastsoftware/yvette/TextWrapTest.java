package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

public class TextWrapTest {
    @Test
    void testEmptyInput() throws IOException {
        StringBuilder builder = new StringBuilder();
        TextWrap.fill(builder, 12, "  ",  "\n  ", "");
        assertThat(builder.toString(), is(equalTo("  ")));
    }

    @Test
    void testNoWrap() throws IOException {
        StringBuilder builder = new StringBuilder();
        TextWrap.fill(builder, 12, "  ",  "\n  ", "hello world");
        assertThat(builder.toString(), is(equalTo("  hello world")));
    }

    @Test
    void testSingleLineWrap() throws IOException {
        StringBuilder builder = new StringBuilder();
        TextWrap.fill(builder, 5, "  ",  "\n  ", "hello world");
        assertThat(builder.toString(), is(equalTo("  hello\n  world")));
    }

    @Test
    void testMultipleLineWrap() throws IOException {
        StringBuilder builder = new StringBuilder();
        TextWrap.fill(builder, 12, "  ",  "\n  ", "hello world\nhope you are well today");
        assertThat(builder.toString(), is(equalTo("  hello world\n  hope you are\n  well today")));
    }
}
