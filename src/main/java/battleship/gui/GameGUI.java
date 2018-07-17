package battleship.gui;

import battleship.gameplay.GameState;
import battleship.helper.Helper;
import battleship.model.Coordinates;
import battleship.model.ShotEvent;
import battleship.player.Computer;
import battleship.player.Human;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;


public class GameGUI {

    public JPanel topLevelPanel;
    int timeout_ms = 1000;
    private GameState state;
    private Computer computer;
    private Human human;
    private JPanel cards;
    private JPanel myTurn;
    private JPanel enemyTurn;
    private JButton restartButton;
    private JPanel gridPanel;
    public JButton canonButton;
    private HashMap<String, JButtonWithCoordinates> buttonList; //Hashmap to access specific buttons
    private LinkedList<JPanel> panelList;

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
    public void createComponents() {
        buttonList = new HashMap<String, JButtonWithCoordinates>();
        panelList = new LinkedList<JPanel>();

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
                if (coordinates.getX() == s.getX() && coordinates.getY() == s.getY()) {
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

    public void close() {
        System.exit(0);
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
        topLevelPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.setMaximumSize(new Dimension(1920, 1080));
        cards = new JPanel();
        cards.setLayout(new CardLayout(0, 0));
        topLevelPanel.add(cards, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        myTurn = new JPanel();
        myTurn.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        cards.add(myTurn, "Card1");
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayoutManager(11, 11, new Insets(0, 0, 0, 0), 0, 0));
        myTurn.add(gridPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("2");
        gridPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("3");
        gridPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("4");
        gridPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("5");
        gridPanel.add(label4, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("6");
        gridPanel.add(label5, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("7");
        gridPanel.add(label6, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("8");
        gridPanel.add(label7, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("9");
        gridPanel.add(label8, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("10");
        gridPanel.add(label9, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("A");
        gridPanel.add(label10, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("B");
        gridPanel.add(label11, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("C");
        gridPanel.add(label12, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("D");
        gridPanel.add(label13, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("E");
        gridPanel.add(label14, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("F");
        gridPanel.add(label15, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("G");
        gridPanel.add(label16, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("H");
        gridPanel.add(label17, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("I");
        gridPanel.add(label18, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("J");
        gridPanel.add(label19, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("1");
        gridPanel.add(label20, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        myTurn.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(200, -1), null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        myTurn.add(panel1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        restartButton = new JButton();
        restartButton.setLabel("");
        restartButton.setText("");
        panel1.add(restartButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        canonButton = new JButton();
        canonButton.setName("canon");
        canonButton.setText("Canon");
        panel2.add(canonButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
}

