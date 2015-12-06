package carcassonne.model;

/**
 * Created by Andrey on 03/12/15.
 */
public enum TileDirections {
    /*
     * WWN = WEST WEST NORTH
     * NNW = NORTH NORTH WEST
     * etc
     */
    SOUTH, NORTH, EAST, WEST, END, WWN, NNW, NNE, EEN, CENTER, SSE, SSW, WWS, EES;

    public TileDirections getNeighbour() {
        switch (this) {
            case SOUTH:
                return NORTH;
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
        }
        assert (false);
        return CENTER;
    }
}
