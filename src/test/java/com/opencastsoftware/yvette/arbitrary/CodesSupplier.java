package com.opencastsoftware.yvette.arbitrary;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;

public class CodesSupplier implements ArbitrarySupplier<String> {
    @Override
    public Arbitrary<String> get() {
        return Arbitraries
                .integers()
                .between(0, 9999)
                .map(i -> String.format("E%4d", i));
    }
}
