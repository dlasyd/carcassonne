package carcassonne.model.tile;

import java.util.HashSet;
import java.util.Set;

public class Coordinates {
    private final int x;
    private final int y;
    private final Rotation rotation;

    public Coordinates(int x, int y) {
        this(x, y, Rotation.DEG_0);
    }

    private Coordinates(int x, int y, Rotation r) {
        this.x = x;
        this.y = y;
        this.rotation = r;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinates))
            return false;
        Coordinates coord = (Coordinates) o;
        return x == coord.getX() && y == coord.getY();
    }

    @Override
    public int hashCode() {
        return (13 * x + 11 * y);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public static Set<Coordinates> getAround(Set<Coordinates> coordinatesSet) {
        Set<Coordinates> result = new HashSet<>();
        for (Coordinates coordinate: coordinatesSet) {
            result.add(new Coordinates(coordinate.getX() - 1, coordinate.getY()));
            result.add(new Coordinates(coordinate.getX() + 1, coordinate.getY()));
            result.add(new Coordinates(coordinate.getX(), coordinate.getY() - 1));
            result.add(new Coordinates(coordinate.getX(), coordinate.getY() + 1));
        }
        result.removeAll(coordinatesSet);
        return result;
    }

    @Override
    public String toString() {
        return "(" + getX()+", " + getY()+")";
    }

}