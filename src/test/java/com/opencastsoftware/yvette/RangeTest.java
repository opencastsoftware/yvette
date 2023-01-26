package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class RangeTest {
    @Test
    void testEquals() {
        EqualsVerifier
                .forClass(Range.class)
                .usingGetClass()
                .withRedefinedSubclass(LabelledRange.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(Range.class).verify();
    }
}
