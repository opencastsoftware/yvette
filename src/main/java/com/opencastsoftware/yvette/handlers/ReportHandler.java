/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.handlers;

import com.opencastsoftware.yvette.Diagnostic;

import java.io.IOException;

public interface ReportHandler {
    void display(Diagnostic diagnostic, Appendable output) throws IOException;
}
