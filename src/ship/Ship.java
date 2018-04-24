package ship;


abstract class Ship {

    public String[] location;
    protected Integer hits;
    protected Integer length;

    public Integer getLength() {
        return length;
    }

    public Ship(String[] location) {
        this.location = location;
    }

    abstract String getTypeName();
    abstract boolean sunken();
}
