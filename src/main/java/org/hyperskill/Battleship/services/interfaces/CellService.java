package org.hyperskill.Battleship.services.interfaces;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.Cell;
import org.hyperskill.Battleship.beans.Offset;

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

    Cell stringToCell(String cell);

    String cellToString(Cell cell);

    Cell getAdjacent(Cell cell, Board board, Offset offset);

    List<List<Cell>> createBoardField();

    List<Cell> getRange(Cell start, Cell end, Board board);

}
