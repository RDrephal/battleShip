package ship;

public class Cruiser extends Ship {
    public Cruiser(String x, String y, Boolean directionX) {
        super(x, y, directionX);
        length = 4;
    }

    @Override
    String getTypeName() {
        return "Cruiser";
    }

    @Override
    boolean sunken() {
        if (hits >= length){
            return true;
        }
        return false;
    }
}
