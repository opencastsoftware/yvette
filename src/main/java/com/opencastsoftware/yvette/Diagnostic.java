package com.opencastsoftware.yvette;

import java.net.URI;
import java.util.Collection;

public abstract class Diagnostic extends RuntimeException {
    public abstract String code();
    public abstract Severity severity();
    public abstract String help();
    public abstract URI url();
    public abstract SourceCode sourceCode();
    public abstract Collection<LabelledRange> labels();

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
