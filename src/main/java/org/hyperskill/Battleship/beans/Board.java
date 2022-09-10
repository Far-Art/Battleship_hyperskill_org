package org.hyperskill.Battleship.beans;

import java.util.List;

public interface Board {
    List<List<Cell>> getField();

    List<ShipType> getGameShipPattern();
}
