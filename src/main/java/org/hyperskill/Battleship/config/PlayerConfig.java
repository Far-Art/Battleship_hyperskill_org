package org.hyperskill.Battleship.config;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.components.ShipPlacementComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PlayerConfig {

    private static int playerNumber = 1;

    private static String initialName = "Player";

    @Bean
    @Scope("prototype")
    public Player getPlayer(Board board, ShipPlacementComponent placement) {
        return new Player(playerNumber, initialName + playerNumber++, board, placement);
    }
}
