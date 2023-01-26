package com.opencastsoftware.yvette;

public class Line {
    private final int lineNumber;
    private final String text;

    public Line(int lineNumber, String text) {
        this.lineNumber = lineNumber;
        this.text = text;
    }

    public int lineNumber() {
        return lineNumber;
    }

    public String text() {
        return text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + lineNumber;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        Line other = (Line) obj;
        if (lineNumber != other.lineNumber)
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Line [lineNumber=" + lineNumber + ", text=" + text + "]";
    }
}
