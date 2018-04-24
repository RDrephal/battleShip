package ship;

public class Cruiser extends Ship {
    public Cruiser(String[] location) {
        super(location);
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
