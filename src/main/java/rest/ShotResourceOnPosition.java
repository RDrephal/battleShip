package rest;

import battleship.gui.GameGUI;
import battleship.gui.JButtonWithCoordinates;
import battleship.gui.JButtonWithCoordinatesFactory;
import battleship.helper.AlexaResponseHelper;
import battleship.helper.Helper;
import battleship.model.ShotEvent;

import javax.swing.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.awt.*;


@Path("/shotOnPosition")
@Produces(MediaType.APPLICATION_JSON)
public class ShotResourceOnPosition {
    private String playerEvent;

    public ShotResourceOnPosition() {
    }

    public Boolean place(){
        Point mousePoint = Helper.getActiveGame().topLevelPanel.getMousePosition();
        GameGUI game = Helper.getActiveGame();
        game.topLevelPanel.getComponentAt(mousePoint);

        return true;
    }

    @POST
    public ShotModel shot() {
        String letter = "";
        Integer number = 666;
        String computerEvent = "";
        Point mousePoint = Helper.getActiveGame().topLevelPanel.getMousePosition();
        GameGUI game = Helper.getActiveGame();
        Component component = game.getGridPanel().getComponentAt(mousePoint);
        if(component != null) {
            System.out.println(mousePoint);
            System.out.println(component);
            JButtonWithCoordinates c = (JButtonWithCoordinates) ((JPanel) component).getComponent(0);

            letter = c.getXValue();
            number = c.getYValue();
            JButtonWithCoordinates jb = JButtonWithCoordinatesFactory.getJButton(letter, number);
            if(jb != null) {
                String[] response = AlexaResponseHelper.getShotResponse(jb, playerEvent);
                playerEvent = response[0];
                computerEvent = response[0];
            } else {
                playerEvent = "NOTEXISTS";
            }
        }else{
            playerEvent = "NOTEXISTS";
        }

        return new ShotModel(number, letter, playerEvent, computerEvent);
    }
}
