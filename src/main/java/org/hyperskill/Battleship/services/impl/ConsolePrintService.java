package org.hyperskill.Battleship.services.impl;

import org.hyperskill.Battleship.beans.Player;
import org.hyperskill.Battleship.beans.Visibility;
import org.hyperskill.Battleship.services.interfaces.PrintService;
import org.springframework.stereotype.Service;

@Service
public class ConsolePrintService implements PrintService {

    @Override
    public void printPlayerBoards(Player current, Player opponent) {
        System.out.printf("\n---- %s board ----", opponent.getName());
        opponent.getBoard().print(Visibility.HIDDEN);
        System.out.printf("---- %s board ----", "Your");
        current.getBoard().print(Visibility.VISIBLE);
    }
}
