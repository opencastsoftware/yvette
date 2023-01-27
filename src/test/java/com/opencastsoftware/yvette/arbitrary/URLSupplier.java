package com.opencastsoftware.yvette.arbitrary;

import java.net.URI;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.web.api.Web;

public class URLSupplier implements ArbitrarySupplier<URI> {
    @Override
    public Arbitrary<URI> get() {
        return Web
                .webDomains()
                .map(domain -> URI.create("https://" + domain));
    }
}
