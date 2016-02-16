package carcassonne.model.Feature;

public class CityPiece extends Feature {
    @Override
    public boolean isSameType(Feature feature) {
        return feature instanceof CityPiece || feature instanceof CityPieceWithShield || super.isSameType(feature);
    }
}
