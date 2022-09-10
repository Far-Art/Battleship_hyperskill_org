package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.beans.ShipType;
import org.hyperskill.Battleship.components.ShipPlacementComponent;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@Service
public class BattleshipPlayerService implements PlayerService {

    private final GameConfig config;
    private final UserInputService inputService;
    private final ShipPlacementComponent shipPlacement;
    private final int minPlayers;
    private final int maxPlayers;
    private final int startingCountNum = 1;
    private final Map<Integer, Player> players;
    private final ApplicationContext context;
    private int numOfLostPlayers;
    private int numOfPlayers;
    private Player currentTurnPlayer;


    public BattleshipPlayerService(GameConfig config, UserInputService inputService, ShipPlacementComponent shipPlacement, ApplicationContext context) {
        this.config = config;
        this.inputService = inputService;
        this.shipPlacement = shipPlacement;
        this.minPlayers = config.getMinPlayers();
        this.maxPlayers = config.getMaxPlayers();
        this.context = context;
        this.players = new HashMap<>(maxPlayers);
    }


    public Player getCurrentPlayer() {
        return currentTurnPlayer;
    }

    public Player getNextPlayer() {
        int currentPlayerKey = players.entrySet().stream().filter(e -> e.getValue().equals(currentTurnPlayer)).findFirst().get().getKey();
        currentPlayerKey++;
        if (currentPlayerKey > numOfPlayers) {
            currentPlayerKey = startingCountNum;
        }
        Player player = players.get(currentPlayerKey);
        currentTurnPlayer = player;
        return player;
    }

    public int getNumOfLostPlayers() {
        return numOfLostPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) throws InputMismatchException {
        if (numOfPlayers > maxPlayers || numOfPlayers < minPlayers)
            throw new InputMismatchException(String.format("Number of players must be between %s and %s, you entered %s", minPlayers, maxPlayers, numOfPlayers));
        this.numOfPlayers = numOfPlayers;
    }

    public void initPlayers() {
        for (int i = 0; i < maxPlayers; i++) {
            int playerNum = startingCountNum + i;
            String initialName = "Player" + playerNum;
            Player player = context.getBean(Player.class);
            players.put(playerNum, player);
        }
        currentTurnPlayer = players.values().toArray(new Player[]{null})[0];
    }

    public void initShipsForPlayer(Player player) {
        Board board = player.getBoard();
        List<ShipType> shipTypes = board.getGameShipPattern();
        for (ShipType shipType : shipTypes) {
            shipPlacement.addShipToBoard(shipType, board);
        }
    }
}
