package model;

import java.util.Map;

public class Ship {

    public Map<String, Integer> locations;
    public Map<String, Integer> hits;
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

    public Map<String, Integer> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Integer> locations) {
        this.locations = locations;
    }

    public Boolean getSunken() {
        return sunken;
    }

    public void setSunken(Boolean sunken) {
        this.sunken = sunken;
    }

    public Map<String, Integer> getHits() {
        return hits;
    }

    public void setHits(Map<String, Integer> hits) {
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
