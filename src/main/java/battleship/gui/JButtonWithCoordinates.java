package battleship.gui;

import battleship.model.Coordinates;
import battleship.model.ShotEvent;

import javax.swing.*;
import java.awt.*;

public class JButtonWithCoordinates extends JButton {
    public battleship.model.Coordinates coords;
    private Font font;
    private Color defaultColor;
    private ShotEvent state = ShotEvent.WAIT;

    public JButtonWithCoordinates(battleship.model.Coordinates coords) {
        font = new Font("Arial", Font.BOLD, 20);
        this.setFont(font);
        this.defaultColor = new Color(0, 102, 153);
        this.coords = coords;
        resetButton();
    }

    public void resetButton() {
        this.setText("");
        this.setBackground(defaultColor);
        this.setEnabled(true);
        this.setState(ShotEvent.WAIT);
    }

    //Change button to indicate a hit
    public void hit(ShotEvent event) {
        this.setText("X");
        this.setEnabled(false);
        this.setState(event);
    }

    //Change button to indicate a miss
    public void noHit(ShotEvent event) {
        this.setText("O");
        this.setEnabled(false);
        this.setState(event);
    }

    public String getXValue() {
        return coords.getX();
    }

    public int getYValue() {
        return coords.getY();
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setColorRed() {
        this.setBackground(new Color(255, 0, 0));
    }

    public ShotEvent getState() {
        return state;
    }

    public void setState(ShotEvent state) {
        this.state = state;
    }
}
