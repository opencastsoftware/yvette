package com.opencastsoftware.yvette;

import java.net.URI;
import java.util.List;

public class URIRangeContents implements RangeContents {
    private final URI uri;
    private final Range range;
    private final List<Line> lines;

    public URIRangeContents(URI uri, Range range, List<Line> lines) {
        this.uri = uri;
        this.range = range;
        this.lines = lines;
    }

    @Override
    public String name() {
        return uri.toString();
    }

    public URI uri() {
        return uri;
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
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
        URIRangeContents other = (URIRangeContents) obj;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
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
        return "URIRangeContents[uri=" + uri + ", range=" + range + ", lines=" + lines + "]";
    }
}
