package com.opencastsoftware.yvette.handlers.graphical;

public enum ColourSupport {
    NONE(0),
    COLOUR_16(1),
    COLOUR_256(2),
    COLOUR_16M(3);

    private final int level;

    ColourSupport(int level) {
        this.level = level;
    }

    boolean has16ColourSupport() {
        return level >= 1;
    }

    boolean has256ColourSupport() {
        return level >= 2;
    }

    boolean has16MColourSupport() {
        return level >= 3;
    }
}
