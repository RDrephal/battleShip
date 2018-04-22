package ship;

public class Submarine extends Ship{

    public Submarine(String x, String y, Boolean directionX) {
        super(x, y, directionX);
        length = 2;
    }

    @Override
    public String getTypeName() {
        return "Submarine";
    }

    @Override
    boolean sunken() {
        if (hits >= length){
            return true;
        }
        return false;
    }
}
