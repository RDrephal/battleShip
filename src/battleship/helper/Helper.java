package battleship.helper;

import battleship.model.Coordinates;

import java.util.Arrays;
/**
 * Helper and Converter for Coordinates
 */
public class Helper {

    public static final String[] Alphabet = {"A","B","C","D","E","F","G","H","I","J"};
    public static final Integer[] CoordinateY= {1,2,3,4,5,6,7,8,9,10};

    /**
     *
     * @param n
     * @return x Coordinate
     */
    public static String toAlpha(int n)
    {
        return Alphabet[n];
    }

    /**
     *
     * @param s
     * @return xCoordinate index
     */
    public static Integer alphaToInt(String s){
            Integer i = (Arrays.asList(Alphabet).indexOf(s))+1;
        return i;
    }

    //Converts coordinates (int,int) to (string,int) | static to be accesses from everywhere without thee need for an object of GameGUI
    public static String convertIntegerToString(int x) {

        String sx = "";

        switch (x) {
            case 1:
                sx = "A";
                break;
            case 2:
                sx = "B";
                break;
            case 3:
                sx = "C";
                break;
            case 4:
                sx = "D";
                break;
            case 5:
                sx = "E";
                break;
            case 6:
                sx = "F";
                break;
            case 7:
                sx = "G";
                break;
            case 8:
                sx = "H";
                break;
            case 9:
                sx = "I";
                break;
            case 10:
                sx = "J";
                break;

            default: break;

        }
        return sx;
    }

    //Converts letters to their respective coordinate numbers | static to be accesses from everywhere without thee need for an object of GameGUI
    public static int convertStringToInteger(String x) {
        int value = -1;

        switch (x) {
            case "A":
                value = 1;
                break;
            case "B":
                value = 2;
                break;
            case "C":
                value = 3;
                break;
            case "D":
                value = 4;
                break;
            case "E":
                value = 5;
                break;
            case "F":
                value = 6;
                break;
            case "G":
                value = 7;
                break;
            case "H":
                value = 8;
                break;
            case "I":
                value = 9;
                break;
            case "J":
                value = 10;
                break;

            default: break;

        }
        return value;
    }
}
