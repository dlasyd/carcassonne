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

    public static Rotation getValue(int rotations) {
        switch (rotations) {
            case 0:
                return DEG_0;
            case 1:
                return DEG_90;
            case 2:
                return DEG_180;
            case 3:
                return DEG_270;
            default:
                assert false;
                return DEG_0;
        }
    }
}
