/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.opencastsoftware.yvette.Diagnostic;
import com.opencastsoftware.yvette.arbitrary.DiagnosticSupplier;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ToStringReportHandlerTest {
    private final ToStringReportHandler handler = new ToStringReportHandler();

    @Test
    void testEquals() {
        EqualsVerifier.forClass(ToStringReportHandler.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ToStringReportHandler.class).verify();
    }

    @Property
    void equivalentToCallingToString(
            @ForAll(supplier = DiagnosticSupplier.class) Diagnostic diagnostic) throws IOException {
        StringBuilder sb = new StringBuilder();
        handler.display(diagnostic, sb);
        assertThat(sb.toString(), is(equalTo(diagnostic.toString())));
    }
}
