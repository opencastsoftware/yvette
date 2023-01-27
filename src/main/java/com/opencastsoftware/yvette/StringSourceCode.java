package com.opencastsoftware.yvette;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringSourceCode implements SourceCode {
    private final String name;
    private final String source;

    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

    public StringSourceCode(String name, String source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public StringRangeContents readRange(Range range, int linesBefore, int linesAfter) {
        if (source.isEmpty()) {
            return new StringRangeContents(name, range, Collections.emptyList());
        } else {
            List<String> lines = Arrays.asList(NEWLINE_PATTERN.split(source));
            int startLine = Arithmetic.unsignedSaturatingSub(range.start().line(), linesBefore);
            int endLine = Math.min(range.end().line() + linesAfter, lines.size() - 1);
            int endChar = Math.max(0, lines.get(endLine).length() - 1);
            int spannedLines = endLine - startLine + 1;
            Range spannedRange = new Range(new Position(startLine, 0), new Position(endLine, endChar));
            List<Line> retainedLines = IntStream.range(0, lines.size())
                    .skip(startLine)
                    .limit(spannedLines)
                    .mapToObj(lineNumber -> new Line(lineNumber, lines.get(lineNumber)))
                    .collect(Collectors.toList());
            return new StringRangeContents(name, spannedRange, retainedLines);
        }
    }

    public String name() {
        return name;
    }

    public String source() {
        return source;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        StringSourceCode other = (StringSourceCode) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StringSourceCode [name=" + name + ", source=" + source + "]";
    }
}
