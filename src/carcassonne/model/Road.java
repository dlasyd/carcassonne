package carcassonne.model;

import java.util.Map;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 04/01/16.
 */
class Road extends RealEstate {

    Road(Tile tile) {
        super(tile);
    }

    /*
     * Finished if
     * 1) looped
     * 2) has two tiles with one TileDirection
     */
    @Override
    public boolean isFinished() {
        if (super.finished == true)
            return true;
        else {
            Map<Tile, Set<TileDirections>> realEstateData = super.getTilesAndFeatureTileDirections();

            /*
             * two ends case
             */
            int end = 0;
            for (Tile tile: realEstateData.keySet()) {
                end += (realEstateData.get(tile).size() == 1) ? 1: 0;

                if ((tile.getName() == TileName.ROAD4 || tile.getName() == TileName.ROAD3) &&
                        realEstateData.get(tile).size() == 2)
                    end += 2;
            }
            assert (end < 3);
            if (end == 2)
                super.finished = true;


        }

        return super.finished;
    }

    int getPoints() {
            return tilesAndFeatureTileDirections.size();
    }
}
