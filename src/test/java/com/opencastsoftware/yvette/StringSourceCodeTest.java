package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class StringSourceCodeTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(StringSourceCode.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(StringSourceCode.class).verify();
    }
}
