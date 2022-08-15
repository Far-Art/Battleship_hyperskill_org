package battleship;

import battleship.beans.Player;
import battleship.board.Board;
import battleship.services.PlayerService;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.play();
    }

    private void play() {
        Player player1 = new Player("Player 1", new Board());
        Player player2 = new Player("Player 2", new Board());

        PlayerService playerService = new PlayerService(player1, player2);
        playerService.startGame();
    }

}
