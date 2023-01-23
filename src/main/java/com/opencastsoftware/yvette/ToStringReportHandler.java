package com.opencastsoftware.yvette;

import java.io.IOException;

public class ToStringReportHandler implements ReportHandler {
    @Override
    public void display(Diagnostic diagnostic, Appendable output) throws IOException {
        output.append(diagnostic.toString());
    }
}
