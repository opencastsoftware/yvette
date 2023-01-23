package com.opencastsoftware.yvette;

public class Position {
    private int line;
    private int character;

    public Position(int line, int character) {
        this.line = line;
        this.character = character;
    }

    public int line() {
        return line;
    }

    public int character() {
        return character;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + line;
        result = prime * result + character;
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
        Position other = (Position) obj;
        if (line != other.line)
            return false;
        if (character != other.character)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Position [line=" + line + ", character=" + character + "]";
    }
}
