package ship;

public class AircraftCarrier extends Ship {
    public AircraftCarrier(String x, String y, Boolean directionX) {
        super(x, y, directionX);
        length = 5;
    }

    @Override
    String getTypeName() {
        return "Aircraft Carrier";
    }

    @Override
    boolean sunken() {
        if (hits >= length){
            return true;
        }
        return false;
    }
}
