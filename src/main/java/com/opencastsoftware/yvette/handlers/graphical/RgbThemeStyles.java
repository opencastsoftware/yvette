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

public final class RgbThemeStyles implements ThemeStyles {

    @Override
    public UnaryOperator<Ansi> error() {
        return ansi -> ansi.fgRgb(255, 30, 30);
    }

    @Override
    public UnaryOperator<Ansi> warning() {
        return ansi -> ansi.fgRgb(244, 191, 117);
    }

    @Override
    public UnaryOperator<Ansi> info() {
        return ansi -> ansi.fgRgb(106, 159, 181);
    }

    @Override
    public UnaryOperator<Ansi> hint() {
        return ansi -> ansi.fgRgb(106, 159, 181);
    }

    @Override
    public UnaryOperator<Ansi> help() {
        return ansi -> ansi.fgRgb(106, 159, 181);
    }

    @Override
    public UnaryOperator<Ansi> link() {
        return ansi -> ansi
                .fgRgb(92, 157, 255)
                .a(Ansi.Attribute.UNDERLINE)
                .bold();
    }

    @Override
    public UnaryOperator<Ansi> lineNumber() {
        return ansi -> ansi.a(Ansi.Attribute.INTENSITY_FAINT);
    }

    @Override
    public UnaryOperator<Ansi> reset() {
        return Ansi::reset;
    }

    @Override
    public Collection<UnaryOperator<Ansi>> highlights() {
        return Stream.<UnaryOperator<Ansi>>of(
                ansi -> ansi.fgRgb(246, 87, 248).bold(),
                ansi -> ansi.fgRgb(30, 201, 212).bold(),
                ansi -> ansi.fgRgb(145, 246, 111).bold()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof RgbThemeStyles;
    }

    @Override
    public int hashCode() {
        return 31 * RgbThemeStyles.class.hashCode();
    }

    @Override
    public String toString() {
        return "RgbThemeStyles []";
    }
}
