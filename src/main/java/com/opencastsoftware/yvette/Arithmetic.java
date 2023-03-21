/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

public class Arithmetic {
    private Arithmetic() {}

    public static int unsignedSaturatingSub(int l, int r) {
        return r > l ? 0 : l - r;
    }
}
