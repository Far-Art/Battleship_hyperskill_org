package org.hyperskill.Battleship.beans;

import java.util.List;

public interface Ship {
    List<Cell> getLocationCells();

    ShipType getCategory();

    boolean isDestroyed();

    void hit(Cell cell);
}
