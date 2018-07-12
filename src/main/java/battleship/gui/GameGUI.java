package battleship.gui;

import battleship.gameplay.GameState;
import battleship.helper.Helper;
import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.ShotEvent;
import battleship.player.Computer;
import battleship.player.Human;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class GameGUI implements MouseMotionListener, MouseListener {

    private GameState state;
    private Computer computer;
    private Human human;
    private Playerboard boardHuman;
    private Playerboard boardComputer;

    private JPanel topLevelPanel;
    private JPanel cards;
    private JPanel myTurn;
    private JPanel enemyTurn;
    private JButton restartButton;
    private JPanel gridPanel;
    private HashMap<String, JButtonWithCoordinates> buttonList; //Hashmap to access specific buttons
    private LinkedList<JPanel> panelList;
    private LinkedList<Point> timeseries;
    private long lastTime;
    private long clicktime;
    private boolean gesture = false;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Schiffe Versenken");
        frame.setContentPane(new GameGUI().topLevelPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public GameGUI() {
        // TODO: add player board
        // easiest would be just as Strings that get redrawn every turn
        // like:
        // 0 = = 0 0 0
        // = 0 0 X = 0
        // 0 0 0 0 0 =
        // = = X 0 0 =
        // 0 0 0 0 0 0
        // 0 = = = X 0

        restartButton.setName("restartButton");
        restartButton.setText("Restart");

        createComponents();
        addComponents();

        restartButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resetBoard();
                setUpGame();
            }
        });

        //Mouse tracker
        topLevelPanel.addMouseMotionListener(this);
        topLevelPanel.addMouseListener(this);
        timeseries = new LinkedList<Point>();

        setUpGame();
    }

    public void setUpGame() {
        computer = new Computer();
        human = new Human();

        state = GameState.PLAYER_TURN;
    }

    //The dynamically generated elements of the UI are created
    public void createComponents() {
        buttonList = new HashMap<String, JButtonWithCoordinates>();
        panelList = new LinkedList<JPanel>();

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                String coordString = String.valueOf(j) + String.valueOf(i);
                JButtonWithCoordinates genericButton = new JButtonWithCoordinates(new Coordinates(Helper.toAlpha(j), i));
                genericButton.addActionListener(new ActionListener() {  //Listener for the buttons of the game field

                    public void actionPerformed(ActionEvent e) {
                        //Do the stuff
                        System.out.println("Aktion ausgelöst");
                        JButtonWithCoordinates jb = (JButtonWithCoordinates) e.getSource();
                        System.out.println(jb.getXValue() + " " + jb.getYValue());

                        // Player Turn
                        Coordinates shotCoords = new Coordinates(jb.getXValue(), jb.getYValue());
                        ShotEvent eventHuman = human.fire(computer.getPlayerboard(), shotCoords);
                        
                        if (eventHuman == ShotEvent.HIT) {
                            jb.hit();
                        } else if (eventHuman == ShotEvent.DESTROYED) {
                            jb.hit();
                            List<Coordinates> currentShipCoordinates = human.getCurrentShip().getHits();
                            showSunkenShip(currentShipCoordinates);
                            JOptionPane.showMessageDialog(null, "You destroyed a ship");
                        } else if (eventHuman == ShotEvent.WINNER){
                            jb.hit();

                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "You Won. Restart game?", "Winner", dialogButton);

                            if (dialogResult == JOptionPane.YES_OPTION) {
                                resetBoard();
                                setUpGame();
                            } else {
                                System.exit(100);
                            }

                            System.out.println("You won the game");
                        } else {
                            jb.noHit();
                        }

                        // Computer Turn
                        ShotEvent eventComputer = computer.fire(human.getPlayerboard());
                        computer.feedback(eventComputer);

                        if (eventComputer == ShotEvent.HIT) {
                            JOptionPane.showMessageDialog(null, "One of your ships was hit");
                        } else if (eventComputer == ShotEvent.DESTROYED) {
                            JOptionPane.showMessageDialog(null, "Your opponent destroyed a ship");
                        } else if (eventComputer == ShotEvent.WINNER) {
                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "You Lost. Restart game?", "Loser", dialogButton);

                            if (dialogResult == JOptionPane.YES_OPTION) {
                                resetBoard();
                                setUpGame();
                            } else {
                                System.exit(100);
                            }
                        } else {
                            System.out.println("Lucky you");
                        }

                    }
                });
                buttonList.put(coordString, genericButton);
                panelList.add(new JPanel()); //The Buttons will be added to the panels (they fill the panels)
            }
        }
    }

    private void showSunkenShip(List<Coordinates> currentShipCoordinates) {
        for (Coordinates s : currentShipCoordinates) {
            for(String bk : buttonList.keySet()) {
                Coordinates coordinates= buttonList.get(bk).getCoords();
                if (coordinates.getX()== s.getX() && coordinates.getY() == s.getY()){
                    buttonList.get(bk).setColorRed();
                }
            }
        }
    }

    //The dynamically created UI elements get added to the GUI
    public void addComponents() {

        int index = 0;

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {

                if (index < buttonList.size() && index < panelList.size()) {
                    JPanel border = panelList.get(index);
                    border.setVisible(true);
                    border.setLayout(new BorderLayout(0, 0));
                    gridPanel.add(border, new com.intellij.uiDesigner.core.GridConstraints(i, j, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

                    JButtonWithCoordinates button = buttonList.get(String.valueOf(j) + String.valueOf(i));
                    button.setVisible(true);
                    button.setPreferredSize(new Dimension(50, 50));
                    border.add(button, BorderLayout.CENTER);

                    index++;
                }
            }
        }
    }

    //The buttons on the game field are resetted to their default appearance
    public void resetButtons() {
        for (String k : buttonList.keySet()) {
            buttonList.get(k).resetButton();
        }
        topLevelPanel.revalidate();
        topLevelPanel.repaint();

    }

    //The reset-button functionalities
    public void resetBoard() {
        resetButtons();
    }



    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        topLevelPanel = new JPanel();
        topLevelPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.setMaximumSize(new Dimension(1920, 1080));
        cards = new JPanel();
        cards.setLayout(new CardLayout(0, 0));
        topLevelPanel.add(cards, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        myTurn = new JPanel();
        myTurn.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        cards.add(myTurn, "Card1");
        gridPanel = new JPanel();
        gridPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(11, 11, new Insets(0, 0, 0, 0), 0, 0));
        myTurn.add(gridPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("2");
        gridPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("3");
        gridPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("4");
        gridPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("5");
        gridPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("6");
        gridPanel.add(label5, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("7");
        gridPanel.add(label6, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("8");
        gridPanel.add(label7, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("9");
        gridPanel.add(label8, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("10");
        gridPanel.add(label9, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("A");
        gridPanel.add(label10, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("B");
        gridPanel.add(label11, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("C");
        gridPanel.add(label12, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("D");
        gridPanel.add(label13, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("E");
        gridPanel.add(label14, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("F");
        gridPanel.add(label15, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("G");
        gridPanel.add(label16, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("H");
        gridPanel.add(label17, new com.intellij.uiDesigner.core.GridConstraints(0, 8, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("I");
        gridPanel.add(label18, new com.intellij.uiDesigner.core.GridConstraints(0, 9, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("J");
        gridPanel.add(label19, new com.intellij.uiDesigner.core.GridConstraints(0, 10, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("1");
        gridPanel.add(label20, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        myTurn.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(200, -1), null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        myTurn.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        restartButton = new JButton();
        panel1.add(restartButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        enemyTurn = new JPanel();
        enemyTurn.setLayout(new GridBagLayout());
        cards.add(enemyTurn, "Card2");
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return topLevelPanel;
    }

    public void mouseDragged(MouseEvent e) {

        long time = System.currentTimeMillis();
        if(timeseries.isEmpty()){
            timeseries.add(new Point(e.getX(),e.getY()));
            lastTime = time;
        }else{
            int difx = Math.abs((int) timeseries.getLast().getX()-e.getX());
            int dify = Math.abs((int) timeseries.getLast().getY()-e.getY());
            double euclid = Math.sqrt(difx^2+dify^2);

            if(euclid > 4 && (time-lastTime)>140){
                timeseries.add(new Point(e.getX(),e.getY()));
                lastTime = time;
            }
        }

    }

    public void determineGesture(){
        if(timeseries.size()<4){
            System.out.println("Geste zu kurz!");
        }else{
            System.out.println("Geste wir nun erkannt");
            System.out.println(timeseries);
            standardize();
            timeseries.clear();
        }
    }

    public void standardize(){
       double minx = timeseries.getFirst().getX();
       double miny = timeseries.getFirst().getY();

       double maxx = timeseries.getFirst().getX();
       double maxy = timeseries.getFirst().getY();

        for(int i=1;i<timeseries.size();i++){
            if(timeseries.get(i).getX()<minx){
                minx=timeseries.get(i).getX();
            }
            if (timeseries.get(i).getX()>maxx){
                maxx = timeseries.get(i).getX();
            }

            //y-Werte vertauscht, da y-wert klein ist wenn die mouse oben im Fenster ist
            if(timeseries.get(i).getY()<maxy){
                maxy = timeseries.get(i).getY();
            }
            if (timeseries.get(i).getX()>miny){
                miny = timeseries.get(i).getX();
            }

        }

        for(int i=0;i<timeseries.size();i++){
            double xvalue = timeseries.get(i).getX();
            double yvalue = timeseries.get(i).getY();

            //Werte auf einer Skala von 0-20
            double newxvalue = ((xvalue-minx)*20)/(maxx-minx);
            double newyvalue = ((yvalue-miny)*20)/(maxy-miny);
            timeseries.get(i).setLocation(newxvalue,newyvalue);
        }
        System.out.println(timeseries);
    }

    public void mouseMoved(MouseEvent e) {
        //System.out.println("Mouse moved!");
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
         clicktime = System.currentTimeMillis();
    }

    public void mouseReleased(MouseEvent e) {
        long time = System.currentTimeMillis();
        if((time-clicktime)>500){
            gesture = true;
            System.out.println("Zeitliche differenz: "+(time-clicktime));
            System.out.println("Geste");
            determineGesture();
        }else{
            System.out.println("Keine Geste!");
            gesture = false;
            timeseries.clear();
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}

