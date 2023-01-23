package com.opencastsoftware.yvette;

import java.net.URI;

public interface Diagnostic {
    String code();
    Severity severity();
    String message();
    String help();
    URI url();
    SourceCode sourceCode();
    Iterable<LabelledRange> labels();
    Iterable<Diagnostic> related();
}
