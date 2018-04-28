package battleship.gui;

import battleship.model.Coordinates;

import javax.swing.*;
import java.awt.*;

public class JButtonWithCoordinates extends JButton {
    private battleship.model.Coordinates coords;
    private Font font;
    private Color defaultColor;


    public JButtonWithCoordinates(battleship.model.Coordinates coords){
        font = new Font("Arial", Font.BOLD, 20);
        this.setFont(font);

        this.defaultColor = new Color(0,102,153);

        this.coords = coords;

        resetButton();
    }

    public void resetButton(){
        this.setText("");
        this.setBackground(defaultColor);
    }

    //Change button to indicate a hit
    public void hit(){
        this.setText("X");
    }

    //Change button to indicate a miss
    public void noHit(){
        this.setText("O");
    }

    public void changeColor(int x, int y){
        //Um mehrere Felder farbig zu hinterlegen
    }

    public String getXValue(){
        return coords.getX();
    }

    public int getYValue(){
        return coords.getY();
    }

    public Coordinates getCoords() {
        return coords;
    }
}
