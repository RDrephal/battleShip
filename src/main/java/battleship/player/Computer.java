package battleship.player;

import battleship.helper.Helper;
import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Computer extends Player {

    private List<Coordinates> firing;

    public Computer(){
        super();
        firing = new ArrayList<Coordinates>();

        for (String s : Helper.Alphabet){
            for(Integer i : Helper.CoordinateY){
                firing.add(new Coordinates(s,i));
            }
        }
        setFiring(firing);
    }

    /**
     * Computer needs randomly fire shots
     * @param enemy
     * @return ShotEvent
     */
    public ShotEvent fire(Playerboard enemy) {
        //Mögliche Schüsse in der Liste listFire. Vorerst  nur Random Coordinates

        List<Coordinates> listFire = getFiring();
        int listSize = getFiring().size();
        Random r = new Random();
        int shotIndex = r.nextInt(listSize);
        Coordinates shot = listFire.get(shotIndex);
        listFire.remove(shotIndex);
        setFiring(listFire);
        return getShotEvent(enemy, shot);
    }

    @Override
    public ShotEvent fire(Playerboard enemy, Coordinates coordinates) {
        return null;
    }
    /**
     * Add new Ships to the Board Randomly
     * @param name
     * @param length
     */
    @Override
    protected void addNewShips(String name, int length) {
        Ship ship = new Ship(name, length);
        playerboard.autoAddShipToPlayerboard(ship);
        System.out.println("Computer: " + ship.getLocations());
    }


    public void feedback(ShotEvent event){
        //Feedback whether the computer hit or not
    }

    public List<Coordinates> getFiring() {
        return firing;
    }

    public void setFiring(List<Coordinates> firing) {
        this.firing = firing;
    }
}
