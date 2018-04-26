package gameplay;

import Player.*;
import model.ShotEvents;

import java.util.Random;

public class BattleShip {

    public void startGame(){
        Player human = new Human();
        Player computer = new Computer();
        ShotEvents gameOver=null;
        Random r = new Random();
        Integer beginner = r.nextInt(2);
        //Random beginner Player == 1; Computer ==0
        while(gameOver != ShotEvents.WINNER){
            if(beginner == 0) {
                gameOver = computer.fire(human.getPlayerboard(),null);
                if(gameOver ==ShotEvents.WINNER){
                    return;
                }
                gameOver = human.fire(computer.getPlayerboard(),null);
            }else{
                gameOver = human.fire(computer.getPlayerboard(),null);
                if(gameOver ==ShotEvents.WINNER){
                    return;
                }
                gameOver = computer.fire(human.getPlayerboard(),null);
            }
        }

        //Null ersetzen beim HUMAN und Ausgabe des Siegers
    }
}
