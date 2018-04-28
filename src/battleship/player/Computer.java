package battleship.player;

import battleship.helper.Helper;
import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Computer extends Player {

    private List<Coordinates> firing;

    public Computer(){
        super();
        firing = new ArrayList<>();

        for (String s : Helper.Alphabet){
            for(Integer i : Helper.CoordinateY){
                firing.add(new Coordinates(s,i));
            }
        }
        setFiring(firing);
    }
    @Override
    public ShotEvents fire(Playerboard enemy, Coordinates coordinates) {
        //Mögliche Schüsse in der Liste listFire. Vorerst  nur Random Coordinates

        List<Coordinates>listFire = getFiring();
        Integer listSize = getFiring().size();
        Random r = new Random();
        Integer shotIndex = r.nextInt(listSize);
        Coordinates shot = listFire.get(shotIndex);
        listFire.remove(shotIndex);
        setFiring(listFire);
        String fireX = shot.getX();
        Integer fireY = shot.getY();
        ShotEvents result = getShotEvent(enemy, shot, fireX, fireY);

        return result;
    }

    @Override
    protected void addNewShips(String s, String s1) {
        Integer i1= Integer.valueOf(s1);
        Ship ship = new Ship(s,i1);
        playerboard.autoAddShipToPlayerboard(ship);
    }

    public List<Coordinates> getFiring() {
        return firing;
    }

    public void setFiring(List<Coordinates> firing) {
        this.firing = firing;
    }
}
