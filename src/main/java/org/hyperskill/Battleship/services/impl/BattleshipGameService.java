package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.GameService;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.joining;

@Service
public class BattleshipGameService implements GameService {

    private final UserInputService inputService;

    private final int minPlayers;

    private final int maxPlayers;

    private final PlayerService playerService;

    private Player currentPlayer;

    @Autowired
    public BattleshipGameService(GameConfig config, UserInputService inputService, PlayerService playerService) {
        this.inputService = inputService;
        this.playerService = playerService;
        this.minPlayers = config.getMinPlayers();
        this.maxPlayers = config.getMaxPlayers();
    }


    @Override
    public void initPlayers() {
        // set number of players
        while (true) {
            System.out.printf("\nEnter number of players between %s and %s\n", minPlayers, maxPlayers/*, inputPlaceholder*/);
            try {
                playerService.setNumOfPlayers(Integer.parseInt(inputService.getInput()));
                break;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        playerService.initPlayers();

        // change players name
        // TODO make names unique
        currentPlayer = playerService.getCurrentPlayer();
        for (int i = 0; i < playerService.getNumOfPlayers(); i++) {
            System.out.printf("\n%s enter your name\n", currentPlayer.getName()/*, */);
            String name = inputService.getInput();
            name = name.isBlank() ? currentPlayer.getName().strip() : name.strip();
            boolean nameChanged = !name.equals(currentPlayer.getName());
            System.out.println(String.format("* %s name %s %s", currentPlayer.getName(), nameChanged ? "set to" : "unchanged", nameChanged ? name : ""));
            currentPlayer.setName(name);
            currentPlayer = playerService.advanceCurrentPlayer();
        }
        System.out.println();
    }

    @Override
    public void initShips() {
        int numOfPlayers = playerService.getNumOfPlayers();
        for (int i = 0; i < numOfPlayers; i++) {
            playerService.initShipsForPlayer(currentPlayer);
            currentPlayer = playerService.advanceCurrentPlayer();
            if (i + 1 < numOfPlayers) {
                System.out.println(String.format("Press enter to pass the turn to %s", currentPlayer.getName()));
            } else {
                System.out.print("\nPress enter to start the game");
            }
            System.out.print(/*UserInputService.userInputPlaceholder + */inputService.getInput());
            System.out.println(UserInputService.verticalDots);
        }
    }

    @Override
    public void play() {
        System.out.println("\n\t\t *** THE BATTLESHIP GAME STARTS ***\n");
        boolean isFinished = true;
        int numOfPlayers = playerService.getNumOfPlayers();
        Player player = currentPlayer;
        Player opponent;
        if (numOfPlayers > 2) {
            System.out.println(String.format("%s choose your opponent [%s]", player.getName(), playerService.getOpponents().stream().map(Player::getName).collect(joining(","))));
            // TODO after implementing retry input service, place it here
            opponent = playerService.getPlayer(inputService.getInput());
        } else {
            opponent = playerService.getNextPlayer();
        }

        while (!isFinished) {
            // TODO implement game
        }
        System.out.println("game finished");
    }
}
