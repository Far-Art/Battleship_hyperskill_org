package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.beans.ShipType;
import org.hyperskill.Battleship.beans.Visibility;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BattleshipPlayerService implements PlayerService {

    private final int minPlayers;
    private final int maxPlayers;
    private final List<Player> players;
    private final ApplicationContext context;
    private int numOfLostPlayers;
    private int numOfPlayers;
    private Player currentTurnPlayer;

    private int currentTurnPlayerIndex;


    public BattleshipPlayerService(GameConfig config, ApplicationContext context) {
        this.minPlayers = config.getMinPlayers();
        this.maxPlayers = config.getMaxPlayers();
        this.context = context;
        this.players = new ArrayList<>(maxPlayers);
    }


    public Player getCurrentPlayer() {
        if (currentTurnPlayer == null)
            initCurrentPlayer();
        return currentTurnPlayer;
    }

    public Player getNextPlayer() {
        return players.get(getNextPlayerIndex());
    }

    @Override
    public Player advanceCurrentPlayer() {
        // TODO implement here console asking to press enter and pass the turn ? (with prompt overload?)
        currentTurnPlayerIndex = getNextPlayerIndex();
        currentTurnPlayer = players.get(currentTurnPlayerIndex);
        return currentTurnPlayer;
    }

    @Override
    public Player getPlayer(String name) {
        return players.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> new InputMismatchException(String.format("Player %s not found", name)));
    }

    public int getNumOfLostPlayers() {
        return numOfLostPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) throws InputMismatchException {
        if (numOfPlayers > maxPlayers || numOfPlayers < minPlayers)
            throw new InputMismatchException(String.format("Number of players must be between %s and %s", minPlayers, maxPlayers));
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public List<Player> getOpponents() {
        return players.stream().filter(p -> p != currentTurnPlayer).collect(toList());
    }

    public void initPlayers() {
        for (int i = 0; i < numOfPlayers; i++) {
            Player player = context.getBean(Player.class);
            players.add(player);
        }
    }

    public void initShipsForPlayer(Player player) {
        System.out.printf("\n%s place your ships%n", player.getName());
        Board board = player.getBoard();
        List<ShipType> shipTypes = board.getGameShipPattern();
        board.print(Visibility.VISIBLE);
        for (ShipType shipType : shipTypes) {
            player.placement().addShipToBoard(shipType);
            board.print(Visibility.VISIBLE);
        }
        System.out.printf("%s you all set%n", player.getName());
    }

    @Override
    public void changeName(Player player, String newName) {
        if (!newName.isBlank() && getOpponents().stream().anyMatch(p -> p.getName().equalsIgnoreCase(newName)))
            throw new InputMismatchException(String.format("Name [%s] already taken", newName));

        String oldName = player.getName();
        if (newName.isBlank() || oldName.equalsIgnoreCase(newName)) {
            System.out.printf("* %s name left unchanged%n", oldName);
        } else {
            player.setName(newName);
            System.out.printf("* %s name changed to %s%n", oldName, newName);
        }
    }

    private int getNextPlayerIndex() {
        int nextPlayerIndex = currentTurnPlayerIndex + 1;
        if (nextPlayerIndex == numOfPlayers) {
            nextPlayerIndex = 0;
        }
        return nextPlayerIndex;
    }

    private void initCurrentPlayer() {
        currentTurnPlayerIndex = 0;
        currentTurnPlayer = players.get(currentTurnPlayerIndex);
    }
}
