package battleship.player;

import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvent;


public class Human extends Player {

    public Human() {
        super();
    }

    /**
     * Add new Ships to the Board Randomly
     *
     * @param name
     * @param length
     */
    @Override
    protected void addNewShips(String name, int length) {
        //Replace in the next lession
        Ship ship = new Ship(name, length);
        playerboard.autoAddShipToPlayerboard(ship);
        System.out.println("Human: " + ship.getLocations());
    }

    /**
     * Fire from the Human uses hit controler
     *
     * @param enemy
     * @param coordinates
     * @return ShotEvent
     */
    @Override
    public ShotEvent fire(Playerboard enemy, Coordinates coordinates) {
        return getShotEvent(enemy, coordinates);
    }
}
