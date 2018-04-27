package src.GUI;

import javax.swing.*;
import java.awt.*;

public class JButtonWithCoordinates extends JButton {
    private int xValue;
    private int yValue;
    private Font font;
    private Color defaultColor;


    public JButtonWithCoordinates(int xValue, int yValue){
        font = new Font("Arial", Font.BOLD, 20);
        this.setFont(font);

        this.defaultColor = new Color(0,102,153);

        this.xValue = xValue;
        this.yValue = yValue;

        this.setText("");
    }

    public void resetButton(){
        this.setText("");
        this.setBackground(defaultColor);
    }

    public void hit(){
        this.setText("X");
    }

    public void noHit(){
        this.setText("O");
    }

    public void changeColor(int x, int y){
        //Um mehrere Felder farbig zu hinterlegen
    }

    public int getXValue(){
        return xValue;
    }

    public int getYValue(){
        return yValue;
    }
}
