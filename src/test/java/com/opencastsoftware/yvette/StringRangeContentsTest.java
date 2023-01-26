package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class StringRangeContentsTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(StringRangeContents.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(StringRangeContents.class).verify();
    }
}
