package com.opencastsoftware.yvette;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.text.WordUtils;

public class TextWrap {
    private TextWrap() {
    }

    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

    public static void fill(
            Appendable appendable, int width,
            String initialIndent, String subsequentIndent,
            String message) throws IOException {
        String[] lines = NEWLINE_PATTERN.split(message);

        appendable.append(initialIndent);

        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                appendable.append(subsequentIndent);
            }
            String wrapped = WordUtils.wrap(lines[i], width, subsequentIndent, false);
            appendable.append(wrapped);
        }
    }
}
