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

@Component
@Scope("prototype")
public class ShipPlacementComponent {

    private final GameConfig config;
    private final UserInputService inputService;

    private final String inputPlaceholder;

    private final String shipSymbol;

    private final List<Cell> blockedCells = new ArrayList<>();

    private final List<Cell> shipLocations = new ArrayList<>();

    private final List<Ship> shipsOnField = new ArrayList<>();

    private final CellService cellService;


    public ShipPlacementComponent(GameConfig config, UserInputService inputService, CellService cellService) {
        this.config = config;
        this.inputService = inputService;
        this.cellService = cellService;
        this.shipSymbol = config.getSymbolShip();
        this.inputPlaceholder = UserInputService.userInputPlaceholder;
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

    public void addShipToBoard(ShipType shipType, Board board) {
        addShipToBoard(shipType, board, null);
    }

    public void addShipToBoard(ShipType shipType, Board board, List<Cell> location) {
        if (location != null) {
            addValidShip(new BattleShip(config, shipType, location));
        } else {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n%s", shipType.getName(), shipType.getSize(), inputPlaceholder);
            String userInput = inputService.getInput();
            boolean validShipLoc = false;
            while (!validShipLoc) {
                try {
                    String[] cellsFromInput = userInput != null && !userInput.isBlank() ? userInput.split(" ") : null;
                    if (cellsFromInput != null) {
                        Cell start = cellService.toCell(cellsFromInput[0]);
                        Cell end = cellService.toCell(cellsFromInput[1]);
                        validShipLoc = validateShipLocation(start, end, shipType);
                        if (validShipLoc) {
                            List<Cell> range = cellService.getCellRangeFromBoard(start, end, board);
                            Ship ship = new BattleShip(config, shipType, range);
                            addValidShip(ship);
                        }
                    }
                } catch (Exception e) {
                    System.out.printf("Error! %s Try again: \n%s", e.getMessage(), inputPlaceholder);
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
//        List<Cell> shipLocation = getCellRange(first, last);
//        if (shipLocation.size() != ship.getSize()) {
//            throw new Exception(String.format("Wrong length of the %s!", ship.getName()));
//        } else if (containsAny(blockedCells, shipLocation)) {
//            throw new Exception("You placed it too close to another one.");
//        }
        return true;
    }

    private void blockCells(Ship ship) {
        for (Cell current : ship.getLocationCells()) {
            Cell top = cellService.getAdjacentCell(current, CellService.CellOffset.TOP);
            Cell bottom = cellService.getAdjacentCell(current, CellService.CellOffset.BOTTOM);
            Cell left = cellService.getAdjacentCell(current, CellService.CellOffset.LEFT);
            Cell right = cellService.getAdjacentCell(current, CellService.CellOffset.RIGHT);
            blockedCells.addAll(Arrays.asList(current, top, bottom, left, right));
        }
    }

    public Ship getShipByCell(Cell cell) {
        return shipsOnField.stream().filter(s -> s.getLocationCells().contains(cell)).findFirst().orElse(null);
    }
}
