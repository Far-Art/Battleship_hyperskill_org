package org.hyperskill.Battleship.config;

import org.hyperskill.Battleship.beans.Board;
import org.hyperskill.Battleship.beans.PlayerBoard;
import org.hyperskill.Battleship.services.interfaces.CellService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BoardConfig {

    @Bean
    @Scope("prototype")
    public Board getBoard(GameConfig gameConfig, CellService cellService) {
        return new PlayerBoard(gameConfig, cellService);
    }
}
