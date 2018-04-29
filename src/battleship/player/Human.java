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
    protected void addNewShips(String name, int length) {
        //Replace in the next lession
        Ship ship = new Ship(name, length);
        playerboard.autoAddShipToPlayerboard(ship);
        System.out.println("Human: " + ship.getLocations());
    }

    @Override
    public ShotEvent fire(Playerboard enemy, Coordinates coordinates) {
        return getShotEvent(enemy, coordinates);
    }
}
