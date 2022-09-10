package org.hyperskill.Battleship.config;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.PlayerBoard;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardConfig {

    @Bean
    public Board getBoard(GameConfig gameConfig, CellService cellService) {
        return new PlayerBoard(gameConfig, cellService);
    }
}
