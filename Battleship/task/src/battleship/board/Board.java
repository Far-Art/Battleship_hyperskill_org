package battleship.board;

import battleship.beans.Cell;
import battleship.beans.PlayableShip;
import battleship.beans.ShipCategory;
import battleship.beans.ShipsVisibility;
import battleship.services.CellsService;
import battleship.services.ShipPlacementService;
import battleship.services.ShootingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Board {

    public static final String shipCell = "O";
    public static final String hitCell = "X";
    public static final String missCell = "M";
    public static final String userInputPlaceholder = "> ";
    private final String fogOfWar = "~";
    private final List<ShipCategory> gameShipPattern = List.of(ShipCategory.CARRIER, ShipCategory.BATTLESHIP, ShipCategory.SUBMARINE, ShipCategory.CRUISER, ShipCategory.DESTROYER);
    private final int rows = 10;
    private final int columns = 10;
    private final ShipPlacementService placementService;

    private final CellsService cellsService;

    private final ShootingService shootingService;
    private final String startingColumnSymbol = "1";
    private final String startingRowSymbol = "A";
    private final List<Set<Cell>> field = new ArrayList<>(rows);

    private List<Cell> shipLocations;

    public Board() {
        initBoard();
        this.cellsService = new CellsService(getFirstCell(), getLastCell());
        this.placementService = new ShipPlacementService(this, cellsService);
        this.shootingService = new ShootingService(this);
        this.shipLocations = placementService.getShipLocations();
    }

    public List<ShipCategory> getGameShipPattern() {
        return gameShipPattern;
    }

    public ShootingService getShootingService() {
        return shootingService;
    }

    public List<Set<Cell>> getField() {
        return field;
    }

    private void initBoard() {
        for (int i = 0; i < rows; i++) {
            Set<Cell> row = new TreeSet<>();
            int rowSymbol = CellsService.stringToCharCode(startingRowSymbol) + i;
            for (int j = 0; j < columns; j++) {
                int colSymbol = CellsService.stringToCharCode(startingColumnSymbol) + j;
                row.add(new Cell(CellsService.charCodeToString(rowSymbol), String.valueOf(colSymbol), fogOfWar));
            }
            field.add(row);
        }
    }

    public void addShip(PlayableShip ship) {
        this.shipLocations = placementService.addShipToField(ship.getCategory(), ship.getLocationCells());
        print(ShipsVisibility.VISIBLE);
    }

    public void print(ShipsVisibility visibility) {
        StringBuilder builder = new StringBuilder();
        builder.append(getFieldRowAsString(field.get(0), true, visibility));
        builder.append(System.lineSeparator());
        for (int i = 0; i < field.size(); i++) {
            builder.append(getFieldRowAsString(field.get(i), false, visibility));
            if (i + 1 < field.size()) builder.append(System.lineSeparator());
        }
        System.out.println(builder);
    }


    public Cell getFirstCell() {
        if (field.size() > 0) {
            Set<Cell> firstRow = field.get(0);
            return firstRow.toArray(new Cell[0])[0];
        }
        return null;
    }

    public Cell getLastCell() {
        if (field.size() > 0) {
            Set<Cell> lastRow = field.get(field.size() - 1);
            return lastRow.toArray(new Cell[0])[lastRow.size() - 1];
        }
        return null;
    }

    public ShipPlacementService getPlacementService() {
        return this.placementService;
    }


    private String getFieldRowAsString(Set<Cell> row, boolean isFirst, ShipsVisibility visibility) {
        StringBuilder builder = new StringBuilder(isFirst ? "  " : row.stream().findFirst().get().getRow() + " ");
        if (isFirst) builder.append(row.stream().map(Cell::getColumn).collect(Collectors.joining(" ")));
        else
            builder.append(row.stream().map(c -> c.getSymbol().equals(shipCell) && visibility == ShipsVisibility.HIDDEN ? fogOfWar : c.getSymbol()).collect(Collectors.joining(" ")));
        return builder.toString();
    }
}
