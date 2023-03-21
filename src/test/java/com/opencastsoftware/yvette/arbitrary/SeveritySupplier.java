/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.arbitrary;

import com.opencastsoftware.yvette.Severity;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;

public class SeveritySupplier implements ArbitrarySupplier<Severity> {
    @Override
    public Arbitrary<Severity> get() {
        return Arbitraries.of(Severity.class);
    }
}
