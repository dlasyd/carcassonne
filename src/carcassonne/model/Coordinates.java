package carcassonne.model;

class Coordinates {
    private int x, y;
    private Rotation rotation;

    Coordinates (int x, int y) {
        this(x, y, Rotation.DEG_0);
    }

    Coordinates (int x, int y, Rotation r) {
        this.x = x;
        this.y = y;
        this.rotation = r;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Coordinates))
            return false;
        Coordinates coord = (Coordinates) o;
        if (x == coord.getX() && y == coord.getY())
            return true;
        return false;
    }

    public int hashCode() {
        return (13 * x + 11 * y);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void turnRight() {
        rotation = rotation.turnRight();
    }

    public enum Rotation {
        DEG_0, DEG_90, DEG_180, DEG_270;
        Rotation turnRight() {
            return Rotation.values()[(this.ordinal() + 1) % Rotation.values().length];
        }
    }
}