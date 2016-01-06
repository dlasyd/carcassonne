package carcassonne.model;

/**
 * Land is never finished.
 * Owners get 3 points for each finished castle that touches the land
 */
class Land extends RealEstate {
    public Land(Tile tile, Table table) {
        super(tile, table);
    }

    boolean isFinished() {
        return false;
    }
}
