package org.hyperskill.Battleship.beans;

import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.CellService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class PlayerBoard implements Board {

    private final CellService cellService;

    private final String shipCell;
    private final String fogOfWar;
    private final List<ShipType> gameShipPattern;
    private final List<List<Cell>> field;


    public PlayerBoard(GameConfig config, CellService cellService) {
        this.cellService = cellService;
        this.field = cellService.createBoardField();
        this.fogOfWar = config.getSymbolSea();
        this.shipCell = config.getSymbolShip();
        this.gameShipPattern = config.getShipPattern();
    }

    public List<ShipType> getGameShipPattern() {
        return gameShipPattern;
    }

    @Override
    public Cell getCell(String cell) {
        List<Cell> cells = getCells(cell);
        return cells.size() > 0 ? cells.get(0) : null;
    }

    @Override
    public List<Cell> getCells(String... cells) {
        // TODO can be optimized?
        List<Cell> flatField = field.stream().flatMap(Collection::stream).collect(toList());
        List<Cell> found = new ArrayList<>();
        for (String cell : cells) {
            found.addAll(flatField.stream().filter(c -> cellService.cellToString(c).equals(cell)).collect(toList()));
        }
        return found;
    }


    public List<List<Cell>> getField() {
        return field;
    }

    public void print(Visibility visibility) {
        StringBuilder builder = new StringBuilder();
        builder.append(getFieldRowAsString(field.get(0), true, visibility));
        builder.append(System.lineSeparator());
        for (int i = 0; i < field.size(); i++) {
            builder.append(getFieldRowAsString(field.get(i), false, visibility));
            if (i + 1 < field.size()) builder.append(System.lineSeparator());
        }
        System.out.println(builder);
    }

    private String getFieldRowAsString(List<Cell> row, boolean isFirst, Visibility visibility) {
        StringBuilder builder = new StringBuilder(isFirst ? "  " : row.stream().findFirst().get().getRow() + " ");
        if (isFirst) builder.append(row.stream().map(Cell::getColumn).collect(joining(" ")));
        else
            builder.append(row.stream().map(c -> c.getSymbol().equals(shipCell) && visibility == Visibility.HIDDEN ? fogOfWar : c.getSymbol()).collect(joining(" ")));
        return builder.toString();
    }
}
