package com.opencastsoftware.yvette;

import java.util.List;

public interface RangeContents {
    String name();
    Range range();
    List<Line> lines();
}
