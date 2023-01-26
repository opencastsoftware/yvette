package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

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
