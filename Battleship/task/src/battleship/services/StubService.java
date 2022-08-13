package battleship.services;

import battleship.beans.Cell;
import battleship.beans.Ship;
import battleship.beans.ShipCategory;
import battleship.board.Board;

public class StubService {

    private final Board board;

    public StubService(Board board) {
        this.board = board;
    }

    public void placeShips() {
        board.addShip(new Ship(ShipCategory.CARRIER,CellsService.getCellRange(new Cell("F3"), new Cell("F7"))));
        board.addShip(new Ship(ShipCategory.BATTLESHIP,CellsService.getCellRange(new Cell("A1"), new Cell("D1"))));
        board.addShip(new Ship(ShipCategory.SUBMARINE,CellsService.getCellRange(new Cell("J8"), new Cell("J10"))));
        board.addShip(new Ship(ShipCategory.CRUISER,CellsService.getCellRange(new Cell("B9"), new Cell("D9"))));
        board.addShip(new Ship(ShipCategory.DESTROYER,CellsService.getCellRange(new Cell("I2"), new Cell("J2"))));
    }
}
