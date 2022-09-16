package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Cell;
import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.beans.Ship;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.hyperskill.Battleship.services.interfaces.PrintService;
import org.hyperskill.Battleship.services.interfaces.ShootingService;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Service
public class BattleshipShootingService implements ShootingService {

    private final PlayerService playerService;

    private final CellService cellService;

    private final RetryService retryService;


    private final PrintService printService;

    private final String ship;

    private final String sea;

    private final String miss;

    private final String hit;

    public BattleshipShootingService(PlayerService playerService, CellService cellService, RetryService retryService, PrintService printService, GameConfig config) {
        this.playerService = playerService;
        this.cellService = cellService;
        this.retryService = retryService;
        this.printService = printService;
        this.ship = config.getSymbolShip();
        this.sea = config.getSymbolSea();
        this.miss = config.getSymbolMiss();
        this.hit = config.getSymbolHit();
    }

    @Override
    public boolean shootAt(Player playersOpponent, String cell) {
        Cell shotCell = playersOpponent.getBoard().getCell(cell);

        String symbol = shotCell.getSymbol();
        Ship hitShip = playersOpponent.placement().getShipByCell(shotCell);

        if (symbol.equals(miss)) {
            throw new InputMismatchException("Missed cell won't contain a ship in the future");
        } else if (symbol.equals(hit)) {
            if (hitShip.isDestroyed()) {
                throw new InputMismatchException(String.format("%s's %s already sunk", playersOpponent.getName(), hitShip.getCategory().getName()));
            }
            throw new InputMismatchException("This ship already hit");
        }

        printService.printPlayerBoards(playerService.getCurrentPlayer(), playersOpponent);

        if (symbol.equals(sea)) {
            shotCell.setSymbol(miss);
            System.out.println("You missed!");
        } else if (symbol.equals(ship)) {
            shotCell.setSymbol(hit);
            if (hitShip.isDestroyed()) {
                System.out.printf("You sunk %s of %s!%n", hitShip.getCategory().getName(), playersOpponent.getName());
            } else {
                System.out.println("You hit a ship!");
            }
        }
        System.out.println();
        return true;
    }
}
