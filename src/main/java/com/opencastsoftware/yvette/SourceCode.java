/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.yvette;

import java.io.IOException;

public interface SourceCode {
    RangeContents readRange(
        Range range,
        int linesBefore, int linesAfter) throws IOException;
}
