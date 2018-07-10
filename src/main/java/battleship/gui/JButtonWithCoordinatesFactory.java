package battleship.gui;

import java.util.ArrayList;

public class JButtonWithCoordinatesFactory {
    private static ArrayList<JButtonWithCoordinates> jbuttons = new ArrayList<>();

    public static void addJButtons(JButtonWithCoordinates jbutton) {
        jbuttons.add(jbutton);
    }

    public static void clearJButtons() {
        jbuttons.clear();
    }

    public static ArrayList<JButtonWithCoordinates> getJButtons() {
        return jbuttons;
    }

    public static JButtonWithCoordinates getJButton(String x, Integer y) {
        for (JButtonWithCoordinates jb : jbuttons) {
            if (jb.getXValue().toLowerCase().equals(x.toLowerCase()) && jb.getYValue() == y) {
                return jb;
            }
        }
        return null;
    }
}
