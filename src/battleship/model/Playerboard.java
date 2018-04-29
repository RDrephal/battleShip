package battleship.model;

import battleship.player.Human;
import battleship.player.Player;
import battleship.helper.Helper;

import java.util.*;


public class Playerboard {
    public Player owner;
    private List<Ship> shipsOnBoard;


    public Playerboard(Player owner) {
        this.owner = owner;
        shipsOnBoard =  new ArrayList<>();

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
            } else {
                Integer x = r.nextInt(alphabethLength)+1;
                Integer y = r.nextInt(coordinatesLength - (shipSize-1))+1;
                y = y - 1;
                for (int i = 0; i <= shipSize-1; i++) {
                    y += 1;
                    if (checkCoordinates(x, y) == true) {
                        String alpha = Helper.toAlpha(x-1);
                        currentCoordinates.add(new Coordinates(alpha, y));
                    } else {
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

    /**
     * compare size of list with all ships with count of sunken ships
     * if size = count -> all ships are destroyed
     * @return boolean whether all ships were destroyed
     */
    public Boolean prefAllShipsSunken(){
        List<Ship> ships = getShipsOnBoard();
        int sunkenShips = 0;
        for (Ship s : ships) {
            if(s.getSunken()) {
                sunkenShips++;
            }
        }
        System.out.println("Sunken ships: " + sunkenShips);
        return sunkenShips == ships.size();
    }

    private Boolean checkCoordinates(Integer x, Integer y) {
        List<Ship> ships= getShipsOnBoard();
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

    public List<Ship> getShipsOnBoard() {
        return shipsOnBoard;
    }

    public void addToPlayerboard(Ship ship) {
        List<Ship> playerboard = getShipsOnBoard();
        playerboard.add(ship);
        setShipsOnBoard(playerboard);
    }

    public void setShipsOnBoard(List<Ship> shipsOnBoard) {
        this.shipsOnBoard = shipsOnBoard;
    }

    public static void main(String[] args){
        Player p = new Human();
    }
}
