package com.opencastsoftware.yvette;

import java.io.IOException;
import java.io.PrintStream;

import com.opencastsoftware.yvette.handlers.ReportHandler;

public class UncaughtExceptionHandler {
    private static volatile Thread.UncaughtExceptionHandler previous;

    private UncaughtExceptionHandler() {
    }

    static {
        previous = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static Thread.UncaughtExceptionHandler create(ReportHandler handler, PrintStream output) {
        return (thread, exc) -> {
            Diagnostic diagnostic = new BasicDiagnostic(exc.getMessage(), exc.getCause());

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
