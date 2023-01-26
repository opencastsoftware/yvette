package com.opencastsoftware.yvette;

import java.util.List;

public class StringRangeContents implements RangeContents {
    private final String name;
    private final Range range;
    private final List<Line> lines;

    public StringRangeContents(String name, Range range, List<Line> lines) {
        this.name = name;
        this.range = range;
        this.lines = lines;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Range range() {
        return range;
    }

    @Override
    public List<Line> lines() {
        return lines;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((range == null) ? 0 : range.hashCode());
        result = prime * result + ((lines == null) ? 0 : lines.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringRangeContents other = (StringRangeContents) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (range == null) {
            if (other.range != null)
                return false;
        } else if (!range.equals(other.range))
            return false;
        if (lines == null) {
            if (other.lines != null)
                return false;
        } else if (!lines.equals(other.lines))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StringRangeContents [name=" + name + ", range=" + range + ", lines=" + lines + "]";
    }
}
