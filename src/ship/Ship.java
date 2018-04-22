package ship;

abstract class Ship {

    public String x;
    public String y;
    protected Integer hits;
    protected Integer length;
    public boolean directionX;

    public Integer getLength() {
        return length;
    }

    public Ship(String x, String y, Boolean directionX) {
        this.x = x;
        this.y = y;
        this.directionX = directionX;
    }

    abstract String getTypeName();
    abstract boolean sunken();
}
