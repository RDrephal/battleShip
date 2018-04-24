package src.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class GameGUI {

    private JPanel topLevelPanel;
    private JPanel cards;
    private JPanel myTurn;
    private JPanel enemyTurn;
    private JButton restartButton;
    private JTextPane aTextPane;
    private JTextPane bTextPane;
    private JTextPane cTextPane;
    private JTextPane dTextPane;
    private JTextPane eTextPane;
    private JTextPane fTextPane;
    private JTextPane gTextPane;
    private JTextPane hTextPane;
    private JTextPane iTextPane;
    private JTextPane jTextPane;
    private JTextPane a1TextPane;
    private JTextPane a2TextPane;
    private JTextPane a3TextPane;
    private JTextPane a4TextPane;
    private JTextPane a5TextPane;
    private JTextPane a6TextPane;
    private JTextPane a7TextPane;
    private JTextPane a8TextPane;
    private JTextPane a9TextPane;
    private JTextPane a10TextPane;
    private JButton button1;
    private JPanel gridPanel;
    private JTextPane textPane2;
    private JTextPane textPane3;
    private JTextPane textPane4;
    private JTextPane textPane5;
    private JTextPane textPane6;
    private JTextPane textPane7;
    private JTextPane textPane8;
    private JTextPane textPane9;
    private JTextPane textPane1;
    private JTextPane textPane11;
    private JTextPane textPane12;
    private JTextPane textPane13;
    private JTextPane textPane14;
    private JTextPane textPane15;
    private JTextPane textPane16;
    private JTextPane textPane17;
    private JTextPane textPane18;
    private JTextPane textPane19;
    private JTextPane textPane10;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Schiffe Versenken");
        frame.setContentPane(new GameGUI().topLevelPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public void addButtons() {
        LinkedList<JButton> buttonList = new LinkedList<JButton>();

        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                buttonList.add(new JButton());
                //buttonList.getFirst().setPreferredSize(new Dimension(50, 50));
                //buttonList.getFirst().setVisible(true);

                //Buttons erzeugen und Panel zuweisen ohne 100 Mal nen Button erstellen zu müssen
            }
        }
    }


}

