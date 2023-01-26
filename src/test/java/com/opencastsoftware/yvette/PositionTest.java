package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class PositionTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(Position.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(Position.class).verify();
    }
}
