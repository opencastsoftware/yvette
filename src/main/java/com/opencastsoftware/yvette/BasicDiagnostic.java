/*
 * SPDX-FileCopyrightText:  © 2023-2024 Opencast Software Europe Ltd <https://opencastsoftware.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.opencastsoftware.prettier4j.Doc;

import java.net.URI;
import java.util.Collection;

public class BasicDiagnostic extends Diagnostic {
    public String code;
    public Severity severity;
    public Doc message;
    public Doc help;
    public URI url;
    public SourceCode sourceCode;
    public Collection<LabelledRange> labels;

    public BasicDiagnostic(Doc message) {
        this(null, Severity.Error, message, null, null, null, null);
    }

    public BasicDiagnostic(
            Doc message,
            Throwable cause) {
        this(null, Severity.Error, message, cause, null, null, null, null);
    }

    public BasicDiagnostic(
            Doc message,
            Throwable cause,
            URI url) {
        this(null, Severity.Error, message, cause, null, url, null, null);
    }

    public BasicDiagnostic(
            String code,
            Severity severity,
            Doc message,
            Doc help,
            URI url,
            SourceCode sourceCode,
            Collection<LabelledRange> labels) {
        super();
        this.message = message;
    }

    public BasicDiagnostic(
            String code,
            Severity severity,
            Doc message, Throwable cause,
            Doc help,
            URI url,
            SourceCode sourceCode,
            Collection<LabelledRange> labels) {
        super(cause);
        this.message = message;
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
    public Doc message() {
        return message;
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
    public Collection<LabelledRange> labels() {
        return labels;
    }
}
