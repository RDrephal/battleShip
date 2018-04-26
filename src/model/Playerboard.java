package model;

import Player.Human;
import Player.Player;
import helper.Helper;

import java.util.*;


public class Playerboard {
    public Player owner;
    private List<Ship> playerboard;


    public Playerboard(Player owner) {
        this.owner = owner;
        playerboard =  new ArrayList<>();

    }

    public void autoAddShipToPlayerboard(Ship ship){
        List<Coordinates> currentCoordinates = new ArrayList<>();
        Integer alphabethLength= Helper.Alphabet.length;
        Integer coordinatesLength= Helper.CoordinateY.length;
        Integer shipSize = ship.getLength();
        Integer counter = 0;
        Random r = new Random();
        boolean position = false;
        while (position == false) {
            counter++;

            Integer horizontal = r.nextInt(2);
            currentCoordinates = new ArrayList<>();
            if (horizontal == 0) {
                Integer x = r.nextInt(alphabethLength - (shipSize-1))+1;
                Integer y = r.nextInt(coordinatesLength)+1;
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
                Integer x = r.nextInt(alphabethLength)+1;
                Integer y = r.nextInt(coordinatesLength - (shipSize-1))+1;
                y = y - 1;
                for (int i = 0; i <= shipSize-1; i++) {
                    y += 1;
                    if (checkCoordinates(x, y) == true) {
                        String alpha = Helper.toAlpha(x-1);
                        currentCoordinates.add(new Coordinates(alpha, y));
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

    public Boolean prefAllShipsSunken(){
        List<Ship> ships = getPlayerboard();
        for ( Ship s : ships) {
            if(s.getSunken() == false){
                return true;
            }
        }
        return false;
    }

    private Boolean checkCoordinates(Integer x, Integer y) {
        List<Ship> ships= getPlayerboard();
        for ( Ship s : ships) {
            List<Coordinates> locations = s.getLocations();
            for (Coordinates a: locations) {
                Integer hx =  Helper.alphaToInt((String) a.getX());
                Integer hy =  a.getY();
                int sum = (Math.abs((x - hx)) + Math.abs((y - hy)));
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
        Player p = new Human();
    }
}
