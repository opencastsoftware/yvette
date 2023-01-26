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

    @Override
    String code() {
        return code;
    }

    @Override
    Severity severity() {
        return severity;
    }

    @Override
    String help() {
        return help;
    }

    @Override
    URI url() {
        return url;
    }

    @Override
    SourceCode sourceCode() {
        return sourceCode;
    }

    @Override
    List<LabelledRange> labels() {
        return labels;
    }
}
