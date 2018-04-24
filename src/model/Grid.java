package model;

import Player.Player;
import helper.Helper;

import java.util.HashMap;

import java.util.Map;
import java.util.Random;


public class Grid {
    private Player owner;
    private Map<Integer,Integer> playerboard;


    public Grid(/*Player owner*/) {
        //this.owner = owner;
        playerboard = new HashMap<Integer, Integer>();

    }

    public void addShipToGrid(Ship ship){
        Map<String, Integer> currentCoordinate = new HashMap<>();
        Map<Integer, Integer> currentPlayerBoard = new HashMap<>();

        boolean position = false;
        while (position == false) {
            currentCoordinate = new HashMap<>();
            currentPlayerBoard = new HashMap<>();
            Integer shipSize = ship.getLength();
            Random r = new Random();
            Boolean horizontal = r.nextBoolean();

            if (horizontal == true) {
                Integer x = r.nextInt(Helper.Alphabet.length - shipSize)+1;
                Integer y = r.nextInt(Helper.CoordinateY.length)+1;
                x = x - 1;
                for (int i = 0; i <= shipSize-1; i++) {
                    x += 1;
                    if (checkCoordinates(x, y) == true) {
                        currentPlayerBoard.put(x,y);
                        String alpha = Helper.toAlpha(x-1);
                        currentCoordinate.put(alpha, y);
                    }else{
                        break;
                    }
                }
            }else{
                Integer x = r.nextInt(Helper.Alphabet.length)+1;
                Integer y = r.nextInt(Helper.CoordinateY.length - shipSize)+1;
                y = y - 1;
                for (int i = 0; i <= shipSize-1; i++) {
                    y += 1;
                    if (checkCoordinates(x, y) == true) {
                        String alpha = Helper.toAlpha(x-1);
                        currentCoordinate.put(alpha, y);
                        currentPlayerBoard.put(x,y);
                    }else{
                        break;
                    }
                }
            }
            if(currentCoordinate.size() == shipSize) {
                position = true;
            }
        }
        ship.setLocations(currentCoordinate);
        playerboard.putAll(currentPlayerBoard);
    }

    private Boolean checkCoordinates(Integer x, Integer y) {
        for (HashMap.Entry s: playerboard.entrySet()) {
            Integer hx = (Integer) s.getKey();
            Integer hy = (Integer) s.getValue();
            if (Math.abs(x - hx + y - hy) < 2){
                return false;
            }
        }
        return true;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Map<Integer, Integer> getPlayerboard() {
        return playerboard;
    }


    public static void main(String[] args){
        Grid s = new Grid();
        Ship ship = new Ship("name",4);
        s.addShipToGrid(ship);
        Ship a = new Ship("a",3);
        s.addShipToGrid(a);
        Ship b = new Ship("b",2);
        s.addShipToGrid(b);

        Ship c = new Ship("c",3);
        s.addShipToGrid(c);
        Ship d = new Ship("d",2);
        s.addShipToGrid(d);
    }
}
