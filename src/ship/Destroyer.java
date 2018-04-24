package ship;

public class Destroyer extends Ship {

    public Destroyer(String[] location) {
        super(location);
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
