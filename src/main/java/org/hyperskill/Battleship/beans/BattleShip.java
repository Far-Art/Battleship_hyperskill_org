package org.hyperskill.Battleship.beans;

import org.hyperskill.Battleship.config.GameConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.InputMismatchException;
import java.util.List;

public class BattleShip implements Ship {
    private final String hit;
    private final String ship;
    private final ShipType category;
    private final List<Cell> locationCells;


    @Autowired
    public BattleShip(GameConfig config, ShipType category, List<Cell> locationCells) {
        this.hit = config.getSymbolHit();
        this.ship = config.getSymbolShip();
        this.category = category;
        this.locationCells = locationCells;
        if (locationCells != null) {
            this.locationCells.forEach(c -> c.setSymbol(ship));
        }
    }

    @Override
    public List<Cell> getLocationCells() {
        return locationCells;
    }

    @Override
    public ShipType getCategory() {
        return category;
    }

    @Override
    public boolean isDestroyed() {
        return locationCells.stream().allMatch(c -> hit.equals(c.getSymbol()));
    }

    @Override
    public void hit(Cell cell) throws InputMismatchException {
        Cell hit = this.locationCells.stream().filter(c -> c.equals(cell)).findFirst().orElseThrow(() -> new InputMismatchException(String.format("Cell %s%s not found", cell.getRow(), cell.getColumn())));
        hit.setSymbol(this.hit);
    }
}
