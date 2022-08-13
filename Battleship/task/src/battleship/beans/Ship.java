package battleship.beans;

import battleship.board.Board;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;

public class Ship implements PlayableShip {
    private ShipCategory category;
    private List<Cell> locationCells;

    public Ship(ShipCategory category, List<Cell> locationCells) {
        this.category = category;
        this.locationCells = locationCells;
        if (locationCells != null) {
            this.locationCells.forEach(c -> c.setSymbol(Board.shipCell));
        }
    }

    @Override
    public List<Cell> getLocationCells() {
        return locationCells;
    }

    @Override
    public ShipCategory getCategory() {
        return category;
    }

    @Override
    public boolean isDestroyed() {
        return locationCells.stream().allMatch(c -> Board.hitCell.equals(c.getSymbol()));
    }

    @Override
    public void hit(Cell cell) {
        this.locationCells.stream().filter(c -> c.equals(cell)).findFirst().orElseThrow(() -> new InputMismatchException(String.format("Cell %s not found", cell))).setSymbol(Board.hitCell);
    }

}
