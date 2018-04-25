package model;

import java.util.List;
import java.util.Map;

public class Ship {

    public List<Coordinate> locations;
    public List<Coordinate> hits;
    public Integer length;
    public String name;
    public Boolean sunken;

    public Ship(String name, Integer length) {
        this.length = length;
        this.name = name;
    }

    public Integer getLength() {
        return length;
    }

    public List<Coordinate> getLocations() {
        return locations;
    }

    public void setLocations(List<Coordinate> locations) {
        this.locations = locations;
    }

    public Boolean getSunken() {
        return sunken;
    }

    public void setSunken(Boolean sunken) {
        this.sunken = sunken;
    }

    public List<Coordinate> getHits() {
        return hits;
    }

    public void setHits(List<Coordinate> hits) {
        this.hits = hits;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
