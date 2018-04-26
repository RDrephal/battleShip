package Player;

import helper.Helper;
import model.Coordinates;
import model.Playerboard;
import model.Ship;

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
    public void fire(Playerboard enemy, Coordinates coordinates) {
        //Mögliche Schüsse in der Liste listFire. Vorerst  nur Random Coordinates

        List<Coordinates>listFire = getFiring();
        Integer listSize = getFiring().size();
        Random r = new Random();
        Integer shotIndex = r.nextInt(listSize);
        Coordinates shot = listFire.get(shotIndex);
        listFire.remove(shotIndex);
        String fireX = shot.getX();
        Integer fireY = shot.getY();

        for(Ship ship :enemy.getPlayerboard()){
            for (Coordinates c : ship.getLocations()){
                String shipX = c.getX();
                Integer shipY = c.getY();

                if (shipX ==fireX && shipY == fireY){

                    List<Coordinates> ls = ship.getHits();
                    ls.add(shot);
                    ship.setHits(ls);

                    //Prüfen ob versenkt
                }
            }
        }
        //Rückgabe werd einfügen Eventuell Treffer, Wasser, Versenkt, Game Over;

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
