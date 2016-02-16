package carcassonne.model.Feature;

/**
 * This class represents parts of tile that are pieces of road
 */
public class RoadPiece extends Feature {
    @Override
    public boolean isRoad() {
        return true;
    }
}
