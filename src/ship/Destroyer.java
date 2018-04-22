package ship;

public class Destroyer extends Ship {

    public Destroyer(String x, String y, Boolean directionX) {
        super(x, y, directionX);
        length = 3;
    }

    @Override
    public String getTypeName() {
        return "Destroyer";
    }

    @Override
    boolean sunken() {
        if (hits >= length){
            return true;
        }
        return false;
    }
}
