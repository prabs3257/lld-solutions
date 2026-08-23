package com.example.googledocs.model;

import java.util.Objects;

/**
 * Uniquely identifies a cell inside a document.
 * In a spreadsheet-like document, row + column is enough to locate a cell.
 */
public final class CellId implements Comparable<CellId> {

    private final int row;
    private final int column;

    public CellId(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row and column must be non-negative");
        }
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int compareTo(CellId other) {
        int rowComparison = Integer.compare(this.row, other.row);
        return rowComparison != 0
                ? rowComparison
                : Integer.compare(this.column, other.column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellId)) return false;
        CellId cellId = (CellId) o;
        return row == cellId.row && column == cellId.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "Cell(" + row + "," + column + ")";
    }
}
