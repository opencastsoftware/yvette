package com.opencastsoftware.yvette.handlers.graphical;

import java.util.function.UnaryOperator;

import org.fusesource.jansi.Ansi;

import com.opencastsoftware.yvette.LabelledRange;
import com.opencastsoftware.yvette.Line;
import com.opencastsoftware.yvette.Position;

public class StyledRange extends LabelledRange {
    private final UnaryOperator<Ansi> style;

    public StyledRange(UnaryOperator<Ansi> style, String label, Position start, Position end) {
        super(label, start, end);
        this.style = style;
    }

    public StyledRange(UnaryOperator<Ansi> style, LabelledRange range) {
        super(range.label(), range);
        this.style = style;
    }

    public UnaryOperator<Ansi> style() {
        return style;
    }

    public boolean spansOnly(Line line) {
        return start().line() == line.lineNumber() &&
                end().line() == line.lineNumber();
    }

    public boolean appliesTo(Line line) {
        return start().line() <= line.lineNumber() &&
            end().line() >= line.lineNumber();
    }

    public boolean beginsAfter(Line line) {
        return start().line() >= line.lineNumber();
    }

    public boolean finishesBefore(Line line) {
        return end().line() == line.lineNumber();
    }

    public boolean contains(Line line) {
        return start().line() < line.lineNumber() &&
            end().line() > line.lineNumber();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((style == null) ? 0 : style.hashCode());
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
        StyledRange other = (StyledRange) obj;
        if (style == null) {
            if (other.style != null)
                return false;
        } else if (!style.equals(other.style))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StyledRange[style=" + style + ", label=" + label() + ", start=" + start() + ", end=" + end() + "]";
    }
}
