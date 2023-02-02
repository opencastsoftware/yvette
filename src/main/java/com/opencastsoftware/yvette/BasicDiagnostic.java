package com.opencastsoftware.yvette;

import java.net.URI;
import java.util.Collection;

public class BasicDiagnostic extends Diagnostic {
    public String code;
    public Severity severity;
    public String help;
    public URI url;
    public SourceCode sourceCode;
    public Collection<LabelledRange> labels;

    public BasicDiagnostic(String message) {
        this(null, Severity.Error, message, null, null, null, null);
    }

    public BasicDiagnostic(
            String message,
            Throwable cause) {
        this(null, Severity.Error, message, cause, null, null, null, null);
    }

    public BasicDiagnostic(
            String message,
            Throwable cause,
            URI url) {
        this(null, Severity.Error, message, cause, null, url, null, null);
    }

    public BasicDiagnostic(
            String code,
            Severity severity,
            String message,
            String help,
            URI url,
            SourceCode sourceCode,
            Collection<LabelledRange> labels) {
        super(message);
    }

    public BasicDiagnostic(
            String code,
            Severity severity,
            String message, Throwable cause,
            String help,
            URI url,
            SourceCode sourceCode,
            Collection<LabelledRange> labels) {
        super(message, cause);
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
    public String message() {
        return getMessage();
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
    public Collection<LabelledRange> labels() {
        return labels;
    }
}
