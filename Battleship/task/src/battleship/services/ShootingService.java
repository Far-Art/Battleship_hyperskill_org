package battleship.services;

import battleship.beans.Cell;
import battleship.beans.PlayableShip;
import battleship.board.Board;
import battleship.utils.ListUtils;

import java.util.InputMismatchException;
import java.util.List;

public class ShootingService {

    private final ShipPlacementService placementService;

    private final UserInputService inputService;

    private final List<Cell> field;

    public ShootingService(Board board) {
        this.field = ListUtils.flatten(board.getField());
        this.placementService = board.getPlacementService();
        this.inputService = new UserInputService();
    }

    public void shoot() {
        boolean validShot = false;
        System.out.printf("Take a shot!\n%s", Board.userInputPlaceholder);
        while (!validShot) {
            try {
                String input = inputService.getInput();
                Cell cell = CellsService.stringToCell(input);
                validShot = processShot(cell);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean isCellExist(Cell cell) {
        return field.contains(cell);
    }

    private boolean processShot(Cell cell) throws InputMismatchException {
        if (!isCellExist(cell)) {
            throw new InputMismatchException("Error! You entered the wrong coordinates! Try again");
        }
        Cell fieldCell = placementService.getFieldCell(cell);
        boolean shipHit = isShipLocation(fieldCell);
        placementService.replaceSymbolInField(cell, shipHit ? Board.hitCell : Board.missCell);
        PlayableShip ship = placementService.getShipByCell(cell);
        if (shipHit) {
            ship.hit(cell);
        }
        String message;
        if (shipHit && ship.isDestroyed()) {
            message = "You sank a ship!";
        } else if (shipHit) {
            message = "You hit a ship!";
        } else {
            message = "You missed";
        }
        System.out.printf("%s", message);
        return true;
    }

    private boolean isShipLocation(Cell cell) {
        return placementService.getShipLocations().contains(cell);
    }

}
