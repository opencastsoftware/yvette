package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class URIRangeContentsTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(URIRangeContents.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(URIRangeContents.class).verify();
    }
}
