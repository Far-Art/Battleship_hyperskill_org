package org.hyperskill.Battleship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BattleshipApplication {

	public static void main(String[] args) {
		SpringApplication.run(BattleshipApplication.class, args);
		System.out.println("\n\t ------------ " + BattleshipApplication.class.getSimpleName() + " is running ------------");
	}

}
