package com.opencastsoftware.yvette.handlers.graphical;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

public class GraphicalThemeTest {
    @Test
    void testEquals() {
        EqualsVerifier.forClass(GraphicalTheme.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(GraphicalTheme.class).verify();
    }
}
