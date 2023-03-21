/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers.graphical;

import org.fusesource.jansi.Ansi;

import java.util.Collection;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class NoThemeStyles implements ThemeStyles {

    private static final UnaryOperator<Ansi> identity = UnaryOperator.identity();

    @Override
    public UnaryOperator<Ansi> error() {
        return identity;
    }

    @Override
    public UnaryOperator<Ansi> warning() {
        return identity;
    }

    @Override
    public UnaryOperator<Ansi> info() {
        return identity;
    }

    @Override
    public UnaryOperator<Ansi> hint() {
        return identity;
    }

    @Override
    public UnaryOperator<Ansi> help() {
        return identity;
    }

    @Override
    public UnaryOperator<Ansi> link() {
        return identity;
    }

    @Override
    public UnaryOperator<Ansi> lineNumber() {
        return identity;
    }

    @Override
    public UnaryOperator<Ansi> reset() {
        return identity;
    }

    @Override
    public Collection<UnaryOperator<Ansi>> highlights() {
        return Stream.of(identity).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof NoThemeStyles;
    }

    @Override
    public int hashCode() {
        return 31 * NoThemeStyles.class.hashCode();
    }

    @Override
    public String toString() {
        return "NoThemeStyles []";
    }
}
