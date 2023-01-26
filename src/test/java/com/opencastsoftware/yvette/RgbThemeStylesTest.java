package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class RgbThemeStylesTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(RgbThemeStyles.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(RgbThemeStyles.class).verify();
    }
}
