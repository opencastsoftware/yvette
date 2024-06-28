/*
 * SPDX-FileCopyrightText:  © 2023-2024 Opencast Software Europe Ltd <https://opencastsoftware.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.opencastsoftware.prettier4j.Doc;
import com.opencastsoftware.yvette.handlers.ReportHandler;

import java.io.IOException;
import java.io.PrintStream;

public class UncaughtExceptionHandler {
    private static volatile Thread.UncaughtExceptionHandler previous;

    private UncaughtExceptionHandler() {
    }

    static {
        previous = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static Thread.UncaughtExceptionHandler create(ReportHandler handler, PrintStream output) {
        return (thread, exc) -> {
            Diagnostic diagnostic = new BasicDiagnostic(Doc.text(exc.getMessage()), exc.getCause());

            output.format("Uncaught exception in thread %s: ", thread.getName());

            try {
                handler.display(diagnostic, output);
                output.println();
                exc.printStackTrace(output);
            } catch (IOException ioe) {
                ioe.addSuppressed(exc);
                ioe.printStackTrace(output);
            }
        };
    }

    public static void install(ReportHandler handler, PrintStream output) {
        previous = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(create(handler, output));
    }

    public static void uninstall() {
        Thread.setDefaultUncaughtExceptionHandler(previous);
    }
}
