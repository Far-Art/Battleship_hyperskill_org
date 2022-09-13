package org.hyperskill.Battleship.services.interfaces;

import org.hyperskill.Battleship.beans.Cell;
import org.hyperskill.Battleship.beans.Player;

public interface ShootingService {

    void shootAt(Player player, String cell);
}
