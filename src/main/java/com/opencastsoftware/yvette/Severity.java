/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

/*
 * The severity of a Diagnostic.
 *
 * Aligned with the DiagnosticSeverity of the Language Server Protocol.
 */
public enum Severity {
    Error(1),
    Warning(2),
    Information(3),
    Hint(4);

    private final int code;

    Severity(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
