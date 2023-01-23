package com.opencastsoftware.yvette;

public class LabelledRange {
    private String label;
    private Range range;

    public LabelledRange(String label, Range range) {
        this.label = label;
        this.range = range;
    }

    public String label() {
        return label;
    }

    public Range range() {
        return range;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((range == null) ? 0 : range.hashCode());
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
        LabelledRange other = (LabelledRange) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (range == null) {
            if (other.range != null)
                return false;
        } else if (!range.equals(other.range))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LabeledRange [label=" + label + ", range=" + range + "]";
    }
}
