package com.opencastsoftware.yvette.handlers.graphical;

public final class EmojiThemeCharacters implements ThemeCharacters {
    private static final String hBar = "─";
    private static final String vBar = "│";
    private static final String xBar = "┼";
    private static final String vBarBreak = "·";

    private static final String upArrow = "▲";
    private static final String rightArrow = "▶";

    private static final String leftTop = "╭";
    private static final String middleTop = "┬";
    private static final String rightTop = "╮";
    private static final String leftBottom = "╰";
    private static final String rightBottom = "┴";
    private static final String middleBottom = "╯";

    private static final String leftBox = "[";
    private static final String rightBox = "]";

    private static final String leftCross = "├";
    private static final String rightCross = "┤";

    private static final String underBar = "┬";
    private static final String underLine = "─";

    private static final String error = "❌";
    private static final String warning = "️⚠️";
    private static final String info = "ℹ️";
    private static final String hint = "💡";

    public String hBar() {
        return hBar;
    }

    public String vBar() {
        return vBar;
    }

    public String xBar() {
        return xBar;
    }

    public String vBarBreak() {
        return vBarBreak;
    }

    public String upArrow() {
        return upArrow;
    }

    public String rightArrow() {
        return rightArrow;
    }

    public String leftTop() {
        return leftTop;
    }

    public String middleTop() {
        return middleTop;
    }

    public String rightTop() {
        return rightTop;
    }

    public String leftBottom() {
        return leftBottom;
    }

    public String rightBottom() {
        return rightBottom;
    }

    public String middleBottom() {
        return middleBottom;
    }

    public String leftBox() {
        return leftBox;
    }

    public String rightBox() {
        return rightBox;
    }

    public String leftCross() {
        return leftCross;
    }

    public String rightCross() {
        return rightCross;
    }

    public String underBar() {
        return underBar;
    }

    public String underLine() {
        return underLine;
    }

    public String error() {
        return error;
    }

    public String warning() {
        return warning;
    }

    public String info() {
        return info;
    }

    public String hint() {
        return hint;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof EmojiThemeCharacters;
    }

    @Override
    public int hashCode() {
        return 31 * EmojiThemeCharacters.class.hashCode();
    }

    @Override
    public String toString() {
        return "EmojiThemeCharacters []";
    }
}
