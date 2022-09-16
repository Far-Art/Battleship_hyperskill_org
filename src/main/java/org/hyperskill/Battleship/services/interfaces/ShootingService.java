package org.hyperskill.Battleship.services.interfaces;

import org.hyperskill.Battleship.beans.Cell;
import org.hyperskill.Battleship.beans.Player;

public interface ShootingService {

    boolean shootAt(Player player, String cell);
}
