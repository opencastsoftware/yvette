/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers.graphical;

import org.fusesource.jansi.Ansi;

import java.util.function.UnaryOperator;

public class GraphicalTheme {
    private final ThemeCharacters characters;
    private final ThemeStyles styles;

    public GraphicalTheme(ThemeCharacters characters, ThemeStyles styles) {
        this.characters = characters;
        this.styles = styles;
    }

    public ThemeCharacters characters() {
        return characters;
    }

    public ThemeStyles styles() {
        return styles;
    }

    public Ansi withStyle(Ansi ansi, UnaryOperator<Ansi> style, UnaryOperator<Ansi> op) {
        return style.andThen(op)
                .andThen(styles.reset())
                .apply(ansi);
    }

    public static GraphicalTheme ascii() {
        return new GraphicalTheme(ThemeCharacters.ascii(), ThemeStyles.ansi());
    }

    public static GraphicalTheme emoji() {
        return new GraphicalTheme(ThemeCharacters.emoji(), ThemeStyles.rgb());
    }

    public static GraphicalTheme unicode() {
        return new GraphicalTheme(ThemeCharacters.unicode(), ThemeStyles.rgb());
    }

    public static GraphicalTheme unicodeNoColour() {
        return new GraphicalTheme(ThemeCharacters.unicode(), ThemeStyles.none());
    }

    public static GraphicalTheme none() {
        return new GraphicalTheme(ThemeCharacters.ascii(), ThemeStyles.none());
    }

    public static GraphicalTheme getDefault() {
        String noColour = System.getenv("NO_COLOR");
        if (noColour != null && !noColour.isEmpty()) {
            return GraphicalTheme.unicodeNoColour();
        } else {
            return GraphicalTheme.unicode();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((characters == null) ? 0 : characters.hashCode());
        result = prime * result + ((styles == null) ? 0 : styles.hashCode());
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
        GraphicalTheme other = (GraphicalTheme) obj;
        if (characters == null) {
            if (other.characters != null)
                return false;
        } else if (!characters.equals(other.characters))
            return false;
        if (styles == null) {
            if (other.styles != null)
                return false;
        } else if (!styles.equals(other.styles))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GraphicalTheme [characters=" + characters + ", styles=" + styles + "]";
    }
}
