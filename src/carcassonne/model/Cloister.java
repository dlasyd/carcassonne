package carcassonne.model;

/**
 * Represents cloister real estate. It consist of a cloister tile and
 * optionally up to 8 tiles around cloister.
 * Is finished if there are 8 tiles around cloister.
 * 1 point for each tile that is part of this real estate.
 */
class Cloister extends RealEstate {
    public Cloister(Tile tile, Table table) {
        super(tile, table);
    }

    boolean isFinished() {
        return true;
    }

    int getPoints() {
        return 9;
    }
}
