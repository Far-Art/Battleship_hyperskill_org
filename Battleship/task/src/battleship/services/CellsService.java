package battleship.services;

import battleship.beans.Cell;

import java.util.*;

public class CellsService {

    private final Cell firstCell;
    private final Cell lastCell;

    public CellsService(Cell firstCell, Cell lastCell) {
        this.firstCell = firstCell;
        this.lastCell = lastCell;
    }

    public static int stringToCharCode(String str) {
        if (Character.isAlphabetic(str.charAt(0))) {
            return str.charAt(0);
        }
        return Integer.parseInt(str);
    }

    public static String charCodeToString(int code) {
        return String.valueOf((char) code);
    }

    public static Cell stringToCell(String cell) {
        return new Cell(cell.substring(0, 1), cell.substring(1));
    }

    public static List<Cell> getCellRange(Cell first, Cell last) {
        if (first.getRow().equals(last.getRow())) {
            return getHorizontalRange(first, last);
        } else if (first.getColumn().equals(last.getColumn())) {
            return getVerticalRange(first, last);
        }
        throw new InputMismatchException("Diagonal placement not allowed!");
    }

    private static List<Cell> getVerticalRange(Cell first, Cell last) {
        List<Cell> set = new ArrayList<>();
        int start = stringToCharCode(first.getRow());
        int end = stringToCharCode(last.getRow());
        int tempStart = start;
        start = Math.min(start, end);
        end = Math.max(tempStart, end);
        for (int i = start; i <= end; i++) {
            set.add(new Cell(charCodeToString(i), first.getColumn()));
        }
        return set;
    }

    private static List<Cell> getHorizontalRange(Cell first, Cell last) {
        List<Cell> set = new ArrayList<>();
        int start = stringToCharCode(first.getColumn());
        int end = stringToCharCode(last.getColumn());
        int tempStart = start;
        start = Math.min(start, end);
        end = Math.max(tempStart, end);
        for (int i = start; i <= end; i++) {
            set.add(new Cell(first.getRow(), String.valueOf(i)));
        }
        return set;
    }

    public Cell getAdjacentCell(Cell cell, CellOffset position) {
        switch (position) {
            case TOP:
                return getAdjacentTopCell(cell);
            case BOTTOM:
                return getAdjacentBottomCell(cell);
            case RIGHT:
                return getAdjacentRightCell(cell);
            case LEFT:
                return getAdjacentLeftCell(cell);
            default:
                return null;
        }
    }

    private Cell getAdjacentTopCell(Cell cell) {
        return firstCell.getRow().equals(cell.getRow()) ? cell : stringToCell(charCodeToString(stringToCharCode(cell.getRow()) - 1) + cell.getColumn());
    }

    private Cell getAdjacentBottomCell(Cell cell) {
        return lastCell.getRow().equals(cell.getRow()) ? cell : stringToCell(charCodeToString(stringToCharCode(cell.getRow()) + 1) + cell.getColumn());
    }

    private Cell getAdjacentRightCell(Cell cell) {
        return lastCell.getColumn().equals(cell.getColumn()) ? cell : stringToCell(cell.getRow() + (stringToCharCode(cell.getColumn()) + 1));
    }

    private Cell getAdjacentLeftCell(Cell cell) {
        return firstCell.getColumn().equals(cell.getColumn()) ? cell : stringToCell(cell.getRow() + (stringToCharCode(cell.getColumn()) - 1));
    }

    public enum CellOffset {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
