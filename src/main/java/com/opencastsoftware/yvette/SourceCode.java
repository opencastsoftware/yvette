package com.opencastsoftware.yvette;

import java.io.IOException;

public interface SourceCode {
    RangeContents readRange() throws IOException;
}
