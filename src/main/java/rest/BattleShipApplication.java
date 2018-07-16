package rest;

import battleship.gui.GameGUI;
import battleship.helper.Helper;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.swing.*;

public class BattleShipApplication extends Application<BattleShipConfiguration> {

    public static void main(String[] args) throws Exception {
        new BattleShipApplication().run(args);

        JFrame frame = new JFrame("Schiffe Versenken");
        GameGUI game = new GameGUI();
        Helper.setActiveGame(game);
        frame.setContentPane(game.topLevelPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public String getName() {
        return "battle-ship";
    }

    @Override
    public void initialize(Bootstrap<BattleShipConfiguration> bootstrap) {

    }

    @Override
    public void run(BattleShipConfiguration battleShipConfiguration, Environment environment) throws Exception {
        final ShotResource resource = new ShotResource();
        final NewStartResource newstart = new NewStartResource();
        final ShotResourceOnPosition shotResourceOnPosition = new ShotResourceOnPosition();
        final CanonPosition canonPosition = new CanonPosition();
        environment.jersey().register(resource);
        environment.jersey().register(newstart);
        environment.jersey().register(shotResourceOnPosition);
        environment.jersey().register(canonPosition);
    }
}
