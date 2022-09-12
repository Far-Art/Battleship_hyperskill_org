package org.hyperskill.Battleship.beans;

import org.hyperskill.Battleship.components.ShipPlacementComponent;

public class Player {
    private final Board board;

    private final ShipPlacementComponent placement;

    private int playerId;

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

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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

    public ShipPlacementComponent placement() {
        return placement;
    }
}
