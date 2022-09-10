package org.hyperskill.Battleship.services.interfaces;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.Cell;

import java.util.List;

public interface CellService {

    default int toCharCode(String str) {
        if (Character.isAlphabetic(str.charAt(0))) {
            return str.charAt(0);
        }
        return Integer.parseInt(str);
    }

    default String charToString(int code) {
        return String.valueOf((char) code);
    }

    Cell toCell(String cell);

    Cell getAdjacentCell(Cell cell, CellService.CellOffset position);

    List<List<Cell>> createBoardCells();

    List<Cell> getCellRangeFromBoard(Cell start, Cell end, Board board);

    public enum CellOffset {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
