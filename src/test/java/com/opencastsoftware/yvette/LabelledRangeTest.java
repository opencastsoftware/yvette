package com.opencastsoftware.yvette;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.opencastsoftware.yvette.handlers.graphical.StyledRange;

import nl.jqno.equalsverifier.EqualsVerifier;

public class LabelledRangeTest {
    @Test
    void testEquals() {
        EqualsVerifier
            .forClass(LabelledRange.class)
            .usingGetClass()
            .withRedefinedSubclass(StyledRange.class)
            .withRedefinedSuperclass()
            .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(LabelledRange.class).verify();
    }
}
