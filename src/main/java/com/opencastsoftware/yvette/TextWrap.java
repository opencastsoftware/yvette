package com.opencastsoftware.yvette;

import org.apache.commons.text.WordUtils;
import org.fusesource.jansi.Ansi;

class TextWrap {
    static void fill(
            Ansi ansi, int width,
            String initialIndent, String subsequentIndent,
            String message) {
        String[] lines = message.split("\\r?\\n");

        ansi.append(initialIndent);

        int i = 0;
        for (String line : lines) {
            if (i > 0) {
                ansi.append(subsequentIndent);
            }
            String wrapped = WordUtils.wrap(line, width, subsequentIndent, false);
            ansi.append(wrapped);
            i++;
        }
    }
}
