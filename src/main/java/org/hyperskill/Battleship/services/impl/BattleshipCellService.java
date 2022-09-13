package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Axis;
import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.Cell;
import org.hyperskill.Battleship.beans.Offset;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.hyperskill.Battleship.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Service
public class BattleshipCellService implements CellService {

    private final Cell firstCell;
    private final Cell lastCell;
    private final int boardSize;
    private final String seaSymbol;


    public BattleshipCellService(GameConfig config) {
        this.boardSize = config.getBoarsSize();
        this.seaSymbol = config.getSymbolSea();
        this.firstCell = new Cell(config.getFirstRow(), config.getFirstColumn());
        String row = charToString(toCharCode(firstCell.getRow()) + boardSize);
        String column = String.valueOf(boardSize);
        this.lastCell = new Cell(row, column);
    }

    @Override
    public Cell stringToCell(String cell) {
        return new Cell(cell.substring(0, 1), cell.substring(1));
    }

    @Override
    public String cellToString(Cell cell) {
        return cell != null ? cell.getRow() + cell.getColumn() : null;
    }

    @Override
    public List<List<Cell>> createBoardField() {
        List<List<Cell>> boardCells = new ArrayList<>(boardSize);
        for (int i = 0; i < boardSize; i++) {
            String rowSymbol = charToString(toCharCode(firstCell.getRow()) + i);
            boardCells.add(createRow(rowSymbol));
        }
        return boardCells;
    }

    @Override
    public Cell getAdjacent(Cell cell, Board board, Offset offset) {
        switch (offset) {
            case TOP:
                return getAdjacentTopCell(cell, board);
            case BOTTOM:
                return getAdjacentBottomCell(cell, board);
            case RIGHT:
                return getAdjacentRightCell(cell, board);
            case LEFT:
                return getAdjacentLeftCell(cell, board);
            default:
                return null;
        }
    }

    @Override
    public List<Cell> getRange(Cell start, Cell end, Board board) {
        Axis axis = getAxis(start, end);

        if (start.compareTo(end) > 0) {
            Cell temp = start;
            start = end;
            end = temp;
        }

        Cell bStart = findBoardCell(start, board);
        Cell bEnd = findBoardCell(end, board);
        switch (axis) {
            case HORIZONTAL:
                return getHorizontalRange(bStart, bEnd, board);
            case VERTICAL:
                return getVerticalRange(bStart, bEnd, board);
            default:
                return null;
        }
    }


    private List<Cell> getVerticalRange(Cell start, Cell end, Board board) {
        List<Cell> list = new ArrayList<>();
        int length = getRangeLength(start, end);
        for (int i = 0; i < length; i++) {
            String toFind = String.format("%s%s", charToString(toCharCode(start.getRow()) + i), start.getColumn());
            Cell cell = findBoardCell(stringToCell(toFind), board);
            list.add(cell);
        }
        return list;
    }


    private List<Cell> getHorizontalRange(Cell start, Cell end, Board board) {
        List<Cell> list = new ArrayList<>();
        int length = getRangeLength(start, end);
        for (int i = 0; i < length; i++) {
            String toFind = String.format("%s%s", start.getRow(), Integer.parseInt(start.getColumn()) + i);
            Cell cell = findBoardCell(stringToCell(toFind), board);
            list.add(cell);
        }
        return list;
    }

    // TODO extract getting cell string to method with Offset bean
    private Cell getAdjacentTopCell(Cell cell, Board board) {
        return firstCell.getRow().equals(cell.getRow()) ? board.getCell(cellToString(cell)) : board.getCell(charToString(toCharCode(cell.getRow()) - 1) + cell.getColumn());
    }

    private Cell getAdjacentBottomCell(Cell cell, Board board) {
        return lastCell.getRow().equals(cell.getRow()) ? board.getCell(cellToString(cell)) : board.getCell(charToString(toCharCode(cell.getRow()) + 1) + cell.getColumn());
    }

    private Cell getAdjacentRightCell(Cell cell, Board board) {
        return lastCell.getColumn().equals(cell.getColumn()) ? board.getCell(cellToString(cell)) : board.getCell(cell.getRow() + (toCharCode(cell.getColumn()) + 1));
    }

    private Cell getAdjacentLeftCell(Cell cell, Board board) {
        return firstCell.getColumn().equals(cell.getColumn()) ? board.getCell(cellToString(cell)) : board.getCell(cell.getRow() + (toCharCode(cell.getColumn()) - 1));
    }

    private Cell findBoardCell(Cell cell, Board board) throws InputMismatchException {
        List<Cell> field = ListUtils.flatten(board.getField());
        return field.stream().filter(c -> c.getRow().equals(cell.getRow()) && c.getColumn().equals(cell.getColumn())).findFirst().orElseThrow(() -> new InputMismatchException("Cell not found"));
    }

    private List<Cell> createRow(String rowSymbol) {
        List<Cell> list = new ArrayList<>(boardSize);
        int start = toCharCode(firstCell.getColumn());
        int end = toCharCode(lastCell.getColumn());
        for (int i = start; i <= end; i++) {
            list.add(new Cell(rowSymbol, String.valueOf(i), seaSymbol));
        }
        return list;
    }

    private int getRangeLength(Cell start, Cell end) {
        Axis axis = getAxis(start, end);
        int startSymbol = 0;
        int endSymbol = 0;
        if (axis == Axis.VERTICAL) {
            startSymbol = toCharCode(start.getRow());
            endSymbol = toCharCode(end.getRow());
        } else if (axis == Axis.HORIZONTAL) {
            startSymbol = Integer.valueOf(start.getColumn());
            endSymbol = Integer.valueOf(end.getColumn());
        }
        // swap start and end if needed
        int temp = endSymbol;
        if (startSymbol > endSymbol) {
            endSymbol = startSymbol;
            startSymbol = temp;
        }
        return endSymbol - startSymbol + 1;
    }

    private Axis getAxis(Cell start, Cell end) {
        if (start.getRow().equals(end.getRow())) {
            return Axis.HORIZONTAL;
        } else if (start.getColumn().equals(end.getColumn())) {
            return Axis.VERTICAL;
        }
        throw new InputMismatchException("Diagonal placement not allowed!");
    }
}
