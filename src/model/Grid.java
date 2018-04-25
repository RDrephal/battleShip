package model;

import Player.Player;
import helper.Helper;

import java.util.*;


public class Grid {
    private Player owner;
    private List<Ship> playerboard;


    public Grid(/*Player owner*/) {
        //this.owner = owner;
        playerboard =  new ArrayList<>();

    }

    public void addShipToGrid(Ship ship){
        List<Coordinate> currentCoordinate = new ArrayList<>();

        Integer shipSize = ship.getLength();
        boolean position = false;
        while (position == false) {
            Random r = new Random();
            Boolean horizontal = r.nextBoolean();
            currentCoordinate = new ArrayList<>();
            if (horizontal == true) {
                Integer x = r.nextInt(Helper.Alphabet.length - shipSize)+1;
                Integer y = r.nextInt(Helper.CoordinateY.length)+1;
                x = x - 1;
                for (int i = 0; i <= shipSize-1; i++) {
                    x += 1;
                    if (checkCoordinates(x, y) == true) {
                        String alpha = Helper.toAlpha(x-1);
                        currentCoordinate.add(new Coordinate(alpha,y));
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
                        currentCoordinate.add(new Coordinate(alpha, y));
                        currentCoordinate.size();
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
        addToPlayerboard(ship);
    }

    private Boolean checkCoordinates(Integer x, Integer y) {
        List<Ship> ships= getPlayerboard();
        for ( Ship s : ships) {
            List<Coordinate> locations = s.getLocations();
            for (Coordinate a: locations) {
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
        Grid s = new Grid();
        Ship ship = new Ship("name",4);
        s.addShipToGrid(ship);
        List<Ship> playerboard = s.getPlayerboard();
        Ship a = new Ship("a",3);
        s.addShipToGrid(a);
        Ship b = new Ship("b",2);
        s.addShipToGrid(b);

        Ship c = new Ship("c",3);
        s.addShipToGrid(c);
        Ship d = new Ship("d",2);
        s.addShipToGrid(d);
        ship.setSunken(true);

    }
}
