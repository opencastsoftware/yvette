package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AsciiThemeCharactersTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(AsciiThemeCharacters.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(AsciiThemeCharacters.class).verify();
    }
}
