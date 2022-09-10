package org.hyperskill.Battleship.config;

import org.hyperskill.Battleship.beans.ShipType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GameConfig {

    //    private final List<ShipType> shipPattern = List.of(ShipType.CARRIER, ShipType.BATTLESHIP, ShipType.SUBMARINE, ShipType.CRUISER, ShipType.DESTROYER);
    public final List<ShipType> shipPattern = List.of(ShipType.DESTROYER);

    @Value("${board.size}")
    private int boarsSize;


    @Value("${players.minimum}")
    private int minPlayers;


    @Value("${players.maximum}")
    private int maxPlayers;

    @Value("${board.symbol.first.row}")
    private String firstRow;

    @Value("${board.symbol.first.column}")
    private String firstColumn;

    @Value("${board.symbol.sea}")
    private String symbolSea;

    @Value("${board.symbol.ship}")
    private String symbolShip;

    @Value("${board.symbol.hit}")
    private String symbolHit;

    @Value("${board.symbol.miss}")
    private String symbolMiss;

    public int getBoarsSize() {
        return boarsSize;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getFirstRow() {
        return firstRow;
    }

    public String getFirstColumn() {
        return firstColumn;
    }

    public String getSymbolSea() {
        return symbolSea;
    }

    public String getSymbolShip() {
        return symbolShip;
    }

    public String getSymbolHit() {
        return symbolHit;
    }

    public String getSymbolMiss() {
        return symbolMiss;
    }

    public List<ShipType> getShipPattern() {
        return shipPattern;
    }
}
