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
            case WWN:
                return EEN;
            case EEN:
                return WWN;
            case WWS:
                return EES;
            case EES:
                return WWS;
            case NNE:
                return SSE;
            case SSE:
                return NNE;
            case NNW:
                return SSW;
            case SSW:
                return NNW;
        }
        assert (false);
        return CENTER;
    }
}
