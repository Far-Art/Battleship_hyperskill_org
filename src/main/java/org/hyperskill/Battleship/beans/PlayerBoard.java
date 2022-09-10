package org.hyperskill.Battleship.beans;

import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class PlayerBoard implements Board {

    private final GameConfig config;
    private final String shipCell;
    private final String fogOfWar;
    private final List<ShipType> gameShipPattern;
    private final CellService cellService;

    private final String startingColumnSymbol = "1";
    private final String startingRowSymbol = "A";
    private final List<List<Cell>> field;

    private List<Cell> shipLocations;

    @Autowired
    public PlayerBoard(GameConfig config, CellService cellService) {
        this.config = config;
        this.cellService = cellService;
        this.field = cellService.createBoardCells();
        this.fogOfWar = config.getSymbolSea();
        this.shipCell = config.getSymbolShip();
        this.gameShipPattern = config.getShipPattern();
    }

    public List<ShipType> getGameShipPattern() {
        return gameShipPattern;
    }


    public List<List<Cell>> getField() {
        return field;
    }

    public void addShip(Ship ship) {
//        this.shipLocations = placementService.addShipToField(ship.getCategory(), ship.getLocationCells());
//        print(ShipVisibility.VISIBLE);
    }

    public void print(ShipVisibility visibility) {
        StringBuilder builder = new StringBuilder();
        builder.append(getFieldRowAsString(field.get(0), true, visibility));
        builder.append(System.lineSeparator());
        for (int i = 0; i < field.size(); i++) {
            builder.append(getFieldRowAsString(field.get(i), false, visibility));
            if (i + 1 < field.size()) builder.append(System.lineSeparator());
        }
        System.out.println(builder);
    }

    private String getFieldRowAsString(List<Cell> row, boolean isFirst, ShipVisibility visibility) {
        StringBuilder builder = new StringBuilder(isFirst ? "  " : row.stream().findFirst().get().getRow() + " ");
        if (isFirst) builder.append(row.stream().map(Cell::getColumn).collect(joining(" ")));
        else
            builder.append(row.stream().map(c -> c.getSymbol().equals(shipCell) && visibility == ShipVisibility.HIDDEN ? fogOfWar : c.getSymbol()).collect(joining(" ")));
        return builder.toString();
    }
}
