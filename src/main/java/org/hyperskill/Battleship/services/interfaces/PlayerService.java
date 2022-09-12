package org.hyperskill.Battleship.services.interfaces;

import org.hyperskill.Battleship.beans.Player;

import java.util.List;

public interface PlayerService {

    void initPlayers();

    void initShipsForPlayer(Player player);

    void changeName(Player player, String newName);

    Player getCurrentPlayer();

    Player getNextPlayer();

    Player advanceCurrentPlayer();

    Player getPlayer(String name);

    int getNumOfLostPlayers();

    int getNumOfPlayers();

    void setNumOfPlayers(int numOfPlayers);

    List<Player> getOpponents();

}
