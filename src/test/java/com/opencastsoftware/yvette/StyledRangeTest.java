package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class StyledRangeTest {
    @Test
    void testEquals() {
        EqualsVerifier
            .forClass(StyledRange.class)
            .usingGetClass()
            .withRedefinedSuperclass()
            .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(StyledRange.class).verify();
    }
}
