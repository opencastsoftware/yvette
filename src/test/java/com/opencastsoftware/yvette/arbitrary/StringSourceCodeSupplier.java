package com.opencastsoftware.yvette.arbitrary;

import com.opencastsoftware.yvette.StringSourceCode;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;

public class StringSourceCodeSupplier implements ArbitrarySupplier<StringSourceCode> {
    @Override
    public Arbitrary<StringSourceCode> get() {
        return Arbitraries.strings()
                .list()
                .ofMinSize(0)
                .ofMaxSize(10)
                .map(strings -> new StringSourceCode(null, String.join(System.lineSeparator(), strings)));
    }
}
