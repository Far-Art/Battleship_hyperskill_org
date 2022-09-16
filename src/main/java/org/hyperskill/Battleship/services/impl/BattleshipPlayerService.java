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

    private final UserInputService inputService;

    private final int minPlayers;
    private final int maxPlayers;
    private final List<Player> players;
    private final ApplicationContext context;
    private int numOfLostPlayers;
    private int initialNumOfPlayers;
    private Player currentTurnPlayer;

    private int currentTurnPlayerIndex;


    public BattleshipPlayerService(UserInputService inputService, GameConfig config, ApplicationContext context) {
        this.inputService = inputService;
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
        // TODO rework this logic of getting next player
        String playerName = currentTurnPlayer.getName();
        Player player = players.get(getNextPlayerIndex());
        while (player.isLost() && getNumOfActivePlayers() > 1 && !currentTurnPlayer.getName().equals(playerName)) {
            player = advanceCurrentPlayer(false);
        }
        return player;
    }

    @Override
    public Player advanceCurrentPlayer() {
        return advanceCurrentPlayer(true);
    }

    @Override
    public Player advanceCurrentPlayer(boolean prompt) {
        updatePlayersStatus();
        if (prompt) {
            System.out.printf("Press enter to pass the turn to %s%n", getNextPlayer().getName());
            inputService.getInput();
        }
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

    @Override
    public int getNumOfActivePlayers() {
        return initialNumOfPlayers - numOfLostPlayers;
    }

    public int getInitialNumOfPlayers() {
        return initialNumOfPlayers;
    }

    public void setInitialNumOfPlayers(int initialNumOfPlayers) throws InputMismatchException {
        if (initialNumOfPlayers > maxPlayers || initialNumOfPlayers < minPlayers)
            throw new InputMismatchException(String.format("Number of players must be between %s and %s", minPlayers, maxPlayers));
        this.initialNumOfPlayers = initialNumOfPlayers;
    }

    @Override
    public List<Player> getActiveOpponents() {
        return players.stream().filter(p -> p != currentTurnPlayer && !p.isLost()).collect(toList());
    }

    public void initPlayers() {
        for (int i = 0; i < initialNumOfPlayers; i++) {
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
        if (!newName.isBlank() && getActiveOpponents().stream().anyMatch(p -> p.getName().equalsIgnoreCase(newName)))
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
        if (nextPlayerIndex == initialNumOfPlayers) {
            nextPlayerIndex = 0;
        }
        return nextPlayerIndex;
    }

    private void initCurrentPlayer() {
        currentTurnPlayerIndex = 0;
        currentTurnPlayer = players.get(currentTurnPlayerIndex);
    }

    private void updatePlayersStatus() {
        List<Player> lost = players.stream().filter(p -> p.placement().isAllShipsDestroyed()).toList();
        numOfLostPlayers = lost.size();
        lost.forEach(p -> p.setLost(true));
    }
}
