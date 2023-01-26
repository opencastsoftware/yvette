package com.opencastsoftware.yvette;

public interface ThemeCharacters {
    String hBar();
    String vBar();
    String xBar();
    String vBarBreak();

    String upArrow();
    String rightArrow();

    String leftTop();
    String middleTop();
    String rightTop();
    String leftBottom();
    String rightBottom();
    String middleBottom();

    String leftBox();
    String rightBox();

    String leftCross();
    String rightCross();

    String underBar();
    String underLine();

    String error();
    String warning();
    String info();
    String hint();

    default String forSeverity(Severity severity) {
        String icon = null;

        switch (severity) {
            case Error:
                icon = error();
                break;
            case Warning:
                icon = warning();
                break;
            case Information:
                icon = info();
                break;
            case Hint:
                icon = hint();
                break;
        }

        return icon;
    }

    static ThemeCharacters ascii() {
        return new AsciiThemeCharacters();
    }

    static ThemeCharacters unicode() {
        return new UnicodeThemeCharacters();
    }

    static ThemeCharacters emoji() {
        return new EmojiThemeCharacters();
    }
}
