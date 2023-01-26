package com.opencastsoftware.yvette.handlers.graphical;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class UnicodeThemeCharactersTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(UnicodeThemeCharacters.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(UnicodeThemeCharacters.class).verify();
    }
}
