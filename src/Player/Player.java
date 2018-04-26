package Player;

import helper.Helper;
import model.Coordinates;
import model.Playerboard;
import model.Ship;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected Playerboard playerboard;
    protected String[][] ships;
    protected List<Ship> listShips;
    protected boolean alive;

    public abstract void fire(Playerboard enemy, Coordinates coordinates);

    public Playerboard getPlayerboard() {
        return playerboard;
    }

    public Player() {
        playerboard = new Playerboard(this);
        ships = new String[][]{
                {"Aircraft Carrier", "5"},
                {"Aircraft Carrier", "4"},
                {"Submarine", "3"},
                {"Destroyer", "3"},
                {"Patrol Boat", "2"},
                {"Patrol Boat 2", "2"},
                {"Boat", "1"},
        };

        for (int i = 0; i <= ships.length-1; i++)
            addNewShips(ships[i][0],ships[i][1]);
    }

    protected abstract void addNewShips(String s, String s1);
}
