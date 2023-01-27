package com.opencastsoftware.yvette;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class URISourceCode implements SourceCode {
    private final URI uri;

    public URISourceCode(URI uri) {
        this.uri = uri;
    }

    @Override
    public URIRangeContents readRange(Range range, int linesBefore, int linesAfter) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(uri));
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
        return new URIRangeContents(uri, spannedRange, retainedLines);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
        URISourceCode other = (URISourceCode) obj;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "URISourceCode [uri=" + uri + "]";
    }
}
