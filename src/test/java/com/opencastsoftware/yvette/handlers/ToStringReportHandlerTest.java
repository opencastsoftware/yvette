package com.opencastsoftware.yvette.handlers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.opencastsoftware.yvette.Diagnostic;
import com.opencastsoftware.yvette.arbitrary.DiagnosticSupplier;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import nl.jqno.equalsverifier.EqualsVerifier;

public class ToStringReportHandlerTest {
    ToStringReportHandler handler = new ToStringReportHandler();

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
