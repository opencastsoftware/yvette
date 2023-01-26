package com.opencastsoftware.yvette;

class Arithmetic {
    static int unsignedSaturatingSub(int l, int r) {
        return r > l ? 0 : l - r;
    }
}
