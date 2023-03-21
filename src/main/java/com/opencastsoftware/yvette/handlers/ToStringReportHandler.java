/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers;

import com.opencastsoftware.yvette.Diagnostic;

import java.io.IOException;

public final class ToStringReportHandler implements ReportHandler {
    @Override
    public void display(Diagnostic diagnostic, Appendable output) throws IOException {
        output.append(diagnostic.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof ToStringReportHandler;
    }

    @Override
    public int hashCode() {
        return 31 * ToStringReportHandler.class.hashCode();
    }

    @Override
    public String toString() {
        return "ToStringReportHandler []";
    }
}
