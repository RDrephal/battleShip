package battleship.Player;

import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvents;

import java.util.List;

import static battleship.model.ShotEvents.*;

public abstract class Player {
    protected Playerboard playerboard;
    protected String[][] ships;
    protected List<Ship> listShips;
    protected boolean alive;

    public abstract ShotEvents fire(Playerboard enemy, Coordinates coordinates);

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

    protected ShotEvents getShotEvent(Playerboard enemy, Coordinates shot, String fireX, Integer fireY) {
        ShotEvents result = WATER;

        for(Ship ship :enemy.getPlayerboard()){
            for (Coordinates c : ship.getLocations()){
                String shipX = c.getX();
                Integer shipY = c.getY();

                if (shipX ==fireX && shipY == fireY){

                    List<Coordinates> ls = ship.getHits();
                    ls.add(shot);
                    ship.setHits(ls);
                    result = HIT;

                    //Prüfen ob versenkt
                    if (ship.getSunken()){
                        result = DESTROY;

                        if(enemy.prefAllShipsSunken()){
                            result = WINNER;
                        }
                    }
                }
            }
        }
        return result;
    }

    protected abstract void addNewShips(String s, String s1);
}
