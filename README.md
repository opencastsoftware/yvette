# yvette

[![CI](https://github.com/opencastsoftware/yvette/actions/workflows/ci.yml/badge.svg)](https://github.com/opencastsoftware/yvette/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/opencastsoftware/yvette/branch/main/graph/badge.svg?token=JHVF151VM1)](https://codecov.io/gh/opencastsoftware/yvette)
[![Maven Central](https://img.shields.io/maven-central/v/com.opencastsoftware/yvette)](https://search.maven.org/search?q=g%3Acom.opencastsoftware+AND+a%3Ayvette)
[![javadoc](https://javadoc.io/badge2/com.opencastsoftware/yvette/javadoc.svg)](https://javadoc.io/doc/com.opencastsoftware/yvette)
[![License](https://img.shields.io/github/license/opencastsoftware/yvette?color=blue)](https://spdx.org/licenses/Apache-2.0.html)

A diagnostic reporting library for Java, ported from the Rust library [miette](https://github.com/zkat/miette).

![An example of a graphical error message produced by this library is shown. The error message text is preceded by a red cross symbol. This is followed by the message "Mismatched type! Expected: Int, Actual: Tree\[A\]". Following the error message a source code snippet is printed, headed by a hyperlink pointing to the underlying file and line number that is in error. Line numbers are shown in the margin indicating the location of the snippet in the underlying source file. The specific term in the source code that is in error is underlined using a blue line.](./images/example-report.png)

## Installation

*yvette* is published for Java 11 and above.

Gradle (build.gradle / build.gradle.kts):
```groovy
implementation("com.opencastsoftware:yvette:0.1.0")
```

Maven (pom.xml):
```xml
<dependency>
    <groupId>com.opencastsoftware</groupId>
    <artifactId>yvette</artifactId>
    <version>0.1.0</version>
</dependency>
```

To use the `GraphicalReportHandler` and other features of the package `com.opencastsoftware.yvette.handlers.graphical`, the [jansi](https://github.com/fusesource/jansi) library is also needed:

Gradle (build.gradle / build.gradle.kts):
```groovy
implementation("org.fusesource.jansi:jansi:2.4.0")
```

Maven (pom.xml):
```xml
<dependency>
    <groupId>org.fusesource.jansi</groupId>
    <artifactId>jansi</artifactId>
    <version>2.4.0</version>
</dependency>
```

## Displaying diagnostics

In order to display diagnostics with *yvette*, your application's error messages must implement the abstract class [Diagnostic](./src/main/java/com/opencastsoftware/yvette/Diagnostic.java). All of the methods of this class may return `null`, but bear in mind that your diagnostics may not be very helpful without a `message`.

A [BasicDiagnostic](./src/main/java/com/opencastsoftware/yvette/BasicDiagnostic.java) implementation is provided, which can be used to wrap exceptions:

```java
Diagnostic err = new BasicDiagnostic(e.getMessage(), e.getCause());
```

You can then implement a [ReportHandler](./src/main/java/com/opencastsoftware/yvette/handlers/ReportHandler.java) to display your diagnostics using the [Appendable](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Appendable.html) instance you wish to use for output, for example [System.out](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#out), [System.err](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#err) or a [StringBuilder](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuilder.html).

*yvette* provides a [GraphicalReportHandler](./src/main/java/com/opencastsoftware/yvette/handlers/graphical/GraphicalReportHandler.java) which produces output like the screenshot above. You can create one of these using the `GraphicalReportHandler.builder()` static method:

```java
ReportHandler reportHandler = GraphicalReportHandler.builder()
    .withRgbColours(RgbColours.PREFERRED)
    .buildFor(System.err);
```

There are some terminal feature detection features in *yvette*. If you wish to bypass these, use the `withColours`, `withTerminalWidth` and `withUnicode` methods of the builder to enable or disable those features explicitly. However, please bear in mind that using `withColours` to force enable colour output will override the [NO_COLOR](https://no-color.org/) detection implemented by this library.

Once you have a [ReportHandler](./src/main/java/com/opencastsoftware/yvette/handlers/ReportHandler.java), it can be used to output diagnostics:

```java
Diagnostic diagnostic = ???; // A diagnostic from your application
reportHandler.display(diagnostic, System.err);
```

## Uncaught exception handler

*yvette* provides an implementation of [Thread.UncaughtExceptionHandler](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Thread.UncaughtExceptionHandler.html) which can be used to replace the default handler for all threads.

```java
import com.opencastsoftware.yvette.UncaughtExceptionHandler;

ReportHandler handler = ???; // Your report handler, obtained as described above

try {
    UncaughtExceptionHandler.install(handler, System.err); // Installs the new handler
} finally {
    UncaughtExceptionHandler.uninstall(); // Restores the default handler
}
```

The setup above will print diagnostics to STDERR:

```java
new Thread(() -> {
    Throwable exc = new FileNotFoundException("Couldn't find the file BadFile.java");
    exc.initCause(new AccessDeniedException("Access denied to file BadFile.java"));
    throw new RuntimeException("Whoops!", exc);
}).start();

/*
Uncaught exception in thread Thread-5:
    x Whoops!
    |-> Couldn't find the file BadFile.java
    `-> Access denied to file BadFile.java

java.lang.RuntimeException: Whoops!
    at com.opencastsoftware.yvette.UncaughtExceptionHandlerTest.lambda$replacesThreadPoolHandler$2(UncaughtExceptionHandlerTest.java:87)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
    at java.lang.Thread.run(Thread.java:750)
Caused by: java.io.FileNotFoundException: Couldn't find the file BadFile.java
    at com.opencastsoftware.yvette.UncaughtExceptionHandlerTest.lambda$replacesThreadPoolHandler$2(UncaughtExceptionHandlerTest.java:85)
    ... 3 more
Caused by: java.nio.file.AccessDeniedException: Access denied to file BadFile.java
    at com.opencastsoftware.yvette.UncaughtExceptionHandlerTest.lambda$replacesThreadPoolHandler$2(UncaughtExceptionHandlerTest.java:86)
    ... 3 more
*/
```

It can also be set as the uncaught exception handler for new threads in a thread pool by using a [ThreadFactory](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ThreadFactory.html):

```java
Thread.UncaughtExceptionHandler excHandler = UncaughtExceptionHandler.create(handler, System.err);

ThreadFactory threadFactory = runnable -> {
    Thread thread = new Thread(runnable);
    thread.setUncaughtExceptionHandler(excHandler);
    thread.setDaemon(true);
    return newThread;
};
```

## Deviations and Limitations

This is not an exact port of *miette* - there are some differences and unported features:

* Where *miette* and the [Language Server Protocol](https://microsoft.github.io/language-server-protocol/)'s definitions deviate, we have erred on the side of alignment with LSP. This is for ease of integrating *yvette* with language server applications. For example, *miette* uses `SourceSpan` to keep track of the byte offset and length of a span within a source file. However, *yvette*'s equivalent of `SourceSpan` is called `Range`, and specifies a start and end `Position`, each of which refers to a zero-indexed line and character position within a document. The upside of this is alignment with LSP, but the downside is that we can no longer efficiently read arbitrary offsets of a source file in order to get the span contents.
* Only the `GraphicalReportHandler` and `ToStringReportHandler` are currently implemented, there is no `NarratableReportHandler` or `JSONReportHandler` as yet.
* There is no special handling of tab characters or [unicode character width](https://crates.io/crates/unicode-width) in *yvette* yet, which may mean that your highlights are misaligned with the source code they are supposed to underline.
* Related diagnostics are not currently implemented in *yvette*, since the definition of `DiagnosticRelatedInformation` is sufficiently different in the Language Server Protocol that we haven't decided how to implement it yet.

## Acknowlegements

This project wouldn't exist without the work of [zkat](https://github.com/zkat) and the other miette contributors.

## License

All code in this repository is licensed under the Apache License, Version 2.0. See [LICENSE](./LICENSE).