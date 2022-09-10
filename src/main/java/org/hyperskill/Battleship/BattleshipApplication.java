package org.hyperskill.Battleship;

import org.hyperskill.Battleship.services.interfaces.GameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:game.properties")
})
public class BattleshipApplication {

    private static GameService game;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BattleshipApplication.class, args);
        game = context.getBean(GameService.class);
        System.out.println("\n\t ------------ " + BattleshipApplication.class.getSimpleName() + " is running ------------");
        game.startGame();
    }

}
