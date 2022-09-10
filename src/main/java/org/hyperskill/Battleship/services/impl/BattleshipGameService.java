package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.GameService;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BattleshipGameService implements GameService {

    private final String inputPlaceholder;

    private final String separator;

    private final UserInputService inputService;

    private final int minPlayers;

    private final int maxPlayers;

    private final PlayerService playerService;

    private Player currentPlayer;

    @Autowired
    public BattleshipGameService(GameConfig config, UserInputService inputService, PlayerService playerService) {
        this.inputService = inputService;
        this.playerService = playerService;
        this.inputPlaceholder = UserInputService.userInputPlaceholder;
        this.separator = UserInputService.lineSeparator;
        this.minPlayers = config.getMinPlayers();
        this.maxPlayers = config.getMaxPlayers();
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
            name = name.isBlank() ? currentPlayer.getName().strip() : name.strip();
            boolean nameChanged = !name.equals(currentPlayer.getName());
            System.out.println(String.format("* %s name %s %s", currentPlayer.getName(), nameChanged ? "set to" : "unchanged", nameChanged ? name : ""));
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

    @Override
    public void play() {
        boolean isFinished = true;
        while (!isFinished) {
            // TODO implement game
        }
        System.out.println("game finished");
    }
}
