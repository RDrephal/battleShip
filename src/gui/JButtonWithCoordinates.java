package src.GUI;

import javax.swing.*;

public class JButtonWithCoordinates extends JButton {
    int xValue;
    int yValue;

    public JButtonWithCoordinates(int xValue, int yValue){
        this.xValue = xValue;
        this.yValue = yValue;

        this.setText("");
    }

    public void resetButton(){
        //Hier soll sich der Button auf den Spielstart-Zustand zurücksetzen
    }
}
