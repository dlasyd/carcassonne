package carcassonne.model.tile;

import java.util.*;

public enum TileDirection {
    /*
     * WWN = WEST WEST NORTH
     * NNW = NORTH NORTH WEST
     * etc
     */
    SOUTH, NORTH, EAST, WEST, WWN, NNW, NNE, EEN, CENTER, SSE, SSW, WWS, EES;

    public TileDirection getNeighbour() {
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

    public Set<TileDirection> getEdge() {
        Set<TileDirection> result = new HashSet<>();
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

    public TileDirection turnRight(Rotation angle) {
        List<TileDirection> congruentNorth = new ArrayList<>(Arrays.asList(NORTH, EAST, SOUTH, WEST));
        List<TileDirection> congruentNNE   = new ArrayList<>(Arrays.asList(NNE, EES, SSW, WWN));
        List<TileDirection> congruentNNW   = new ArrayList<>(Arrays.asList(NNW, EEN, SSE, WWS));

        int numberOf90Rotations = angle.ordinal();

        switch (this) {
            case SOUTH:
            case NORTH:
            case EAST:
            case WEST:
                return congruentNorth.get((congruentNorth.indexOf(this) + numberOf90Rotations) % congruentNorth.size());
            case NNE:
            case EES:
            case SSW:
            case WWN:
                return congruentNNE.get((congruentNNE.indexOf(this) + numberOf90Rotations) % congruentNNE.size());
            case NNW:
            case EEN:
            case SSE:
            case WWS:
                return congruentNNW.get((congruentNNW.indexOf(this) + numberOf90Rotations) % congruentNNW.size());
            case CENTER:
                return this;
        }
        assert false;
        return TileDirection.CENTER;
    }

    public Collection<TileDirection> getAdjacentDirections() {
        HashSet<TileDirection> result = new HashSet<>();
        switch (this) {
            case SOUTH:
                result.add(SSW);
                result.add(SSE);
                break;
            case NORTH:
                result.add(NNW);
                result.add(NNE);
                break;
            case EAST:
                result.add(EEN);
                result.add(EES);
                break;
            case WEST:
                result.add(WWS);
                result.add(WWN);
                break;
            case WWN:
                result.add(WEST);
                result.add(NNW);
                break;
            case EEN:
                result.add(EAST);
                result.add(NNE);
                break;
            case WWS:
                result.add(WEST);
                result.add(SSW);
                break;
            case EES:
                result.add(EAST);
                result.add(SSE);
                break;
            case NNE:
                result.add(NORTH);
                result.add(EEN);
                break;
            case SSE:
                result.add(SOUTH);
                result.add(EES);
                break;
            case NNW:
                result.add(NORTH);
                result.add(WWN);
                break;
            case SSW:
                result.add(SOUTH);
                result.add(WWS);
                break;
            default:
                assert (false);
        }
        return result;
    }

    public Coordinates getNeighbourCoordinates(int x, int y) {
        switch (this) {
            case NNE:
            case NNW:
            case NORTH:
                return new Coordinates(x, y - 1);
            case SSE:
            case SSW:
            case SOUTH:
                return new Coordinates(x, y + 1);
            case WWN:
            case WWS:
            case WEST:
                return new Coordinates(x - 1, y);
            case EEN:
            case EES:
            case EAST:
                return new Coordinates(x + 1, y);
        }
        throw new RuntimeException("Cannot get neighbour coordinates");
    }
}
