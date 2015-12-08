package carcassonne.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public Set<TileDirections> getEdge() {
        Set<TileDirections> result = new HashSet<>();
        switch (this) {
            case NORTH:
            case NNW:
            case NNE:
                result.addAll(Arrays.asList(NORTH, NNW, NNE));
                break;
            case SOUTH:
            case SSW:
            case SSE:
                result.addAll(Arrays.asList(SOUTH, SSW, SSE));
                break;
            case EAST:
            case EEN:
            case EES:
                result.addAll(Arrays.asList(EAST, EEN, EES));
                break;
            case WEST:
            case WWN:
            case WWS:
                result.addAll(Arrays.asList(WEST, WWN, WWS));
                break;
        }
        return result;
    }
}
