package com.opencastsoftware.yvette;

public class GraphicalTheme {
    private ThemeCharacters characters;
    private ThemeStyles styles;

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
}
