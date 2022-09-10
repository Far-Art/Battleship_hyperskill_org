package org.hyperskill.Battleship.services.interfaces;

public interface GameService {

    default void startGame() {
        initPlayers();
        initShips();
        play();
    }

    void initPlayers();

    void initShips();

    void play();

}
