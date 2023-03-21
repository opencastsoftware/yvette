/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers.graphical;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class GraphicalThemeTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(GraphicalTheme.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(GraphicalTheme.class).verify();
    }
}
