package org.hyperskill.Battleship.beans;

public class Player {
    private final Board board;

    private int playerId;

    private String name;

    private boolean isLost = false;

    public Player(int playerId, String name, Board board) {
        this.playerId = playerId;
        this.name = name;
        this.board = board;
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
}
