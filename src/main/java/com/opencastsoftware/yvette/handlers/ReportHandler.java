package com.opencastsoftware.yvette.handlers;

import java.io.IOException;

import com.opencastsoftware.yvette.Diagnostic;

public interface ReportHandler {
    void display(Diagnostic diagnostic, Appendable output) throws IOException;
}
