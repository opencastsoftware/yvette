/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.arbitrary;

import com.opencastsoftware.yvette.handlers.graphical.GraphicalTheme;
import com.opencastsoftware.yvette.handlers.graphical.ThemeCharacters;
import com.opencastsoftware.yvette.handlers.graphical.ThemeStyles;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.Combinators;

public class GraphicalThemeSupplier implements ArbitrarySupplier<GraphicalTheme> {
    @Override
    public Arbitrary<GraphicalTheme> get() {
        return Combinators.combine(
                Arbitraries.of(
                        ThemeCharacters.ascii(),
                        ThemeCharacters.emoji(),
                        ThemeCharacters.unicode()),
                Arbitraries.of(
                        ThemeStyles.ansi(),
                        ThemeStyles.rgb(),
                        ThemeStyles.none()))
                .as(GraphicalTheme::new);
    }
}
