package model;

import Player.Player;
import helper.Helper;

import java.util.*;


public class Playerboard {
    private Player owner;
    private List<Ship> playerboard;


    public Playerboard(/*Player owner*/) {
        //this.owner = owner;
        playerboard =  new ArrayList<>();

    }

    public void addShipToGrid(Ship ship){
        List<Coordinates> currentCoordinates = new ArrayList<>();
        Random r = new Random();
        Integer horizontal = r.nextInt(2);
        Integer shipSize = ship.getLength();
        boolean position = false;
        while (position == false) {

            currentCoordinates = new ArrayList<>();
            if (horizontal == 1) {
                Integer x = r.nextInt(Helper.Alphabet.length - shipSize)+1;
                Integer y = r.nextInt(Helper.CoordinateY.length)+1;
                x = x - 1;
                for (int i = 0; i <= shipSize-1; i++) {
                    x += 1;
                    if (checkCoordinates(x, y) == true) {
                        String alpha = Helper.toAlpha(x-1);
                        currentCoordinates.add(new Coordinates(alpha,y));
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
                        currentCoordinates.add(new Coordinates(alpha, y));
                        currentCoordinates.size();
                    }else{
                        break;
                    }
                }
            }
            if(currentCoordinates.size() == shipSize) {
                position = true;
            }
        }
        ship.setLocations(currentCoordinates);
        addToPlayerboard(ship);
    }

    private Boolean checkCoordinates(Integer x, Integer y) {
        List<Ship> ships= getPlayerboard();
        for ( Ship s : ships) {
            List<Coordinates> locations = s.getLocations();
            for (Coordinates a: locations) {
                Integer hx =  Helper.alphaToInt((String) a.getX());
                Integer hy =  a.getY();
                int sum = Math.abs((x - hx) + (y - hy));
                if (sum < 2){
                    return false;
                }
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

    public List<Ship> getPlayerboard() {
        return playerboard;
    }

    public void addToPlayerboard(Ship ship) {
        List<Ship> playerboard = getPlayerboard();
        playerboard.add(ship);
        setPlayerboard(playerboard);
    }

    public void setPlayerboard(List<Ship> playerboard) {
        this.playerboard = playerboard;
    }

    public static void main(String[] args){
        Playerboard s = new Playerboard();
        
        Ship ship = new Ship("name",4);
        s.addShipToGrid(ship);
        List<Ship> playerboard = s.getPlayerboard();
        Ship a = new Ship("a",3);
        s.addShipToGrid(a);
        Ship c = new Ship("c",3);
        s.addShipToGrid(c);
        Ship b = new Ship("b",2);
        s.addShipToGrid(b);
        Ship d = new Ship("d",2);
        s.addShipToGrid(d);

        ship.setSunken(true);
    }
}
