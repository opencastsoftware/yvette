package com.opencastsoftware.yvette;

import java.io.IOException;

public interface ReportHandler {
    void display(Diagnostic diagnostic, Appendable output) throws IOException;
}
