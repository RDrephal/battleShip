package battleship.helper;

import battleship.gui.GameGUI;
import battleship.model.ShotEvent;

import java.util.Arrays;

/**
 * Helper and Converter for Coordinates
 */
public class Helper {

    public static final String[] Alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    public static final Integer[] CoordinateY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static ShotEvent currentPlayerEvent;
    public static ShotEvent currentComputerEvent;
    public static GameGUI activeGame;

    /**
     * @param n
     * @return x Coordinate
     */
    public static String toAlpha(int n) {
        return Alphabet[n - 1];
    }

    /**
     * @param s
     * @return xCoordinate index
     */
    public static Integer alphaToInt(String s) {
        Integer i = (Arrays.asList(Alphabet).indexOf(s)) + 1;
        return i;
    }


    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static ShotEvent getCurrentPlayerEvent() {
        return currentPlayerEvent;
    }

    public static void setCurrentPlayerEvent(ShotEvent currentPlayerEvent) {
        Helper.currentPlayerEvent = currentPlayerEvent;
    }

    public static ShotEvent getCurrentComputerEvent() {
        return currentComputerEvent;
    }

    public static void setCurrentComputerEvent(ShotEvent currentComputerEvent) {
        Helper.currentComputerEvent = currentComputerEvent;
    }

    public static GameGUI getActiveGame() {
        return activeGame;
    }

    public static void setActiveGame(GameGUI activeGame) {
        Helper.activeGame = activeGame;
    }
}