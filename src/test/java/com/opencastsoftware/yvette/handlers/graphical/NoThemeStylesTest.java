package com.opencastsoftware.yvette.handlers.graphical;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class NoThemeStylesTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(NoThemeStyles.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(NoThemeStyles.class).verify();
    }
}
