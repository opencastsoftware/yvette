package com.opencastsoftware.yvette;

public class LabelledRange extends Range {
    private final String label;

    public LabelledRange(String label, Position start, Position end) {
        super(start, end);
        this.label = label;
    }

    public LabelledRange(String label, Range range) {
        super(range.start(), range.end());
        this.label = label;
    }

    public String label() {
        return label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        LabelledRange other = (LabelledRange) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LabelledRange [label=" + label + ", start=" + start() + ", end=" + end() + "]";
    }
}
