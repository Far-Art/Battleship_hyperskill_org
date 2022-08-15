package battleship.services;

import battleship.beans.Player;
import battleship.beans.ShipsVisibility;
import battleship.board.Board;

public class PlayerService {

    private final Player player1;

    private final Player player2;
    private final UserInputService inputService = new UserInputService();
    private Player currentTurnPlayer;
    private Player waitingPlayer;
    private boolean gameOver = false;

    public PlayerService(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentTurnPlayer = player1;
        this.waitingPlayer = player2;
        setPlayers();
    }

    public void startGame() {
        if (player1.getPlayerBoard().getPlacementService().getShipsOnField().isEmpty()) {
            printPlaceShipsText();
            initField(player1.getPlayerBoard());
        }
        switchPlayers();

        if (player2.getPlayerBoard().getPlacementService().getShipsOnField().isEmpty()) {
            printPlaceShipsText();
            initField(player2.getPlayerBoard());
        }
        switchPlayers();

        printGameStartText();
        takeTurns();
        printWinText();
    }

    public void takeTurns() {
        while (!gameOver) {
            currentTurnPlayer.printBoards();
            System.out.printf("\n%s, it's your turn:\n", currentTurnPlayer.getPlayerName());
            currentTurnPlayer.takeTurn();
            gameOver = waitingPlayer.isLost();
            switchPlayers();
        }
    }

    private void setPlayers() {
        this.currentTurnPlayer.setOpponent(this.waitingPlayer);
        this.waitingPlayer.setOpponent(this.currentTurnPlayer);
        ShootingService curr = currentTurnPlayer.getShootingService();
        ShootingService opp = waitingPlayer.getShootingService();
        currentTurnPlayer.setShootingService(opp);
        waitingPlayer.setShootingService(curr);
    }

    private void initField(Board board) {
        board.print(ShipsVisibility.VISIBLE);
        board.getGameShipPattern().forEach((s) -> {
            board.getPlacementService().addShipToField(s);
            board.print(ShipsVisibility.VISIBLE);
        });
    }

    private void switchPlayers() {
        if (!gameOver) {
            passTurn();
        }
        Player temp = this.currentTurnPlayer;
        this.currentTurnPlayer = this.waitingPlayer;
        this.waitingPlayer = temp;

    }

    private void passTurn() {
        System.out.printf("\nPress Enter and pass the move to %s\n", waitingPlayer.getPlayerName());
        String pressed = inputService.getInput();
        while (!pressed.equals("")) {
            pressed = inputService.getInput();
        }
    }

    private void printGameStartText() {
        System.out.println("The game starts!\n");
    }

    private void printWinText() {
        System.out.printf("%s, You sank the last ship of %s. You won. Congratulations!\n", waitingPlayer.getPlayerName(), currentTurnPlayer.getPlayerName());
    }

    private void printPlaceShipsText() {
        System.out.printf("%s, place your ships to the game field\n\n", currentTurnPlayer.getPlayerName());
    }

}
