/*
 * SPDX-FileCopyrightText:  © 2023-2024 Opencast Software Europe Ltd <https://opencastsoftware.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.opencastsoftware.prettier4j.Doc;

import java.net.URI;
import java.util.List;

public class TestDiagnostic extends Diagnostic {
    private final String code;
    private final Doc message;
    private final Severity severity;
    private final Doc help;
    private final URI url;
    private final SourceCode sourceCode;
    private final List<LabelledRange> labels;

    public TestDiagnostic(String code, Severity severity, Doc message, Doc help, URI url, SourceCode sourceCode, List<LabelledRange> labels) {
        super();
        this.code = code;
        this.severity = severity;
        this.message = message;
        this.help = help;
        this.url = url;
        this.sourceCode = sourceCode;
        this.labels = labels;
    }

    public TestDiagnostic(String code, Severity severity, Doc message, Throwable cause, Doc help, URI url, SourceCode sourceCode, List<LabelledRange> labels) {
        super(cause);
        this.code = code;
        this.severity = severity;
        this.message = message;
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
    public Doc message() {
        return message;
    }

    @Override
    public Severity severity() {
        return severity;
    }

    @Override
    public Doc help() {
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
