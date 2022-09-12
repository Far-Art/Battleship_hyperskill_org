package org.hyperskill.Battleship.beans;

import java.util.List;

public interface Board {
    List<List<Cell>> getField();

    void print(Visibility visibility);

    List<ShipType> getGameShipPattern();

    Cell getCell(String cell);

    List<Cell> getCells(String... cells);

}
