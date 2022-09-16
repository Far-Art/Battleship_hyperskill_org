package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.GameService;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.hyperskill.Battleship.services.interfaces.ShootingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.joining;

@Service
public class BattleshipGameService implements GameService {

    private final UserInputService inputService;

    private final RetryService retryService;

    private final ShootingService shootingService;

    private final int minPlayers;

    private final int maxPlayers;

    private final PlayerService playerService;

    private Player currentPlayer;

    @Autowired
    public BattleshipGameService(GameConfig config, UserInputService inputService, PlayerService playerService, RetryService retryService, ShootingService shootingService) {
        this.inputService = inputService;
        this.playerService = playerService;
        this.retryService = retryService;
        this.shootingService = shootingService;
        this.minPlayers = config.getMinPlayers();
        this.maxPlayers = config.getMaxPlayers();
    }


    @Override
    public void initPlayers() {
        // set number of players
        System.out.printf("Enter number of players between %s and %s\n", minPlayers, maxPlayers);
        retryService.retryWhile(() -> {
            playerService.setNumOfPlayers(Integer.parseInt(inputService.getInput()));
            return true;
        }, 5, () -> {
            playerService.setNumOfPlayers(minPlayers);
            System.out.printf("Players quantity set to %s%n", minPlayers);
            return null;
        });
        playerService.initPlayers();

        // change players name
        currentPlayer = playerService.getCurrentPlayer();
        for (int i = 0; i < playerService.getNumOfPlayers(); i++) {
            System.out.printf("%s enter your name%n", currentPlayer.getName());
            retryService.retryWhile(() -> {
                playerService.changeName(currentPlayer, inputService.getInput());
                return true;
            }, 5, () -> {
                System.out.printf("%s name left unchanged%n", currentPlayer.getName());
                return null;
            });
            System.out.println(UserInputService.lineSeparator);
            currentPlayer = playerService.advanceCurrentPlayer();
        }
    }

    @Override
    public void initShips() {
        int numOfPlayers = playerService.getNumOfPlayers();
        for (int i = 0; i < numOfPlayers; i++) {
            playerService.initShipsForPlayer(currentPlayer);
            currentPlayer = playerService.advanceCurrentPlayer();
            if (i + 1 < numOfPlayers) {
                System.out.printf("Press enter to pass the turn to %s%n", currentPlayer.getName());
            } else {
                System.out.print("\nPress enter to start the game");
            }
            inputService.getInput();
            System.out.println(UserInputService.verticalDots);
        }
    }

    @Override
    public void play() {
        System.out.println("\n\t\t *** THE BATTLESHIP GAME STARTS ***\n");
        Player player = currentPlayer;
        AtomicReference<Player> opponent = new AtomicReference<>();
        if (playerService.getNumOfPlayers() > 2) {
            System.out.printf("%s choose your opponent [%s]%n", player.getName(), playerService.getOpponents().stream().map(Player::getName).collect(joining(",")));
            retryService.retryWhile(() -> {
                opponent.set(playerService.getPlayer(inputService.getInput()));
                return true;
            });
        } else {
            opponent.set(playerService.getNextPlayer());
        }

        // TODO implement with retry service ?
        boolean isFinished = false;
        while (!isFinished) {
            // TODO implement game
            takeTurn(opponent.get());
        }
        System.out.println("game finished");
    }

    private void takeTurn(Player opponent) {
        System.out.printf("%s shoot at %s%n", currentPlayer.getName(), opponent.getName());
        // TODO implement retry service
        shootingService.shootAt(opponent, inputService.getInput());
        playerService.advanceCurrentPlayer();
    }
}
