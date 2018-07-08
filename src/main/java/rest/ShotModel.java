package rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShotModel {

    private int number;
    private String letter;

    public ShotModel() {

    }

    public ShotModel(int number, String letter) {
        this.number = number;
        this.letter = letter;
    }

    @JsonProperty
    public int getNumber() {
        return number;
    }

    @JsonProperty
    public String getLetter() {
        return letter;
    }

}
