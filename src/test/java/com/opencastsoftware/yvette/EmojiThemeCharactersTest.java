package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class EmojiThemeCharactersTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(EmojiThemeCharacters.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(EmojiThemeCharacters.class).verify();
    }
}
