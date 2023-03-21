/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers.graphical;

import com.opencastsoftware.yvette.Severity;
import org.fusesource.jansi.Ansi;

import java.util.Collection;
import java.util.function.UnaryOperator;

public interface ThemeStyles {
    UnaryOperator<Ansi> error();

    UnaryOperator<Ansi> warning();

    UnaryOperator<Ansi> info();

    UnaryOperator<Ansi> hint();

    UnaryOperator<Ansi> help();

    UnaryOperator<Ansi> link();

    UnaryOperator<Ansi> lineNumber();

    UnaryOperator<Ansi> reset();

    Collection<UnaryOperator<Ansi>> highlights();

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

    static ThemeStyles ansi() {
        return new AnsiThemeStyles();
    }

    static ThemeStyles rgb() {
        return new RgbThemeStyles();
    }

    static ThemeStyles none() {
        return new NoThemeStyles();
    }
}
