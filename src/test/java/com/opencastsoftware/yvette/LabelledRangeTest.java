/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.opencastsoftware.yvette.handlers.graphical.StyledRange;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class LabelledRangeTest {
    @Test
    void testEquals() {
        EqualsVerifier
            .forClass(LabelledRange.class)
            .usingGetClass()
            .withRedefinedSubclass(StyledRange.class)
            .withRedefinedSuperclass()
            .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(LabelledRange.class).verify();
    }
}
