package battleship;

import battleship.beans.ShipsVisibility;
import battleship.board.Board;
import battleship.services.StubService;

public class Main {

    private Board board = new Board();

    private StubService stub = new StubService(board);

    public static void main(String[] args) {
        Main main = new Main();
        main.play();
    }


    private void play() {
//        initShipsByStubService();
        initShipsByUser();
        board.printGameStartText();
        board.print(ShipsVisibility.HIDDEN);
        while (!board.gameIsFinished()) {
            board.takeShot();
        }
        board.printWinText();
    }

    private void initShipsByUser() {
        board.getGameShipPattern().forEach(board::addShip);
    }

    private void initShipsByStubService() {
        stub.placeShips();
    }
}
