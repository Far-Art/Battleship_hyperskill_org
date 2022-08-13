package battleship.beans;

import java.util.List;

public interface PlayableShip {
    List<Cell> getLocationCells();

    ShipCategory getCategory();

    boolean isDestroyed();

    void hit(Cell cell);
}
