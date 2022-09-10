package org.hyperskill.Battleship.services.interfaces;

public interface GameService {

    default void startGame() {
        initPlayers();
        initShips();
    }

    void initPlayers();

    void initShips();


}
