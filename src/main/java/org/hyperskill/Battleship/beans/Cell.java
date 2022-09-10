package org.hyperskill.Battleship.beans;

import java.util.StringJoiner;

public class Cell implements Comparable<Cell> {
    private String row;
    private String column;
    private String symbol;

    public Cell(String row, String column) {
        this.row = row;
        this.column = column;
    }

    public Cell(String cell) {
        this.row = cell.substring(0, 1);
        this.column = cell.substring(1);
    }

    public Cell(String row, String column, String symbol) {
        this.row = row;
        this.column = column;
        this.symbol = symbol;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (!row.equals(cell.row)) return false;
        return column.equals(cell.column);
    }

    @Override
    public int hashCode() {
        int result = row.hashCode();
        result = 31 * result + column.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cell.class.getSimpleName() + "[", "]")
                .add("cell=" + row + column)
                .add("symbol=" + symbol)
                .toString();
    }

    @Override
    public int compareTo(Cell o) {
        return Integer.parseInt(this.getColumn()) - Integer.parseInt(o.getColumn()) + this.getRow().compareTo(o.getRow());
    }
}
