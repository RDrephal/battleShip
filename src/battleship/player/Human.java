package battleship.player;

import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvent;


public class Human extends Player {

    public Human()  {
        super();
    }

    @Override
    protected void addNewShips(String s, String s1) {
        //Replace in the next lession
        Integer i1= Integer.valueOf(s1);
        Ship ship = new Ship(s,i1);
        playerboard.autoAddShipToPlayerboard(ship);
        System.out.println("Human: " + ship.getLocations());
    }

    @Override
    public ShotEvent fire(Playerboard enemy, Coordinates coordinates) {
        String fireX = coordinates.getX();
        Integer fireY = coordinates.getY();
        return getShotEvent(enemy, coordinates, fireX, fireY);
    }
}
