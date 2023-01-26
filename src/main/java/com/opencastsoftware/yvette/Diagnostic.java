package com.opencastsoftware.yvette;

import java.net.URI;
import java.util.Collection;

public abstract class Diagnostic extends RuntimeException {
    abstract String code();
    abstract Severity severity();
    abstract String help();
    abstract URI url();
    abstract SourceCode sourceCode();
    abstract Collection<LabelledRange> labels();

    public Diagnostic(String message) {
        super(message);
    }

    public Diagnostic(Throwable cause) {
        super(cause);
    }

    public Diagnostic(String message, Throwable cause) {
        super(message, cause);
    }

    public String message() {
        return getMessage();
    }
}
