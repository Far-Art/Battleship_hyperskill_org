package org.hyperskill.Battleship.components;

import org.hyperskill.Battleship.beans.*;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.impl.UserInputService;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.CollectionUtils.containsAny;

@Component
@Scope("prototype")
public class ShipPlacementComponent {

    private final GameConfig config;
    private final UserInputService inputService;

    private final String shipSymbol;

    private final List<Cell> blockedCells = new ArrayList<>();

    private final List<Cell> shipLocations = new ArrayList<>();

    private final List<Ship> shipsOnField = new ArrayList<>();

    private final CellService cellService;

    private Board board;

    // TODO  find a way to provide board from containing element
    public ShipPlacementComponent(GameConfig config, UserInputService inputService, CellService cellService) {
        this.config = config;
        this.inputService = inputService;
        this.cellService = cellService;
        this.shipSymbol = config.getSymbolShip();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Ship> getShipsOnField() {
        return shipsOnField;
    }

    public List<Cell> getShipLocations() {
        return shipLocations;
    }

    public List<Cell> getBlockedCellsList() {
        return blockedCells;
    }

    public void addShipToBoard(ShipType shipType) {
        addShipToBoard(shipType, null);
    }

    public void addShipToBoard(ShipType shipType, List<Cell> location) {
        if (location != null) {
            addValidShip(new BattleShip(config, shipType, location));
        } else {
            // TODO implement retry service
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", shipType.getName(), shipType.getSize()/*, UserInputService.userInputPlaceholder*/);
            String userInput = inputService.getInput();
            boolean validShipLoc = false;
            while (!validShipLoc) {
                try {
                    String[] cellsFromInput = userInput != null && !userInput.isBlank() ? userInput.toUpperCase().split(" ") : null;
                    if (cellsFromInput != null) {
                        Cell start = cellService.stringToCell(cellsFromInput[0]);
                        Cell end = cellService.stringToCell(cellsFromInput[1]);
                        validShipLoc = validateShipLocation(start, end, shipType);
                        if (validShipLoc) {
                            List<Cell> range = cellService.getRange(start, end, board);
                            Ship ship = new BattleShip(config, shipType, range);
                            addValidShip(ship);
                        }
                    }
                } catch (Exception e) {
                    System.out.printf("Error! %s Try again: \n", e.getMessage()/*, UserInputService.userInputPlaceholder*/);
                    userInput = inputService.getInput();
                }
            }
        }
    }

    private void addValidShip(Ship ship) {
        blockCells(ship);
        ship.getLocationCells().forEach(c -> c.setSymbol(shipSymbol));
        shipLocations.addAll(ship.getLocationCells());
        shipsOnField.add(ship);
    }


    private boolean validateShipLocation(Cell first, Cell last, ShipType ship) throws Exception {
        List<Cell> shipLocation = getCellRange(first, last);
        if (shipLocation.size() != ship.getSize()) {
            throw new Exception(String.format("Wrong length of the %s!", ship.getName()));
        } else if (containsAny(blockedCells, shipLocation)) {
            throw new Exception("You placed it too close to another one.");
        }
        return true;
    }

    private void blockCells(Ship ship) {
        for (Cell current : ship.getLocationCells()) {
            Cell top = cellService.getAdjacent(current, board, Offset.TOP);
            Cell bottom = cellService.getAdjacent(current, board, Offset.BOTTOM);
            Cell left = cellService.getAdjacent(current, board, Offset.LEFT);
            Cell right = cellService.getAdjacent(current, board, Offset.RIGHT);
            blockedCells.addAll(Arrays.asList(current, top, bottom, left, right));
        }
    }

    private List<Cell> getCellRange(Cell first, Cell last) {
        return cellService.getRange(first, last, board);
    }

    public Ship getShipByCell(Cell cell) {
        return shipsOnField.stream().filter(s -> s.getLocationCells().contains(cell)).findFirst().orElse(null);
    }
}
