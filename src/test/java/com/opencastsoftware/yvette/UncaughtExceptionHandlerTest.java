package com.opencastsoftware.yvette;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.opencastsoftware.yvette.handlers.graphical.GraphicalReportHandler;

import static org.hamcrest.MatcherAssert.*;
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
