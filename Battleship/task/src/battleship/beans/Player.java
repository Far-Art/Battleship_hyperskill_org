package battleship.beans;

import battleship.board.Board;
import battleship.services.ShootingService;

public class Player {

    private final String playerName;

    private final Board playerBoard;

    private Player opponent;

    private boolean isLost = false;

    private ShootingService shootingService;

    public Player(String playerName, Board playerBoard) {
        this.playerName = playerName;
        this.playerBoard = playerBoard;
        this.shootingService = playerBoard.getShootingService();
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ShootingService getShootingService() {
        return shootingService;
    }

    public void setShootingService(ShootingService shootingService) {
        this.shootingService = shootingService;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public void takeTurn() {
        if (this.shootingService == null) {
            throw new NullPointerException("ShootingService is not set");
        }
        this.shootingService.shoot();
    }

    public boolean isLost() {
        return this.getPlayerBoard().getPlacementService().getShipsOnField().stream().allMatch(PlayableShip::isDestroyed);
    }

    public void printBoards() {
        this.opponent.getPlayerBoard().print(ShipsVisibility.HIDDEN);
        System.out.println("---------------------");
        this.getPlayerBoard().print(ShipsVisibility.VISIBLE);
    }

}
