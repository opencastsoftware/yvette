/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import java.util.List;

public interface RangeContents {
    String name();
    Range range();
    List<Line> lines();
}
