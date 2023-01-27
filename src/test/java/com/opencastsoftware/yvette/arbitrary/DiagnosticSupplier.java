package com.opencastsoftware.yvette.arbitrary;

import java.util.Collections;
import java.util.List;

import com.opencastsoftware.yvette.*;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.Combinators;

public class DiagnosticSupplier implements ArbitrarySupplier<Diagnostic> {
    @Override
    public Arbitrary<Diagnostic> get() {
        return Combinators.combine(
                Arbitraries.lazy(new CodesSupplier()),
                Arbitraries.lazy(new SeveritySupplier()),
                Arbitraries.strings(),
                Arbitraries.strings(),
                Arbitraries.lazy(new URLSupplier()),
                Arbitraries.lazy(new StringSourceCodeSupplier()))
                .flatAs((code, severity, message, help, url, sourceCode) -> {
                    // This would split to an empty array later
                    if (sourceCode.source().matches("\\r?\\n")) {
                        return Arbitraries.just(
                                new TestDiagnostic(
                                        code, severity,
                                        message, help, url,
                                        sourceCode, Collections.emptyList()));
                    } else {
                        return labels(sourceCode).map(labels -> {
                            return new TestDiagnostic(
                                    code, severity,
                                    message, help, url,
                                    sourceCode, labels);
                        });
                    }
                });
    }

    /*
     * Valid labelled ranges within the bounds of the source code
     */
    private Arbitrary<List<LabelledRange>> labels(StringSourceCode source) {
        return range(source)
                .flatMap(range -> {
                    return Arbitraries.strings()
                            .map(label -> new LabelledRange(label, range));
                })
                .list();
    }

    /*
     * Valid ranges within the bounds of the source code
     */
    Arbitrary<Range> range(StringSourceCode source) {
        return position(source)
                .tuple2()
                .map(tuple -> {
                    Position pos1 = tuple.get1();
                    Position pos2 = tuple.get2();
                    if (pos1.compareTo(pos2) < 0) {
                        return new Range(pos1, pos2);
                    } else {
                        return new Range(pos2, pos2);
                    }
                });
    }

    /*
     * Character positions within the bounds of each line of source code
     */
    private Arbitrary<Position> position(StringSourceCode source) {
        String[] lines = source.source().split("\\r?\\n");
        return Arbitraries.integers()
                .between(0, Math.max(0, lines.length - 1))
                .flatMap(line -> {
                    return Arbitraries.integers()
                            .between(0, Math.max(0, lines[line].length() - 1))
                            .map(character -> new Position(line, character));
                });
    }
}
