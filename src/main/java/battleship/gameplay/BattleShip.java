package battleship.gameplay;

import battleship.model.ShotEvent;
import battleship.player.Computer;
import battleship.player.Human;
import battleship.player.Player;

import java.util.Random;

public class BattleShip {

    public void startGame() {
        Player human = new Human();
        Player computer = new Computer();
        ShotEvent gameOver = null;
        Random r = new Random();
        Integer beginner = r.nextInt(2);
        //Random beginner battleship.player == 1; Computer ==0
        while (gameOver != ShotEvent.WINNER) {
            if (beginner == 0) {
                gameOver = computer.fire(human.getPlayerboard(), null);
                if (gameOver == ShotEvent.WINNER) {
                    return;
                }
                gameOver = human.fire(computer.getPlayerboard(), null);
            } else {
                gameOver = human.fire(computer.getPlayerboard(), null);
                if (gameOver == ShotEvent.WINNER) {
                    return;
                }
                gameOver = computer.fire(human.getPlayerboard(), null);
            }
        }

        //Null ersetzen beim HUMAN und Ausgabe des Siegers
    }
}
