package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.hyperskill.Battleship.services.interfaces.GameService;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BattleshipGameService implements GameService {

    private final GameConfig config;
    private final String inputPlaceholder;

    private final String separator;

    private final UserInputService inputService;

    private final int minPlayers;

    private final int maxPlayers;

    private final CellService cellService;

    private final PlayerService playerService;

    private Player currentPlayer;

//    private final ShipPlacementService placementService;

    @Autowired
    public BattleshipGameService(GameConfig config, UserInputService inputService, CellService cellService, PlayerService playerService) {
        this.config = config;
        this.inputService = inputService;
        this.cellService = cellService;
        this.playerService = playerService;
        this.inputPlaceholder = UserInputService.userInputPlaceholder;
        this.separator = UserInputService.lineSeparator;
        this.minPlayers = config.getMinPlayers();
        this.maxPlayers = config.getMaxPlayers();
    }

    @Override
    public void startGame() {
        initPlayers();
        initShips();
    }

    @Override
    public void initPlayers() {
        // set number of players
        while (true) {
            System.out.printf("\nEnter number of players between %s and %s\n%s", minPlayers, maxPlayers, inputPlaceholder);
            try {
                playerService.setNumOfPlayers(Integer.parseInt(inputService.getInput()));
                break;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        playerService.initPlayers();

        // change players name
        currentPlayer = playerService.getCurrentPlayer();
        for (int i = 0; i < playerService.getNumOfPlayers(); i++) {
            System.out.printf("\n%s enter your name\n%s", currentPlayer.getName(), inputPlaceholder);
            String name = inputService.getInput();
            name = name.strip().isBlank() ? currentPlayer.getName() : name;
            System.out.println(String.format("%s name set to %s", currentPlayer.getName(), name));
            currentPlayer.setName(name);
            currentPlayer = playerService.getNextPlayer();
        }
        System.out.println();
    }

    @Override
    public void initShips() {
        int numOfPlayers = playerService.getNumOfPlayers();
        System.out.println(separator);
        for (int i = 0; i < numOfPlayers; i++) {
            System.out.println(String.format("%s place your ships", currentPlayer.getName()));
            playerService.initShipsForPlayer(currentPlayer);
            currentPlayer = playerService.getNextPlayer();
        }
    }
}