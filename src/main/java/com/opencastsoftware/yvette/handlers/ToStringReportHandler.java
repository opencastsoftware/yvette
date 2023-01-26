package com.opencastsoftware.yvette.handlers;

import java.io.IOException;

import com.opencastsoftware.yvette.Diagnostic;

public class ToStringReportHandler implements ReportHandler {
    @Override
    public void display(Diagnostic diagnostic, Appendable output) throws IOException {
        output.append(diagnostic.toString());
    }

    @Override
    public String toString() {
        return "ToStringReportHandler []";
    }
}
