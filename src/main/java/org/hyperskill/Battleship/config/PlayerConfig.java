package org.hyperskill.Battleship.config;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.Player;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PlayerConfig {

    private static int playerNumber = 1;

    private static String initialName = "Player";

    @Bean
    @Primary
    public Player getPlayer(Board board) {
        return new Player(playerNumber, initialName + playerNumber++, board);
    }
}
