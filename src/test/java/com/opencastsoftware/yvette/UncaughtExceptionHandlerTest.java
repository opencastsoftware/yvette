/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import com.opencastsoftware.yvette.handlers.ReportHandler;
import com.opencastsoftware.yvette.handlers.graphical.GraphicalReportHandler;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UncaughtExceptionHandlerTest {

    @Test
    void replacesDefaultHandler() throws InterruptedException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos, true);

        GraphicalReportHandler handler = GraphicalReportHandler.builder()
                .withColours(false)
                .withUnicode(false)
                .withTerminalLinks(false)
                .buildFor(printStream);

        try {
            UncaughtExceptionHandler.install(handler, printStream);

            Thread throwingThread = new Thread(() -> {
                Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
                exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));
                throw new RuntimeException("Whoops!", exc);
            });

            throwingThread.start();
            throwingThread.join();

            String diagnosticOutput = baos.toString(StandardCharsets.UTF_8.name());

            assertThat(
                    diagnosticOutput,
                    containsString("Uncaught exception in thread"));

            assertThat(
                    diagnosticOutput,
                    containsString(String.join(System.lineSeparator(),
                            "  x Whoops!",
                            "  |-> Couldn't find the file BadFile.java",
                            "  `-> Access denied to file BadFile.java")));
        } finally {
            UncaughtExceptionHandler.uninstall();
        }

        assertThat(Thread.getDefaultUncaughtExceptionHandler(), is(nullValue()));
    }

    @Test
    void handlesExceptionInHandler() throws InterruptedException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos, true);

        ReportHandler handler = new ReportHandler() {
            @Override
            public void display(Diagnostic diagnostic, Appendable output) throws IOException {
                throw new IOException("Uh oh, this went very badly");
            }
        };

        try {
            UncaughtExceptionHandler.install(handler, printStream);

            Thread throwingThread = new Thread(() -> {
                Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
                exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));
                throw new RuntimeException("Whoops!", exc);
            });

            throwingThread.start();
            throwingThread.join();

            String diagnosticOutput = baos.toString(StandardCharsets.UTF_8.name());

            assertThat(
                    diagnosticOutput,
                    containsString("Uncaught exception in thread"));

            assertThat(
                    diagnosticOutput,
                    containsString("java.io.IOException: Uh oh, this went very badly"));

            assertThat(
                    diagnosticOutput,
                    containsString("Suppressed: java.lang.RuntimeException: Whoops!"));

        } finally {
            UncaughtExceptionHandler.uninstall();
        }

        assertThat(Thread.getDefaultUncaughtExceptionHandler(), is(nullValue()));
    }

    @Test
    void replacesThreadPoolHandler() throws InterruptedException, UnsupportedEncodingException {
        CountDownLatch excHandled = new CountDownLatch(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos, true);

        GraphicalReportHandler handler = GraphicalReportHandler.builder()
                .withColours(false)
                .withUnicode(false)
                .withTerminalLinks(false)
                .buildFor(printStream);

        Thread.UncaughtExceptionHandler excHandler = UncaughtExceptionHandler.create(handler, printStream);

        ThreadFactory threadFactory = runnable -> {
            Thread newThread = new Thread(runnable);
            newThread.setDaemon(true);
            newThread.setUncaughtExceptionHandler((t, ex) -> {
                excHandler.uncaughtException(t, ex);
                excHandled.countDown();
            });
            return newThread;
        };

        ExecutorService singleThread = Executors.newSingleThreadExecutor(threadFactory);

        singleThread.execute(() -> {
            Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
            exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));
            throw new RuntimeException("Whoops!", exc);
        });

        excHandled.await();
        singleThread.shutdown();

        assertThat("Executor should shut down", singleThread.awaitTermination(1, TimeUnit.SECONDS));

        String diagnosticOutput = baos.toString(StandardCharsets.UTF_8.name());

        assertThat(
                diagnosticOutput,
                containsString("Uncaught exception in thread"));

        assertThat(
                diagnosticOutput,
                containsString(String.join(System.lineSeparator(),
                        "  x Whoops!",
                        "  |-> Couldn't find the file BadFile.java",
                        "  `-> Access denied to file BadFile.java")));
    }
}
