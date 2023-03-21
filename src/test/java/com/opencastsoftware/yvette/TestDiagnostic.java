/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import java.net.URI;
import java.util.List;

public class TestDiagnostic extends Diagnostic {
    private final String code;
    private final Severity severity;
    private final String help;
    private final URI url;
    private final SourceCode sourceCode;
    private final List<LabelledRange> labels;

    public TestDiagnostic(String code, Severity severity, String message, String help, URI url, SourceCode sourceCode, List<LabelledRange> labels) {
        super(message);
        this.code = code;
        this.severity = severity;
        this.help = help;
        this.url = url;
        this.sourceCode = sourceCode;
        this.labels = labels;
    }

    public TestDiagnostic(String code, Severity severity, String message, Throwable cause, String help, URI url, SourceCode sourceCode, List<LabelledRange> labels) {
        super(message, cause);
        this.code = code;
        this.severity = severity;
        this.help = help;
        this.url = url;
        this.sourceCode = sourceCode;
        this.labels = labels;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public Severity severity() {
        return severity;
    }

    @Override
    public String help() {
        return help;
    }

    @Override
    public URI url() {
        return url;
    }

    @Override
    public SourceCode sourceCode() {
        return sourceCode;
    }

    @Override
    public List<LabelledRange> labels() {
        return labels;
    }
}
