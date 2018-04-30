package battleship.helper;

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
}
