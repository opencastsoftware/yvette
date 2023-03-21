/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.arbitrary;

import com.opencastsoftware.yvette.LinkStyle;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;

public class LinkStyleSupplier implements ArbitrarySupplier<LinkStyle> {
    @Override
    public  Arbitrary<LinkStyle> get() {
        return Arbitraries.of(LinkStyle.class);
    }
}
