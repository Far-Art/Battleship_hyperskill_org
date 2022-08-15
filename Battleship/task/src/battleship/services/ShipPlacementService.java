package battleship.services;

import battleship.beans.Cell;
import battleship.beans.PlayableShip;
import battleship.beans.Ship;
import battleship.beans.ShipCategory;
import battleship.board.Board;
import battleship.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import static battleship.services.CellsService.getCellRange;
import static battleship.services.CellsService.stringToCell;
import static battleship.utils.ListUtils.containsAny;

public class ShipPlacementService {

    private final List<Cell> field;

    private final List<Cell> blockedCells = new ArrayList<>();

    private final List<Cell> shipLocations = new ArrayList<>();

    private final List<PlayableShip> shipsOnField = new ArrayList<>();

    private final CellsService cellsService;

    private final UserInputService inputService;

    public ShipPlacementService(Board board, CellsService cellsService) {
        this.field = ListUtils.flatten(board.getField());
        this.cellsService = cellsService;
        this.inputService = new UserInputService();
    }

    public List<PlayableShip> getShipsOnField() {
        return shipsOnField;
    }

    public List<Cell> getShipLocations() {
        return shipLocations;
    }

    public List<Cell> getBlockedCellsList() {
        return blockedCells;
    }

    public List<Cell> addShipToField(ShipCategory shipCategory) {
        return addShipToField(shipCategory, null);
    }

    public List<Cell> addShipToField(ShipCategory shipCategory, List<Cell> location) {
        if (location != null) {
            addValidShip(new Ship(shipCategory, location));
        } else {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n%s", shipCategory.getName(), shipCategory.getSize(), Board.userInputPlaceholder);
            String userInput = inputService.getInput();
            boolean validShipLoc = false;
            while (!validShipLoc) {
                try {
                    String[] cellsFromInput = userInput != null && !userInput.isBlank() ? userInput.split(" ") : null;
                    if (cellsFromInput != null) {
                        Cell first = stringToCell(cellsFromInput[0]);
                        Cell last = stringToCell(cellsFromInput[1]);
                        validShipLoc = validateShipLocation(first, last, shipCategory);
                        if (validShipLoc) {
                            addValidShip(new Ship(shipCategory, getCellRange(first, last)));
                        }
                    }
                } catch (Exception e) {
                    System.out.printf("Error! %s Try again: \n%s", e.getMessage(), Board.userInputPlaceholder);
                    userInput = inputService.getInput();
                }
            }
        }
        return shipLocations;
    }

    private void addValidShip(PlayableShip ship) {
        blockCells(ship);
        ship.getLocationCells().forEach(c -> replaceSymbolInField(c, Board.shipCell));
        shipLocations.addAll(ship.getLocationCells());
        shipsOnField.add(ship);
    }


    private boolean validateShipLocation(Cell first, Cell last, ShipCategory ship) throws Exception {
        List<Cell> shipLocation = getCellRange(first, last);
        if (shipLocation.size() != ship.getSize()) {
            throw new Exception(String.format("Wrong length of the %s!", ship.getName()));
        } else if (containsAny(blockedCells, shipLocation)) {
            throw new Exception("You placed it too close to another one.");
        }
        return true;
    }

    private void blockCells(PlayableShip ship) {
        for (Cell current : ship.getLocationCells()) {
            Cell top = cellsService.getAdjacentCell(current, CellsService.CellOffset.TOP);
            Cell bottom = cellsService.getAdjacentCell(current, CellsService.CellOffset.BOTTOM);
            Cell left = cellsService.getAdjacentCell(current, CellsService.CellOffset.LEFT);
            Cell right = cellsService.getAdjacentCell(current, CellsService.CellOffset.RIGHT);
            blockedCells.addAll(Arrays.asList(current, top, bottom, left, right));
        }
    }


    public void replaceSymbolInField(Cell coordinate, String symbol) {
        getFieldCell(coordinate).setSymbol(symbol);
    }

    public Cell getFieldCell(Cell cell) {
        return field.stream().filter(c -> c.equals(cell)).findFirst().orElseThrow(() -> new InputMismatchException(String.format("Cell %s not found", cell)));
    }

    public PlayableShip getShipByCell(Cell cell) {
        return shipsOnField.stream().filter(s -> s.getLocationCells().contains(cell)).findFirst().orElse(null);
    }

}
