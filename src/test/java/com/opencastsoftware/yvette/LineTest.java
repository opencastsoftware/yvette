/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class LineTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(Line.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(Line.class).verify();
    }
}
