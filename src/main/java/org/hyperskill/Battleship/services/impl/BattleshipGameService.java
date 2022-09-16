package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.GameService;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.hyperskill.Battleship.services.interfaces.PrintService;
import org.hyperskill.Battleship.services.interfaces.ShootingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.joining;

@Service
public class BattleshipGameService implements GameService {

    private final UserInputService inputService;

    private final RetryService retryService;

    private final ShootingService shootingService;

    private final PrintService printService;

    private final int minPlayers;

    private final int maxPlayers;

    private final PlayerService playerService;

    private Player currentPlayer;

    @Autowired
    public BattleshipGameService(GameConfig config, UserInputService inputService, PlayerService playerService, RetryService retryService, ShootingService shootingService, PrintService printService) {
        this.inputService = inputService;
        this.playerService = playerService;
        this.retryService = retryService;
        this.shootingService = shootingService;
        this.minPlayers = config.getMinPlayers();
        this.maxPlayers = config.getMaxPlayers();
        this.printService = printService;
    }


    @Override
    public void initPlayers() {
        // set number of players
        System.out.printf("Enter number of players between %s and %s%n", minPlayers, maxPlayers);
        retryService.retryFor(() -> {
            playerService.setInitialNumOfPlayers(Integer.parseInt(inputService.getInput()));
            return true;
        }, 5, () -> {
            playerService.setInitialNumOfPlayers(minPlayers);
            System.out.printf("Players quantity set to %s%n", minPlayers);
            return null;
        });
        playerService.initPlayers();

        // change players name
        currentPlayer = playerService.getCurrentPlayer();
        // TODO uncomment after test
//        for (int i = 0; i < playerService.getInitialNumOfPlayers(); i++) {
//            System.out.printf("%s enter your name%n", currentPlayer.getName());
//            retryService.retryFor(() -> {
//                playerService.changeName(currentPlayer, inputService.getInput());
//                return true;
//            }, 5, () -> {
//                System.out.printf("%s name left unchanged%n", currentPlayer.getName());
//                return null;
//            });
//            System.out.println(UserInputService.lineSeparator);
//            currentPlayer = playerService.advanceCurrentPlayer();
//        }
    }

    @Override
    public void initShips() {
        int numOfPlayers = playerService.getInitialNumOfPlayers();
        for (int i = 0; i < numOfPlayers; i++) {
            playerService.initShipsForPlayer(currentPlayer);
            currentPlayer = playerService.advanceCurrentPlayer();
            if (i + 1 == numOfPlayers) {
                System.out.println(UserInputService.verticalDots);
                System.out.println("\nPress enter to start the game");
                inputService.getInput();
            } else {
                System.out.println(UserInputService.verticalDots);
            }
        }
    }

    @Override
    public void play() {
        System.out.println("\n\t\t *** THE BATTLESHIP GAME STARTS ***\n");

        // TODO implement with retry service ?
        boolean isFinished = false;
        while (!isFinished) {
            if (playerService.getNumOfActivePlayers() == 1) {
                break;
            }
            boolean choosePlayer = playerService.getNumOfActivePlayers() > 2;
            AtomicReference<Player> opponent = new AtomicReference<>();
            if (choosePlayer) {
                System.out.printf("%s choose your opponent [%s]%n", currentPlayer.getName(), playerService.getActiveOpponents().stream().map(Player::getName).collect(joining(",")));
                retryService.retryWhile(() -> {
                    Player p = playerService.getPlayer(inputService.getInput());
                    if (p.isLost()) {
                        throw new InputMismatchException(String.format("%s already deservedly lost", p.getName()));
                    }
                    opponent.set(p);
                    return true;
                });
            } else {
                opponent.set(playerService.getNextPlayer());
            }
            takeTurn(opponent.get());
        }
        System.out.println("The Battleship game finished");
        System.out.printf("%s has won!!!", currentPlayer.getName());
    }

    private void takeTurn(Player opponent) {
        printService.printPlayerBoards(currentPlayer, opponent);
        System.out.printf("%s shoot at %s%n", currentPlayer.getName(), opponent.getName());
        retryService.retryWhile(() -> {
            shootingService.shootAt(opponent, inputService.getInput());
            return true;
        });
        // TODO it is bug here that player is not considered lost after last ship sunk, debug please
        if (opponent.isLost()) {
            System.out.printf("%s was completely destroyed by last shot from %s!!!", opponent.getName(), currentPlayer.getName());
        }
        currentPlayer = playerService.advanceCurrentPlayer();
    }

}
