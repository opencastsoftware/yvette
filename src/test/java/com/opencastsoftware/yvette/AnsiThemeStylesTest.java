package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AnsiThemeStylesTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(AnsiThemeStyles.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(AnsiThemeStyles.class).verify();
    }
}
