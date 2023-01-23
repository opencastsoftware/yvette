package com.opencastsoftware.yvette;

import java.util.function.UnaryOperator;

import org.fusesource.jansi.Ansi;

public interface ThemeStyles {
    UnaryOperator<Ansi> error();
    UnaryOperator<Ansi> warning();
    UnaryOperator<Ansi> info();
    UnaryOperator<Ansi> hint();
    UnaryOperator<Ansi> help();
    UnaryOperator<Ansi> link();
    UnaryOperator<Ansi> lineNumber();
    Iterable<UnaryOperator<Ansi>> highlights();

    default UnaryOperator<Ansi> forSeverity(Severity severity) {
        UnaryOperator<Ansi> style = null;

        switch (severity) {
            case Error:
                style = error();
                break;
            case Warning:
                style = warning();
                break;
            case Information:
                style = info();
                break;
            case Hint:
                style = hint();
                break;
        }

        return style;
    }

    public static ThemeStyles ansi() {
        return new AnsiThemeStyles();
    }

    public static ThemeStyles rgb() {
        return new RgbThemeStyles();
    }

    public static ThemeStyles none() {
        return NoThemeStyles.INSTANCE;
    }
}
