package com.opencastsoftware.yvette;

public class Arithmetic {
    private Arithmetic() {}

    public static int unsignedSaturatingSub(int l, int r) {
        return r > l ? 0 : l - r;
    }
}
