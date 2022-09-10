package org.hyperskill.Battleship.services.interfaces;

import org.hyperskill.Battleship.beans.Player;

public interface PlayerService {

    void initPlayers();

    void initShipsForPlayer(Player player);

    Player getCurrentPlayer();

    Player getNextPlayer();

    int getNumOfLostPlayers();

    int getNumOfPlayers();

    void setNumOfPlayers(int numOfPlayers);

}
