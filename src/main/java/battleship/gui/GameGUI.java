package battleship.gui;


import battleship.gameplay.GameState;
import battleship.helper.AlexaResponseHelper;
import battleship.helper.Helper;
import battleship.model.Coordinates;
import battleship.model.ShotEvent;
import battleship.player.Computer;
import battleship.player.Human;
import com.intellij.uiDesigner.core.GridConstraints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class GameGUI implements MouseMotionListener, MouseListener {

    public JPanel topLevelPanel;
    private int timeout_ms = 1000;
    private GameState state;
    private Computer computer;
    private Human human;
    private JPanel cards;
    private JPanel myTurn;
    private JPanel enemyTurn;
    private JButton restartButton;
    private JPanel gridPanel;

    private JTextPane gestureTupelPane;
    private JButton canon;
    private HashMap<String, JButtonWithCoordinates> buttonList; //Hashmap to access specific buttons
    private LinkedList<JPanel> panelList;
    private LinkedList<Point> timeseries;
    private long lastTime;
    private long clicktime;
    private LinkedList<Integer> angles;
    private LinkedList<LinkedList<int[]>> allNumberTemplates;
    private LinkedList<LinkedList<int[]>> allStringTemplates;
    private Coordinates gestureTupel;

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
        timeseries = new LinkedList<>();
        angles = new LinkedList<>();
        allNumberTemplates = new LinkedList<>();
        allStringTemplates = new LinkedList<>();
        createTemplates();

        gestureTupel = new Coordinates(null, null);
        gestureTupelPane.setVisible(true);
        updateTupel(-1);


        setUpGame();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Schiffe Versenken");
        GameGUI game = new GameGUI();
        Helper.setActiveGame(game);
        frame.setContentPane(game.topLevelPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void setUpGame() {
        Helper.setCurrentPlayerEvent(ShotEvent.WAIT);
        Helper.setCurrentComputerEvent(ShotEvent.WAIT);
        computer = new Computer();
        human = new Human();

        state = GameState.PLAYER_TURN;
    }

    //The dynamically generated elements of the UI are created
    private void createComponents() {
        buttonList = new HashMap<>();
        panelList = new LinkedList<>();

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                String coordString = String.valueOf(j) + String.valueOf(i);
                JButtonWithCoordinates genericButton = new JButtonWithCoordinates(new Coordinates(Helper.toAlpha(j), i));
                JButtonWithCoordinatesFactory.addJButtons(genericButton);
                genericButton.addActionListener(new ActionListener() {  //Listener for the buttons of the game field

                    public void actionPerformed(ActionEvent e) {
                        //Do the stuff
                        System.out.println(Helper.getActiveGame().topLevelPanel.getMousePosition());
                        System.out.println("Aktion ausgelöst");
                        JButtonWithCoordinates jb = (JButtonWithCoordinates) e.getSource();
                        System.out.println(jb.getXValue() + " " + jb.getYValue());

                        // Player Turn
                        Coordinates shotCoords = new Coordinates(jb.getXValue(), jb.getYValue());
                        ShotEvent eventHuman = human.fire(computer.getPlayerboard(), shotCoords);

                        if (eventHuman == ShotEvent.HIT) {
                            jb.hit(eventHuman);
                            Helper.setCurrentPlayerEvent(eventHuman);
                        } else if (eventHuman == ShotEvent.DESTROYED) {
                            jb.hit(eventHuman);
                            List<Coordinates> currentShipCoordinates = human.getCurrentShip().getHits();
                            showSunkenShip(currentShipCoordinates);
                            Helper.setCurrentPlayerEvent(eventHuman);
                            createJOptionPane("You destroy a ship");

                        } else if (eventHuman == ShotEvent.WINNER) {
                            Helper.setCurrentPlayerEvent(eventHuman);
                            jb.hit(eventHuman);

                            createJOptionPane("You Won. Restart game? I'm restarting the Game now!");
                            resetBoard();
                            setUpGame();
                            System.out.println("You won the game");
                            return;
                        } else {
                            Helper.setCurrentPlayerEvent(eventHuman);
                            jb.noHit(eventHuman);
                        }

                        // Computer Turn
                        ShotEvent eventComputer = computer.fire(human.getPlayerboard());
                        computer.feedback(eventComputer);

                        if (eventComputer == ShotEvent.HIT) {

                            Helper.setCurrentComputerEvent(eventComputer);
                            createJOptionPane("One of your ships was hit");
                        } else if (eventComputer == ShotEvent.DESTROYED) {
                            Helper.setCurrentComputerEvent(eventComputer);
                            createJOptionPane("Your opponent destroyed a ship");
                        } else if (eventComputer == ShotEvent.WINNER) {
                            Helper.setCurrentComputerEvent(eventComputer);
                            createJOptionPane("You Loose. Restart game? I'm restarting the Game now!");
                            resetBoard();
                            setUpGame();
                            return;
                        } else {
                            Helper.setCurrentComputerEvent(eventComputer);
                            System.out.println("Lucky you");
                        }

                    }
                });
                buttonList.put(coordString, genericButton);
                panelList.add(new JPanel()); //The Buttons will be added to the panels (they fill the panels)
            }
        }
    }

    private void createJOptionPane(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Title");
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                Timer timer = new Timer(timeout_ms, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        dialog.setVisible(true);
    }

    private void showSunkenShip(List<Coordinates> currentShipCoordinates) {
        for (Coordinates s : currentShipCoordinates) {
            for (String bk : buttonList.keySet()) {
                Coordinates coordinates = buttonList.get(bk).getCoords();
                if (coordinates.getX().equals(s.getX()) && coordinates.getY().equals(s.getY())) {
                    buttonList.get(bk).setColorRed();
                }
            }
        }
    }

    //The dynamically created UI elements get added to the GUI
    private void addComponents() {

        int index = 0;

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {

                if (index < buttonList.size() && index < panelList.size()) {
                    JPanel border = panelList.get(index);
                    border.setVisible(true);
                    border.setLayout(new BorderLayout(0, 0));
                    gridPanel.add(border, new GridConstraints(i, j, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

                    JButtonWithCoordinates button = buttonList.get(String.valueOf(j) + String.valueOf(i));
                    button.setVisible(true);
                    button.setPreferredSize(new Dimension(50, 50));
                    border.add(button, BorderLayout.CENTER);

                    index++;
                }
            }
        }
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }

    //The buttons on the game field are resetted to their default appearance
    private void resetButtons() {
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



    public void mouseDragged(MouseEvent e) {

        long time = System.currentTimeMillis();
        if (timeseries.isEmpty()) {
            timeseries.add(new Point(e.getX(), e.getY()));
            lastTime = time;
        } else {
            int difx = Math.abs((int) timeseries.getLast().getX() - e.getX());
            int dify = Math.abs((int) timeseries.getLast().getY() - e.getY());
            double euclid = Math.sqrt(difx ^ 2 + dify ^ 2);

            if (((time - lastTime) > 50 && euclid > 3)) {
                timeseries.add(new Point(e.getX(), e.getY()));
                lastTime = time;
            }
        }

    }

    private void determineGesture() {
        if (timeseries.size() < 4) {
            System.out.println("Geste zu kurz!");
        } else {
            calculateAngles();
            Object value = compareTemplates();
            //standardize();
            timeseries.clear();
            angles.clear();

            updateTupel(value);
        }
    }

    private void updateTupel(Object value) {

        if (value instanceof String) {
            if (gestureTupel.getX() == null) {
                gestureTupel.setX((String) value);

                gestureTupelPane.setText("Gesture recognition: (" + gestureTupel.getX() + ", _)");
            }
        } else {
            if (value instanceof Integer) {
                if ((Integer) value < 0) {
                    gestureTupelPane.setText("Gesture recognition: (_, _)");
                } else {
                    if (gestureTupel.getX() != null) {
                        gestureTupel.setY((Integer) value);
                        gestureTupelPane.setText("Gesture recognition: (" + gestureTupel.getX() + ", " + gestureTupel.getY() + ")");
                    }
                    System.out.println("Gesture: " + gestureTupel.getX() + gestureTupel.getY());

                    JButtonWithCoordinates jb = JButtonWithCoordinatesFactory.getJButton(gestureTupel.getX(), gestureTupel.getY());

                    String playerEvent = "";

                    if (jb != null) {
                        String[] response = AlexaResponseHelper.getShotResponse(jb, playerEvent);
                    }

                    gestureTupel.setX(null);
                    gestureTupel.setY(null);

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                TimeUnit.SECONDS.sleep(5);
                                if (gestureTupel.getX() == null) {
                                    gestureTupelPane.setText("Gesture recognition: (_, _)");
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }

    }

    private void calculateAngles() {
        Point one = timeseries.getFirst();
        for (int i = 0; i < timeseries.size(); i++) {
            if (i > 1) {
                Point two = timeseries.get(i - 1);
                Point three = timeseries.get(i);

                Line2D.Double twoOne = new Line2D.Double(two.getX(), two.getY(), one.getX(), one.getY());
                Line2D.Double twoThree = new Line2D.Double(two.getX(), two.getY(), three.getX(), three.getY());

                double angle1 = Math.atan2(twoOne.getY1() - twoOne.getY2(),
                        twoOne.getX1() - twoOne.getX2());
                double angle2 = Math.atan2(twoThree.getY1() - twoThree.getY2(),
                        twoThree.getX1() - twoThree.getX2());
                double angle = Math.toDegrees(angle1 - angle2);
                if (angle < 0) angle += 360;

                angles.add((int) angle);
            }
        }

    }

    private void createTemplates() {
        //The Templates for the Numbers 1-10

        LinkedList<int[]> templates1 = new LinkedList<>();
        int[] a = {173, 177, 32, 34, 145, 159, 161, 150, 165};
        int[] b = {188, 164, 46, 33, 173, 170};
        int[] c = {182, 178, 26, 25, 88, 143, 162, 163, 164, 165};
        int[] d = {173, 173, 33, 22, 75, 147, 161};
        int[] e = {168, 27, 23, 56, 145, 149, 166};
        int[] f = {182, 28, 67, 158, 168};
        templates1.add(a);
        templates1.add(b);
        templates1.add(c);
        templates1.add(d);
        templates1.add(e);
        templates1.add(f);

        LinkedList<int[]> templates2 = new LinkedList<>();
        int[] a2 = {145, 106, 92, 98, 101, 106, 334, 306, 239, 197};
        int[] b2 = {151, 138, 96, 73, 78, 103, 319, 271, 220};
        int[] c2 = {141, 132, 88, 61, 103, 112, 103, 102, 98, 102, 118, 359, 309, 258, 211, 164, 168};
        int[] d2 = {145, 110, 75, 110, 133, 333, 302, 264, 213, 198};
        int[] e2 = {147, 114, 60, 85, 118, 331, 314, 259, 205, 182};
        int[] f2 = {117, 82, 108, 119, 127, 321, 284, 231, 181};
        templates2.add(a2);
        templates2.add(b2);
        templates2.add(c2);
        templates2.add(d2);
        templates2.add(e2);
        templates2.add(f2);

        LinkedList<int[]> templates3 = new LinkedList<>();
        int[] a3 = {138, 120, 95, 75, 75, 277, 239, 189, 142, 97, 89};
        int[] b3 = {148, 121, 100, 69, 69, 32, 268, 242, 197, 159, 132, 78, 69, 51};
        int[] c3 = {140, 117, 79, 49, 67, 242, 187, 143, 88, 74, 55};
        int[] d3 = {140, 120, 60, 77, 114, 309, 266, 192, 158, 100, 75, 79, 67};
        int[] e3 = {128, 109, 74, 71, 96, 295, 261, 201, 149, 98, 80, 75};
        int[] f3 = {149, 119, 50, 47, 79, 270, 220, 176, 100, 92, 76, 59, 50};
        templates3.add(a3);
        templates3.add(b3);
        templates3.add(c3);
        templates3.add(d3);
        templates3.add(e3);
        templates3.add(f3);

        LinkedList<int[]> templates4 = new LinkedList<>();
        int[] a4 = {180, 175, 176, 319, 305, 276, 268, 30, 28, 79, 134};
        int[] b4 = {180, 180, 180, 311, 259, 288, 285, 19, 22, 88, 136, 148, 141};
        int[] c4 = {179, 179, 323, 323, 284, 290, 23, 31, 101, 133};
        int[] d4 = {180, 179, 331, 332, 302, 290, 31, 62};
        int[] e4 = {181, 181, 311, 325, 286, 271, 17, 121, 161};
        int[] f4 = {180, 180, 180, 165, 317, 269, 249, 356, 357, 188, 183};
        templates4.add(a4);
        templates4.add(b4);
        templates4.add(c4);
        templates4.add(d4);
        templates4.add(e4);
        templates4.add(f4);

        LinkedList<int[]> templates5 = new LinkedList<>();
        int[] a5 = {176, 168, 273, 268, 235, 228, 332, 298, 233, 187, 152, 93, 87, 67, 51};
        int[] b5 = {183, 188, 300, 278, 244, 229, 226, 330, 311, 265, 209, 179, 129, 104, 83};
        int[] c5 = {180, 261, 260, 249, 240, 334, 302, 215, 166, 148, 99, 82};
        int[] d5 = {178, 273, 268, 236, 221, 219, 308, 291, 228, 189, 159, 105, 90, 78, 47};
        int[] e5 = {177, 297, 278, 241, 303, 285, 216, 157, 111, 80, 53};
        int[] f5 = {180, 270, 280, 241, 295, 311, 251, 188, 167, 127, 91, 86};
        templates5.add(a5);
        templates5.add(b5);
        templates5.add(c5);
        templates5.add(d5);
        templates5.add(e5);
        templates5.add(f5);

        LinkedList<int[]> templates6 = new LinkedList<>();
        int[] a6 = {211, 216, 215, 248, 255, 278, 327, 350, 29, 63, 100, 153, 185};
        int[] b6 = {211, 228, 237, 235, 247, 281, 289, 325, 347, 17, 62, 107, 149};
        int[] c6 = {204, 229, 227, 258, 280, 291, 332, 354, 18, 60, 93, 131, 190, 189};
        int[] d6 = {208, 202, 203, 269, 280, 290, 339, 336, 344, 40, 97, 139, 173};
        int[] e6 = {214, 215, 219, 271, 281, 315, 349, 39, 95, 148, 174, 176};
        int[] f6 = {209, 235, 262, 294, 302, 333, 18, 60, 131, 171, 213};
        templates6.add(a6);
        templates6.add(b6);
        templates6.add(c6);
        templates6.add(d6);
        templates6.add(e6);
        templates6.add(f6);

        LinkedList<int[]> templates7 = new LinkedList<>();
        int[] a7 = {179, 177, 182, 182, 125, 53, 121, 151, 165, 162};
        int[] b7 = {177, 167, 77, 47, 88, 125, 129};
        int[] c7 = {183, 182, 118, 56, 92, 146, 154};
        int[] d7 = {180, 59, 56, 126, 143, 155};
        int[] e7 = {180, 111, 59, 141, 147};
        int[] f7 = {177, 90, 48, 138, 153};
        templates7.add(a7);
        templates7.add(b7);
        templates7.add(c7);
        templates7.add(d7);
        templates7.add(e7);
        templates7.add(f7);

        LinkedList<int[]> templates8 = new LinkedList<>();
        int[] a8 = {196, 226, 243, 292, 276, 229, 174, 109, 73, 51, 9, 358, 329, 308, 314, 289};
        int[] b8 = {181, 255, 258, 300, 293, 210, 144, 107, 73, 52, 354, 336, 323, 327, 311, 349, 202};
        int[] c8 = {194, 226, 305, 225, 154, 87, 87, 64, 14, 350, 326, 251, 338};
        int[] d8 = {237, 269, 274, 238, 166, 111, 73, 41, 4, 331, 299, 312, 304, 338};
        int[] e8 = {219, 256, 267, 278, 237, 178, 116, 90, 79, 24, 5, 326, 319, 311, 321, 349};
        int[] f8 = {202, 244, 240, 272, 296, 236, 171, 138, 106, 83, 45, 12, 342, 333, 320, 317, 309};
        templates8.add(a8);
        templates8.add(b8);
        templates8.add(c8);
        templates8.add(d8);
        templates8.add(e8);
        templates8.add(f8);

        LinkedList<int[]> templates9 = new LinkedList<>();
        int[] a9 = {206, 258, 257, 256, 304, 311, 340, 353, 340, 45, 166, 177, 167, 121, 79, 63};
        int[] b9 = {221, 257, 259, 308, 324, 347, 329, 74, 157, 160, 100, 95, 65};
        int[] c9 = {240, 236, 299, 317, 318, 289, 85, 118, 162, 126, 83, 66, 42};
        int[] d9 = {215, 249, 254, 312, 320, 324, 289, 65, 138, 163, 164, 133, 104, 86, 54};
        int[] e9 = {213, 253, 271, 320, 337, 332, 259, 56, 122, 165, 144, 97, 64, 53};
        int[] f9 = {234, 269, 289, 311, 318, 295, 43, 110, 161, 92, 74, 63, 54};
        templates9.add(a9);
        templates9.add(b9);
        templates9.add(c9);
        templates9.add(d9);
        templates9.add(e9);
        templates9.add(f9);

        LinkedList<int[]> templates10 = new LinkedList<>();
        int[] a10 = {207, 226, 230, 258, 274, 270, 320, 323, 324, 322, 280, 241};
        int[] b10 = {205, 247, 253, 286, 292, 279, 308, 318, 302, 309, 278, 242};
        int[] c10 = {210, 236, 239, 269, 268, 292, 304, 316, 324, 315, 298, 286, 293, 262};
        int[] d10 = {217, 225, 234, 263, 273, 296, 322, 331, 344, 325, 268, 223};
        int[] e10 = {217, 235, 254, 276, 262, 309, 323, 315, 324, 281, 280};
        int[] f10 = {220, 248, 245, 264, 254, 301, 338, 333, 293, 289, 267};
        templates10.add(a10);
        templates10.add(b10);
        templates10.add(c10);
        templates10.add(d10);
        templates10.add(e10);
        templates10.add(f10);

        allNumberTemplates.add(templates1);
        allNumberTemplates.add(templates2);
        allNumberTemplates.add(templates3);
        allNumberTemplates.add(templates4);
        allNumberTemplates.add(templates5);
        allNumberTemplates.add(templates6);
        allNumberTemplates.add(templates7);
        allNumberTemplates.add(templates8);
        allNumberTemplates.add(templates9);
        allNumberTemplates.add(templates10);

        //The Templates for the letters a-j
        LinkedList<int[]> templatesA = new LinkedList<>();
        int[] g = {217, 257, 245, 264, 308, 339, 347, 106, 151};
        int[] h = {190, 258, 253, 302, 317, 355, 26, 127, 335, 343, 202};
        int[] i = {199, 255, 269, 283, 299, 335, 302, 244, 56, 66, 158, 171};
        int[] j = {240, 264, 279, 305, 300, 342, 355, 192, 177, 357, 17, 159};
        int[] k = {205, 271, 280, 261, 300, 301, 332, 324, 258, 55, 112, 169};
        int[] l = {206, 257, 266, 298, 308, 332, 325, 271, 61, 88, 181};
        templatesA.add(g);
        templatesA.add(h);
        templatesA.add(i);
        templatesA.add(j);
        templatesA.add(k);
        templatesA.add(l);

        LinkedList<int[]> templatesB = new LinkedList<>();
        int[] g2 = {183, 183, 179, 351, 344, 272, 237, 165, 128, 90, 78, 90};
        int[] h2 = {185, 358, 353, 280, 146, 135, 98, 101, 64, 56, 60};
        int[] i2 = {180, 0, 357, 324, 262, 182, 150, 124, 91, 74, 67};
        int[] j2 = {180, 180, 10, 357, 331, 213, 168, 128, 115, 102, 98, 84};
        int[] k2 = {180, 5, 353, 285, 232, 160, 118, 83, 62};
        int[] l2 = {183, 183, 355, 308, 228, 155, 104, 74, 32};
        templatesB.add(g2);
        templatesB.add(h2);
        templatesB.add(i2);
        templatesB.add(j2);
        templatesB.add(k2);
        templatesB.add(l2);

        LinkedList<int[]> templatesC = new LinkedList<>();
        int[] g3 = {209, 259, 236, 276, 257, 279};
        int[] h3 = {203, 232, 240, 244, 275, 269, 280, 279, 280};
        int[] i3 = {202, 251, 250, 256, 287, 295, 283, 274};
        int[] j3 = {205, 233, 246, 245, 294, 292, 259};
        int[] k3 = {208, 235, 243, 267, 272, 278, 282, 301};
        int[] l3 = {204, 232, 233, 268, 279, 262};
        templatesC.add(g3);
        templatesC.add(h3);
        templatesC.add(i3);
        templatesC.add(j3);
        templatesC.add(k3);
        templatesC.add(l3);

        LinkedList<int[]> templatesD = new LinkedList<>();
        int[] g4 = {223, 245, 261, 272, 268, 355, 237, 194, 358, 3, 175, 168};
        int[] h4 = {236, 256, 266, 303, 317, 283, 230, 7, 10, 41, 140};
        int[] i4 = {236, 252, 298, 308, 335, 336, 226, 212, 210, 353, 1, 178};
        int[] j4 = {242, 250, 276, 323, 328, 216, 237, 13, 16, 147, 169};
        int[] k4 = {227, 271, 288, 314, 346, 268, 213, 341, 12, 145};
        int[] l4 = {239, 257, 285, 332, 327, 214, 354, 10, 179};
        templatesD.add(g4);
        templatesD.add(h4);
        templatesD.add(i4);
        templatesD.add(j4);
        templatesD.add(k4);
        templatesD.add(l4);

        LinkedList<int[]> templatesE = new LinkedList<>();
        int[] g5 = {267, 278, 256, 272, 284, 279, 303, 284, 265};
        int[] h5 = {226, 240, 226, 264, 263, 276, 301, 315, 302, 300, 275, 249};
        int[] i5 = {232, 226, 241, 277, 277, 255, 282, 281, 312, 296, 266, 255, 241};
        int[] j5 = {255, 244, 279, 271, 239, 286, 277, 295, 294, 259, 241};
        int[] k5 = {236, 251, 265, 266, 305, 303, 300, 323, 276, 254, 251};
        int[] l5 = {245, 258, 276, 283, 264, 287, 289, 305, 296, 239, 223};
        templatesE.add(g5);
        templatesE.add(h5);
        templatesE.add(i5);
        templatesE.add(j5);
        templatesE.add(k5);
        templatesE.add(l5);

        LinkedList<int[]> templatesF = new LinkedList<>();
        int[] g6 = {213, 240, 265, 306, 342, 359, 6, 55, 165};
        int[] h6 = {198, 231, 229, 290, 305, 4, 16, 90, 149, 156};
        int[] i6 = {211, 237, 232, 271, 287, 346, 5, 43, 165, 174};
        int[] j6 = {207, 221, 221, 257, 280, 329, 352, 5, 26, 167};
        int[] k6 = {223, 234, 256, 274, 289, 347, 340, 231, 189, 175, 190};
        int[] l6 = {204, 235, 245, 261, 303, 340, 7, 33, 142, 149, 149};
        templatesF.add(g6);
        templatesF.add(h6);
        templatesF.add(i6);
        templatesF.add(j6);
        templatesF.add(k6);
        templatesF.add(l6);

        LinkedList<int[]> templatesG = new LinkedList<>();
        int[] g7 = {244, 260, 297, 307, 345, 323, 77, 161, 164, 131, 121, 82, 42, 343, 317};
        int[] h7 = {233, 258, 265, 304, 311, 297, 315, 65, 152, 135, 91, 83, 20};
        int[] i7 = {237, 257, 311, 325, 331, 71, 154, 157, 101, 57, 353, 310};
        int[] j7 = {237, 237, 285, 283, 322, 312, 66, 92, 151, 115, 96, 37, 10, 297};
        int[] k7 = {228, 251, 302, 311, 316, 79, 122, 138, 117, 65, 12, 322};
        int[] l7 = {232, 305, 297, 324, 258, 35, 153, 126, 43, 3, 314};
        templatesG.add(g7);
        templatesG.add(h7);
        templatesG.add(i7);
        templatesG.add(j7);
        templatesG.add(k7);
        templatesG.add(l7);

        LinkedList<int[]> templatesH = new LinkedList<>();
        int[] g8 = {182, 180, 0, 341, 281, 190, 151, 134, 144};
        int[] h8 = {183, 3, 349, 246, 152, 113, 122, 133};
        int[] i8 = {179, 176, 356, 355, 286, 233, 160, 134, 142};
        int[] j8 = {184, 185, 354, 344, 271, 228, 169, 144, 131};
        int[] k8 = {178, 339, 337, 240, 180, 138, 128};
        int[] l8 = {180, 351, 300, 220, 137, 136};
        templatesH.add(g8);
        templatesH.add(h8);
        templatesH.add(i8);
        templatesH.add(j8);
        templatesH.add(k8);
        templatesH.add(l8);

        LinkedList<int[]> templatesI = new LinkedList<>();
        int[] g9 = {179, 178, 181, 181, 181, 181, 180, 180, 180};
        int[] h9 = {180, 180, 180, 180, 180, 180};
        int[] i9 = {178, 177, 180, 179, 182};
        int[] j9 = {180, 176, 179, 181, 181, 181};
        int[] k9 = {180, 180, 180, 180, 180, 189, 188, 177, 178, 178};
        int[] l9 = {181, 171, 171, 177, 175};
        templatesI.add(g9);
        templatesI.add(h9);
        templatesI.add(i9);
        templatesI.add(j9);
        templatesI.add(k9);
        templatesI.add(l9);

        LinkedList<int[]> templatesJ = new LinkedList<>();
        int[] g10 = {208, 29, 39, 160, 146, 105, 81, 33, 1};
        int[] h10 = {219, 216, 28, 30, 130, 150, 119, 98, 57, 20};
        int[] i10 = {214, 229, 38, 65, 142, 91, 70, 28};
        int[] j10 = {213, 225, 218, 34, 41, 111, 137, 124, 94, 64, 38};
        int[] k10 = {228, 229, 42, 63, 147, 101, 74, 44, 17};
        int[] l10 = {224, 237, 38, 47, 137, 140, 98, 42, 42, 321};
        templatesJ.add(g10);
        templatesJ.add(h10);
        templatesJ.add(i10);
        templatesJ.add(j10);
        templatesJ.add(k10);
        templatesJ.add(l10);


        allStringTemplates.add(templatesA);
        allStringTemplates.add(templatesB);
        allStringTemplates.add(templatesC);
        allStringTemplates.add(templatesD);
        allStringTemplates.add(templatesE);
        allStringTemplates.add(templatesF);
        allStringTemplates.add(templatesG);
        allStringTemplates.add(templatesH);
        allStringTemplates.add(templatesI);
        allStringTemplates.add(templatesJ);

    }

    //ZEitreihe soll mit templates verglichen werden
    private Object compareTemplates() {
        int min = Integer.MAX_VALUE;
        Object index = -1;

        if (gestureTupel.getX() == null) {
            for (int i = 0; i < allStringTemplates.size(); i++) {
                for (int[] template : allStringTemplates.get(i)) {
                    int minTemp = dtw(angles, template);
                    if (minTemp < min) {
                        min = minTemp;
                        index = Helper.toAlpha(i + 1);
                    }
                }

            }
        } else {
            for (int i = 0; i < allNumberTemplates.size(); i++) {
                for (int[] template : allNumberTemplates.get(i)) {
                    int minTemp = dtw(angles, template);
                    if (minTemp < min) {
                        min = minTemp;
                        index = i + 1;
                    }
                }

            }
        }


        //System.out.println(angles);
        System.out.println("Minimum distance: " + min + "  |  Identified character: " + index);

        return index;
    }

    //Distanz der Zeitreihen soll hier berechnet werden. Rekursiv
    private int dtw(LinkedList<Integer> angles, int[] template) {

        Integer series[] = new Integer[angles.size()];
        series = angles.toArray(series);

        int[][] costs = new int[series.length][template.length];

        costs[0][0] = distance(series[0], template[0]);

        for (int i = 1; i < series.length; i++) {
            costs[i][0] = costs[i - 1][0] + distance(series[i], template[0]);
        }

        for (int j = 1; j < template.length; j++) {
            costs[0][j] = costs[0][j] + distance(series[0], template[j]);
        }

        for (int i = 1; i < series.length; i++) {
            for (int j = 1; j < template.length; j++) {
                costs[i][j] = Helper.min(costs[i - 1][j], costs[i][j - 1], costs[i - 1][j - 1]) + distance(series[i], template[j]);
            }

        }

        return costs[series.length - 1][template.length - 1];
    }

    private int distance(int i, int j) {
        int distance;
//        if (Math.abs(i-j)<Math.PI){
//            distance =  (int)(1/Math.PI)*Math.abs(i-j);
//        }else{
//            distance =  (int)((1/Math.PI)*(2*Math.PI-Math.abs(i-j)));
//        }
        distance = Math.abs(i - j);
        return distance;
    }


    //Ungenutzt... Alternativer Ansatz
    public void standardize() {
        double minx = timeseries.getFirst().getX();
        double miny = timeseries.getFirst().getY();

        double maxx = timeseries.getFirst().getX();
        double maxy = timeseries.getFirst().getY();

        for (int i = 1; i < timeseries.size(); i++) {
            if (timeseries.get(i).getX() < minx) {
                minx = timeseries.get(i).getX();
            }
            if (timeseries.get(i).getX() > maxx) {
                maxx = timeseries.get(i).getX();
            }

            //y-Werte vertauscht, da y-wert klein ist wenn die mouse oben im Fenster ist
            if (timeseries.get(i).getY() < maxy) {
                maxy = timeseries.get(i).getY();
            }
            if (timeseries.get(i).getX() > miny) {
                miny = timeseries.get(i).getX();
            }

        }

        for (int i = 0; i < timeseries.size(); i++) {
            double xvalue = timeseries.get(i).getX();
            double yvalue = timeseries.get(i).getY();

            //Werte auf einer Skala von 0-20
            double newxvalue = ((xvalue - minx) * 20) / (maxx - minx);
            double newyvalue = ((yvalue - miny) * 20) / (maxy - miny);
            timeseries.get(i).setLocation(newxvalue, newyvalue);
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

    //Entscheidung ob eine Geste ausgeführt wurde
    public void mouseReleased(MouseEvent e) {
        long time = System.currentTimeMillis();
        if ((time - clicktime) > 500) {
            System.out.println("Geste");
            determineGesture();
        } else {
            System.out.println("Keine Geste!");
            timeseries.clear();
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

}


