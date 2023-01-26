package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class URISourceCodeTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(URISourceCode.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(URISourceCode.class).verify();
    }
}
