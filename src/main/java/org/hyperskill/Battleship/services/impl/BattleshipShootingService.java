package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Cell;
import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.beans.Visibility;
import org.hyperskill.Battleship.config.GameConfig;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.hyperskill.Battleship.services.interfaces.PlayerService;
import org.hyperskill.Battleship.services.interfaces.ShootingService;
import org.springframework.stereotype.Service;

@Service
public class BattleshipShootingService implements ShootingService {

    private final PlayerService playerService;

    private final CellService cellService;

    private final String ship;

    private final String sea;

    private final String miss;

    private final String hit;

    public BattleshipShootingService(PlayerService playerService, CellService cellService, GameConfig config) {
        this.playerService = playerService;
        this.cellService = cellService;
        this.ship = config.getSymbolShip();
        this.sea = config.getSymbolSea();
        this.miss = config.getSymbolMiss();
        this.hit = config.getSymbolHit();
    }

    @Override
    public void shootAt(Player player, String cell) {
        Cell shotCell = player.getBoard().getCell(cell);
        switch (shotCell.getSymbol()) {
            // TODO implement new java 17 switch case
        }

        // TODO validate cell input
        // TODO implement checking for already hit cells
        // TODO implement checking if ship destroyed
        // TODO provide current player
        if (shotCell.getSymbol().equals(sea)) {
            shotCell.setSymbol(miss);
            System.out.println("You missed!");
        } else if (shotCell.getSymbol().equals(ship)) {
            shotCell.setSymbol(hit);
            System.out.println("You hit a ship!");
        }
        player.getBoard().print(Visibility.HIDDEN);
    }
}
