package com.opencastsoftware.yvette;

import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fusesource.jansi.Ansi;

public class AnsiThemeStyles implements ThemeStyles {
    @Override
    public UnaryOperator<Ansi> error() {
        return ansi -> ansi.fgRed();
    }

    @Override
    public UnaryOperator<Ansi> warning() {
        return ansi -> ansi.fgYellow();
    }

    @Override
    public UnaryOperator<Ansi> info() {
        return ansi -> ansi.fgCyan();
    }

    @Override
    public UnaryOperator<Ansi> hint() {
        return ansi -> ansi.fgCyan();
    }

    @Override
    public UnaryOperator<Ansi> help() {
        return ansi -> ansi.fgCyan();
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
    public Iterable<UnaryOperator<Ansi>> highlights() {
        return Stream.<UnaryOperator<Ansi>>of(
            ansi -> ansi.fgMagenta().bold(),
            ansi -> ansi.fgYellow().bold(),
            ansi -> ansi.fgGreen().bold()
        ).collect(Collectors.toList());
    }
}
