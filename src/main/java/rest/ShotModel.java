package rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShotModel {

    private int number;
    private String letter;
    private String playerEventResult;
    private String computerEventResult;

    public ShotModel() {

    }

    public ShotModel(int number, String letter, String playerEventResult, String computerEventResult) {
        this.number = number;
        this.letter = letter;
        this.playerEventResult = playerEventResult;
        this.computerEventResult = computerEventResult;
    }

    @JsonProperty
    public int getNumber() {
        return number;
    }

    @JsonProperty
    public String getLetter() {
        return letter;
    }

    @JsonProperty
    public String getPlayerEventResult() {
        return playerEventResult;
    }

    @JsonProperty
    public String getComputerEventResult() {
        return computerEventResult;
    }
}
