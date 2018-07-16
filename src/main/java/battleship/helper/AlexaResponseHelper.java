package battleship.helper;

import battleship.gui.JButtonWithCoordinates;
import battleship.model.ShotEvent;

import java.lang.reflect.Array;

public class AlexaResponseHelper {

    public static String [] getShotResponse (JButtonWithCoordinates jb, String playerEvent) {
        String computerEvent = "";
            if (Helper.getCurrentPlayerEvent() == ShotEvent.WINNER || Helper.getCurrentComputerEvent() == ShotEvent.WINNER) {
                playerEvent = "GAMEOVER";
            } else {
                if (jb.getState() == ShotEvent.WAIT) {
                    jb.doClick();
                    playerEvent = Helper.getCurrentPlayerEvent().toString();
                    computerEvent = Helper.getCurrentComputerEvent().toString();
                } else {
                    playerEvent = "ALLREADYSHOT";
                }
            }

        String[]s = new String[]{playerEvent,computerEvent};
        return s;
    }
}
