package carcassonne.model;

/**
 * This class represents parts of tile that are pieces of road
 */
class RoadPiece extends Feature {
    @Override
    public boolean isRoad() {
        return true;
    }
}
