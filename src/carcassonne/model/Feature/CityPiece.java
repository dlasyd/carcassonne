package carcassonne.model.feature;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 06/01/16.
 */
public class CityPiece extends Feature {
    @Override
    public boolean isSameType(Feature feature) {
        if (feature instanceof CityPiece || feature instanceof  CityPieceWithShield) {
            return true;
        } else {
            return super.isSameType(feature);
        }
    }
}
