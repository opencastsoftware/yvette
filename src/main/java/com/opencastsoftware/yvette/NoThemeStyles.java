package com.opencastsoftware.yvette;

import java.util.function.UnaryOperator;

import org.fusesource.jansi.Ansi;

public enum NoThemeStyles implements ThemeStyles {
    INSTANCE;

    @Override
    public UnaryOperator<Ansi> error() {
        return null;
    }

    @Override
    public UnaryOperator<Ansi> warning() {
        return null;
    }

    @Override
    public UnaryOperator<Ansi> info() {
        return null;
    }

    @Override
    public UnaryOperator<Ansi> hint() {
        return null;
    }

    @Override
    public UnaryOperator<Ansi> help() {
        return null;
    }

    @Override
    public UnaryOperator<Ansi> link() {
        return null;
    }

    @Override
    public UnaryOperator<Ansi> lineNumber() {
        return null;
    }

    @Override
    public Iterable<UnaryOperator<Ansi>> highlights() {
        return null;
    }
}
