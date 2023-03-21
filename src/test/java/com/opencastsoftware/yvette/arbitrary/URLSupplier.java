/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette.arbitrary;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.web.api.Web;

import java.net.URI;

public class URLSupplier implements ArbitrarySupplier<URI> {
    @Override
    public Arbitrary<URI> get() {
        return Web
                .webDomains()
                .map(domain -> URI.create("https://" + domain));
    }
}
