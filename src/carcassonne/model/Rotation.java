package carcassonne.model;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 09/12/15.
 */
public enum Rotation {
    DEG_0(0), DEG_90(1), DEG_180(2), DEG_270(3);

    private int numberOf90Rotations;

    Rotation(int nOf90) {
        numberOf90Rotations = nOf90;
    }

    public int getNumberOf90Rotations() {
        return numberOf90Rotations;
    }
}
