package org.hyperskill.Battleship.beans;

import org.hyperskill.Battleship.components.ShipPlacementComponent;

public class Player {
    private final Board board;

    private final ShipPlacementComponent placement;

    private final int playerId;

    private String name;

    private boolean isLost = false;

    public Player(int playerId, String name, Board board, ShipPlacementComponent placement) {
        this.playerId = playerId;
        this.name = name;
        this.board = board;
        this.placement = placement;
        this.placement.setBoard(board);
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public ShipPlacementComponent placement() {
        return placement;
    }

    @Override
    public String toString() {
        return "Player{" + "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", isLost=" + isLost +
                '}';
    }
}
