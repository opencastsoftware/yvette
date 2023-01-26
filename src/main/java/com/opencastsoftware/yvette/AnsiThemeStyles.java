package com.opencastsoftware.yvette;

import java.util.Collection;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fusesource.jansi.Ansi;

public final class AnsiThemeStyles implements ThemeStyles {
    @Override
    public UnaryOperator<Ansi> error() {
        return Ansi::fgRed;
    }

    @Override
    public UnaryOperator<Ansi> warning() {
        return Ansi::fgYellow;
    }

    @Override
    public UnaryOperator<Ansi> info() {
        return Ansi::fgCyan;
    }

    @Override
    public UnaryOperator<Ansi> hint() {
        return Ansi::fgCyan;
    }

    @Override
    public UnaryOperator<Ansi> help() {
        return Ansi::fgCyan;
    }

    @Override
    public UnaryOperator<Ansi> link() {
        return ansi -> ansi.fgCyan()
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
            ansi -> ansi.fgMagenta().bold(),
            ansi -> ansi.fgYellow().bold(),
            ansi -> ansi.fgGreen().bold()
        ).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof AnsiThemeStyles;
    }

    @Override
    public int hashCode() {
        return 31 * AnsiThemeStyles.class.hashCode();
    }
}
