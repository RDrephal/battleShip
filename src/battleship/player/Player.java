package battleship.player;

import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static battleship.model.ShotEvent.*;

public abstract class Player {
    protected Playerboard playerboard;
    protected String[][] ships;
    protected List<Ship> listShips;
    protected boolean alive;

    public abstract ShotEvent fire(Playerboard enemy, Coordinates coordinates);

    public Playerboard getPlayerboard() {
        return playerboard;
    }

    public Player() {
        playerboard = new Playerboard(this);

        Map<String, Integer> shipMap = new HashMap<>();
        shipMap.put("Aircraft Carrier", 5);
        shipMap.put("Battleship", 4);
        shipMap.put("Cruiser", 3);
        shipMap.put("Submarine", 3);
        shipMap.put("Patrol Boat", 2);
        shipMap.put("Corvette", 2);
        shipMap.put("Rescue Ship", 1);

        ships = new String[][]{
                {"Aircraft Carrier", "5"},
                {"Aircraft Carrier", "4"},
                {"Submarine", "3"},
                {"Destroyer", "3"},
                {"Patrol Boat", "2"},
                {"Patrol Boat 2", "2"},
                {"Boat", "1"},
        };

        for (Map.Entry<String, Integer> entry : shipMap.entrySet()) {
            addNewShips(entry.getKey(), entry.getValue());
        }
    }

    protected ShotEvent getShotEvent(Playerboard enemy, Coordinates shot) {
        ShotEvent result = WATER;

        for(Ship ship : enemy.getShipsOnBoard()) {
            for (Coordinates c : ship.getLocations()) {
                String shipX = c.getX();
                Integer shipY = c.getY();

                if (shipX.equals(shot.getX()) && shipY == shot.getY()) {

                    List<Coordinates> ls = ship.getHits();
                    ls.add(shot);
                    ship.setHits(ls);
                    result = HIT;

                    System.out.println("HIT");

                    //Prüfen ob versenkt
                    if (ship.getSunken()) {
                        result = DESTROYED;

                        System.out.println("DESTROYED: " + ship.getName());

                        if (enemy.prefAllShipsSunken()) {
                            result = WINNER;
                            System.out.println("WINNER");
                        }
                    }
                }
            }
        }

        if (result == ShotEvent.WATER) {
            System.out.println("WATER");
        }

        return result;
    }

    protected abstract void addNewShips(String name, int length);
}
