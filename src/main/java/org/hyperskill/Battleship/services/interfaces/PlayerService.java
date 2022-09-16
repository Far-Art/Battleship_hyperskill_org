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

    Player advanceCurrentPlayer(boolean prompt);

    Player getPlayer(String name);

    int getNumOfLostPlayers();

    int getNumOfActivePlayers();

    int getInitialNumOfPlayers();

    void setInitialNumOfPlayers(int initialNumOfPlayers);

    List<Player> getActiveOpponents();

}
