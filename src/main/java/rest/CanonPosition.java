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
    public BooleanModel place() {
        GameGUI game = Helper.getActiveGame();

        Point mousePosition = game.topLevelPanel.getMousePosition();
        if (mousePosition != null && game.myTurn.getComponentAt(mousePosition) == game.canon) {
            return new BooleanModel(true);
        } else {
            return new BooleanModel(false);
        }
    }
}
