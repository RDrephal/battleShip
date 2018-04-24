package ship;

public class AircraftCarrier extends Ship {
    public AircraftCarrier(String[] location) {
        super(location);
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
