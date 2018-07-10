package battleship.model;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    public List<Coordinates> locations;
    public List<Coordinates> hits = new ArrayList<Coordinates>();
    public Integer length;
    public String name;
    public Boolean sunken;

    public Ship(String name, Integer length) {
        this.length = length;
        this.name = name;
    }

    public Boolean prefSunkenStatus() {
        if (getHits().size() == getLocations().size()) {
            return true;
        }
        return false;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Coordinates> getLocations() {
        return locations;
    }

    public void setLocations(List<Coordinates> locations) {
        this.locations = locations;
    }

    public Boolean getSunken() {
        sunken = prefSunkenStatus();
        return sunken;
    }

    public List<Coordinates> getHits() {
        return hits;
    }

    public void setHits(List<Coordinates> hits) {
        this.hits = hits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
