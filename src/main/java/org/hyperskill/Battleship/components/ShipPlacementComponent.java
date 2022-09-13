package org.hyperskill.Battleship.components;

import org.hyperskill.Battleship.beans.*;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.impl.RetryService;
import org.hyperskill.Battleship.services.impl.UserInputService;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.util.CollectionUtils.containsAny;

@Component
@Scope("prototype")
public class ShipPlacementComponent {

    private final GameConfig config;

    private final RetryService retryService;
    private final UserInputService inputService;

    private final String shipSymbol;

    private final List<Cell> blockedCells = new ArrayList<>();

    private final List<Cell> shipLocations = new ArrayList<>();

    private final List<Ship> shipsOnField = new ArrayList<>();

    private final CellService cellService;

    private Board board;

    // TODO  find a way to provide board from containing element
    public ShipPlacementComponent(GameConfig config, UserInputService inputService, CellService cellService, RetryService retryService) {
        this.config = config;
        this.retryService = retryService;
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
            addShipToField(new BattleShip(config, shipType, location));
        } else {
            AtomicReference<Cell> start = new AtomicReference<>();
            AtomicReference<Cell> end = new AtomicReference<>();

            System.out.println(String.format("Enter the coordinates of the %s (%d cells)", shipType.getName(), shipType.getSize()));
            retryService.retryWhile(() -> {
                String input = inputService.getInput().toUpperCase();
                if (validateInput(input)) {
                    String[] cells = input.split(" ");
                    start.set(cellService.stringToCell(cells[0]));
                    end.set(cellService.stringToCell(cells[1]));
                }
                return validateShipLocation(start.get(), end.get(), shipType);
            }).then(s -> {
                List<Cell> range = cellService.getRange(start.get(), end.get(), board);
                Ship ship = new BattleShip(config, shipType, range);
                addShipToField(ship);
            });
        }
    }

    private void addShipToField(Ship ship) {
        blockCells(ship);
        ship.getLocationCells().forEach(c -> c.setSymbol(shipSymbol));
        shipLocations.addAll(ship.getLocationCells());
        shipsOnField.add(ship);
    }

    private boolean validateInput(String input) {
        if (input.isBlank()) {
            throw new InputMismatchException("Input cant be blank");
        }
        String[] arr = input.split(" ");
        if (arr.length != 2) {
            throw new InputMismatchException("You must provide 2 cell parameters");
        }
        return true;
    }

    private boolean validateShipLocation(Cell first, Cell last, ShipType ship) {
        if (first == null || last == null) {
            throw new InputMismatchException("One of cells not provided");
        }
        List<Cell> shipLocation = getCellRange(first, last);
        if (shipLocation.size() != ship.getSize()) {
            throw new InputMismatchException(String.format("Wrong length of the %s!", ship.getName()));
        } else if (containsAny(blockedCells, shipLocation)) {
            throw new InputMismatchException("You placed it too close to another one.");
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
