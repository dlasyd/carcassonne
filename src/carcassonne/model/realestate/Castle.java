package carcassonne.model.realEstate;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirections;
import carcassonne.model.feature.Feature;

import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 27/12/15.
 */
class Castle extends RealEstate{
    Castle(Tile tile) {
        super(tile);
    }

    @Override
    public boolean isFinished() {
        if (!super.finished) {
            boolean finished = true;
            Set<Tile> tiles = super.getTileSet();
            outer:
            for (Tile tile : tiles) {
                Set<TileDirections> directions = tilesAndFeatureTileDirections.get(tile);
                for (TileDirections direction : directions) {
                    Tile neighbour = super.getTable().getNeighbouringTile(tile.getX(), tile.getY(), direction);
                    if (!tilesAndFeatureTileDirections.containsKey(neighbour)) {
                        finished = false;
                        break outer;
                    }
                }
            }
            super.finished = finished;
            return finished;
        } else {
            return true;
        }
    }

    int getPoints() {
        int result = 0;
        for (Tile tile: tilesAndFeatureTileDirections.keySet()) {
            result += 1;
            Feature castleOrWithShield = tile.getFeature(tilesAndFeatureTileDirections.get(tile).iterator().next());
            if (castleOrWithShield.hasShield())
                result +=1;

        }
        if (isFinished())
            result *= 2;
        return result;
    }
}
