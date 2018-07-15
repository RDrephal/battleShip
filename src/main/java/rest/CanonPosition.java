package rest;

import battleship.gui.GameGUI;
import battleship.helper.Helper;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;


@Path("/canonPosition")
@Produces(MediaType.APPLICATION_JSON)
public class CanonPosition {
    private String playerEvent;

    public CanonPosition() {
    }

    @POST
    public BooleanModel place(){
        //Point mousePoint = Helper.getActiveGame().topLevelPanel.getMousePosition();
        //GameGUI game = Helper.getActiveGame();
       // game.topLevelPanel.getComponentAt(mousePoint);

        return new BooleanModel(true);
    }
}
