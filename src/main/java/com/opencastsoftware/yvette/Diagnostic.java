/*
 * SPDX-FileCopyrightText:  © 2023-2024 Opencast Software Europe Ltd <https://opencastsoftware.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.opencastsoftware.prettier4j.Doc;

import java.net.URI;
import java.util.Collection;

public abstract class Diagnostic extends RuntimeException {
    public abstract String code();
    public abstract Doc message();
    public abstract Severity severity();
    public abstract Doc help();
    public abstract URI url();
    public abstract SourceCode sourceCode();
    public abstract Collection<LabelledRange> labels();

    public Diagnostic() {
        super();
    }

    public Diagnostic(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return message().render();
    }
}
