package battleship.player;

import battleship.helper.Helper;
import battleship.model.Coordinates;
import battleship.model.Playerboard;
import battleship.model.Ship;
import battleship.model.ShotEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Computer extends Player {

    private List<Coordinates> firing;
    private List<Coordinates> currentShip = new ArrayList<>();
    private List<Coordinates> cardinaldirection = new ArrayList<>();
    private Integer horizontal;


    public Computer() {
        super();
        firing = new ArrayList<Coordinates>();

        for (String s : Helper.Alphabet) {
            for (Integer i : Helper.CoordinateY) {
                firing.add(new Coordinates(s, i));
            }
        }
        setFiring(firing);
    }

    /**
     * Computer needs randomly fire shots
     *
     * @param enemy
     * @return ShotEvent
     */
    public ShotEvent fire(Playerboard enemy) {
        //Mögliche Schüsse in der Liste listFire. Vorerst  nur Random Coordinates
        Random r = new Random();
        List<Coordinates> listFire = getFiring();
        ShotEvent shotEvent;
        if (currentShip.isEmpty()) {

            int listSize = getFiring().size();

            int shotIndex = r.nextInt(listSize);
            Coordinates shot = listFire.get(shotIndex);
            listFire.remove(shotIndex);
            setFiring(listFire);
            shotEvent = getShotEvent(enemy, shot);
            if (shotEvent == ShotEvent.HIT) {
                currentShip.add(shot);
                Integer x = Helper.alphaToInt(shot.getX());
                Integer y = shot.getY();
                if (x > 1) {
                    cardinaldirection.add(new Coordinates(Helper.toAlpha(x - 1), y));
                } else cardinaldirection.add(null);
                if (x < 10) {
                    cardinaldirection.add(new Coordinates(Helper.toAlpha(x + 1), y));
                } else cardinaldirection.add(null);
                if (y > 1) {
                    cardinaldirection.add(new Coordinates(Helper.toAlpha(x), y - 1));
                } else cardinaldirection.add(null);
                if (y < 10) {
                    cardinaldirection.add(new Coordinates(Helper.toAlpha(x), y + 1));
                } else cardinaldirection.add(null);
            }
        } else {
            Integer shotIndex = null;
            for (int i = 0; i <= 3; i++) {
                Coordinates c = cardinaldirection.get(i);
                if (c != null && -1 != indexOfValues(listFire, c)) {
                    shotIndex = i;
                    break;
                }
            }

            if ((cardinaldirection.get(0) != null || cardinaldirection.get(1) != null || cardinaldirection.get(2) != null || cardinaldirection.get(3) != null) && shotIndex != null) {
                shotEvent = getShotEvent(enemy, cardinaldirection.get(shotIndex));
                int place = indexOfValues(listFire, cardinaldirection.get(shotIndex));
                if (place != -1) {
                    listFire.remove(place);
                    setFiring(listFire);
                }
                if (shotEvent == ShotEvent.HIT) {
                    horizontal = shotIndex;
                    if (0 != shotIndex) cardinaldirection.set(0, null);
                    if (1 != shotIndex) cardinaldirection.set(1, null);
                    if (2 != shotIndex) cardinaldirection.set(2, null);
                    if (3 != shotIndex) cardinaldirection.set(3, null);
                    Coordinates currentShot = cardinaldirection.get(shotIndex);
                    Integer x = Helper.alphaToInt(currentShot.getX());
                    Integer y = currentShot.getY();

                    switch (horizontal) {
                        case 0:
                            if (x > 1) {
                                cardinaldirection.set(shotIndex, new Coordinates(Helper.toAlpha(x - 1), y));
                            } else cardinaldirection.set(0, null);
                            break;
                        case 1:
                            if (x < 10) {
                                cardinaldirection.set(shotIndex, new Coordinates(Helper.toAlpha(x + 1), y));
                            } else cardinaldirection.set(1, null);
                            break;
                        case 2:
                            if (y > 1) {
                                cardinaldirection.set(shotIndex, new Coordinates(Helper.toAlpha(x), y - 1));
                            } else cardinaldirection.set(2, null);
                            break;
                        case 3:
                            if (y < 10) {
                                cardinaldirection.set(shotIndex, new Coordinates(Helper.toAlpha(x), y + 1));
                            } else cardinaldirection.set(3, null);
                            break;
                    }
                } else {
                    cardinaldirection.set(shotIndex, null);
                }
            } else {
                Coordinates currentShot = currentShip.get(0);
                Integer x = Helper.alphaToInt(currentShot.getX());
                Integer y = currentShot.getY();
                switch (horizontal) {
                    case 0:
                        if (x < 10) currentShip.add(0, new Coordinates(Helper.toAlpha(x + 1), y));
                        break;
                    case 1:
                        if (x > 1) currentShip.add(0, new Coordinates(Helper.toAlpha(x - 1), y));
                        break;
                    case 2:
                        if (y < 10) currentShip.add(0, new Coordinates(Helper.toAlpha(x), y + 1));
                        break;
                    case 3:
                        if (y > 1) currentShip.add(0, new Coordinates(Helper.toAlpha(x), y - 1));
                        break;
                }
                shotEvent = getShotEvent(enemy, currentShip.get(0));
                int place = indexOfValues(listFire, currentShip.get(0));
                if (place != -1) {
                    listFire.remove(place);
                    setFiring(listFire);
                }
            }
        }

        if (shotEvent == ShotEvent.DESTROYED || shotEvent == ShotEvent.WINNER) {
            deleteFiringFields(currentShip);
            currentShip.clear();
            cardinaldirection.clear();
        }

        return shotEvent;
    }

    private void deleteFiringFields(List<Coordinates> currentShip) {
        List<Coordinates> listFire = getFiring();
        for (Coordinates c : currentShip) {
            Integer x = Helper.alphaToInt(c.getX());
            Integer y = c.getY();
            if (x < 10) {
                int place = indexOfValues(listFire, new Coordinates(Helper.toAlpha(x + 1), y));
                if (place != -1) {
                    listFire.remove(place);
                    setFiring(listFire);
                }
            }
            if (x > 1) {
                int place = indexOfValues(listFire, new Coordinates(Helper.toAlpha(x - 1), y));
                if (place != -1) {
                    listFire.remove(place);
                    setFiring(listFire);
                }
            }
            if (y < 10) {
                int place = indexOfValues(listFire, new Coordinates(Helper.toAlpha(x), y + 1));
                if (place != -1) {
                    listFire.remove(place);
                    setFiring(listFire);
                }
            }
            if (y > 1) {
                int place = indexOfValues(listFire, new Coordinates(Helper.toAlpha(x), y - 1));
                if (place != -1) {
                    listFire.remove(place);
                    setFiring(listFire);
                }
            }
        }
    }


    @Override
    public ShotEvent fire(Playerboard enemy, Coordinates coordinates) {
        return null;
    }

    /**
     * Add new Ships to the Board Randomly
     *
     * @param name
     * @param length
     */
    @Override
    protected void addNewShips(String name, int length) {
        Ship ship = new Ship(name, length);
        playerboard.autoAddShipToPlayerboard(ship);
        System.out.println("Computer: " + ship.getLocations());
    }


    public void feedback(ShotEvent event) {
        //Feedback whether the computer hit or not
    }

    public List<Coordinates> getFiring() {
        return firing;
    }

    public void setFiring(List<Coordinates> firing) {
        this.firing = firing;
    }

    public Integer indexOfValues(List<Coordinates> searchableList, Coordinates values) {
        Integer i = -1;
        for (Coordinates c : searchableList) {
            i++;
            if (c.getX() == values.getX() && c.getY() == values.getY()) {
                return i;
            }
        }
        return i;
    }
}
