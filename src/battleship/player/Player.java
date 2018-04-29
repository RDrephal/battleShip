package battleship.player;

import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvent;

import java.util.List;

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



        ships = new String[][]{
                {"Aircraft Carrier", "5"},
                {"Aircraft Carrier", "4"},
                {"Submarine", "3"},
                {"Destroyer", "3"},
                {"Patrol Boat", "2"},
                {"Patrol Boat 2", "2"},
                {"Boat", "1"},
        };

        for (int i = 0; i <= ships.length-1; i++)
            addNewShips(ships[i][0],ships[i][1]);
    }

    // TODO: change parameter to Coordinates
    protected ShotEvent getShotEvent(Playerboard enemy, Coordinates shot, String fireX, Integer fireY) {
        ShotEvent result = WATER;

        for(Ship ship : enemy.getShipsOnBoard()) {
            for (Coordinates c : ship.getLocations()) {
                String shipX = c.getX();
                Integer shipY = c.getY();

                if (shipX.equals(fireX) && shipY == fireY) {

                    List<Coordinates> ls = ship.getHits();
                    ls.add(shot);
                    ship.setHits(ls);
                    result = HIT;

                    System.out.println("HIT");

                    //Prüfen ob versenkt
                    if (ship.getSunken()) {
                        result = DESTROYED;

                        System.out.println("DESTROYED");

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

    protected abstract void addNewShips(String s, String s1);
}
