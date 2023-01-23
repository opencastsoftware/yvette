package com.opencastsoftware.yvette;

public class Range {
    private Position start;
    private Position end;

    public Range(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position start() {
        return start;
    }
    public Position end() {
        return end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
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
        Range other = (Range) obj;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Range [start=" + start + ", end=" + end + "]";
    }
}
