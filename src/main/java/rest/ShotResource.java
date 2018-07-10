package rest;

import battleship.gui.JButtonWithCoordinates;
import battleship.gui.JButtonWithCoordinatesFactory;
import battleship.helper.Helper;
import battleship.model.ShotEvent;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/shot")
@Produces(MediaType.APPLICATION_JSON)
public class ShotResource {
    private String playerEvent;

    public ShotResource() {
    }

    @POST
    public ShotModel shot(@QueryParam("letter") String letter, @QueryParam("number") Integer number) {
        String computerEvent = "";
        JButtonWithCoordinates jb = JButtonWithCoordinatesFactory.getJButton(letter, number);

        if (jb != null) {
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
        } else {
            playerEvent = "NOTEXISTS";
        }

        return new ShotModel(number, letter, playerEvent, computerEvent);
    }

}
