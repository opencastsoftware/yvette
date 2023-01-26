package com.opencastsoftware.yvette;

import java.io.IOException;

public interface SourceCode {
    RangeContents readRange(
        Range range,
        int linesBefore, int linesAfter) throws IOException;
}
