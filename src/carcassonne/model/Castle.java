package carcassonne.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 27/12/15.
 */
class Castle extends RealEstate{
    Castle(Tile tile, Table table) {
        super(tile, table);
    }

    @Override
    public boolean isFinished() {
        if (super.finished == false) {
            boolean finished = true;
            Set<Tile> tiles = super.getTileSet();
            outer:
            for (Tile tile : tiles) {
                Set<TileDirections> directions = tilesAndFeatureTileDirections.get(tile);
                for (TileDirections direction : directions) {
                    Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(), direction);
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
            Feature castleOrWithShield = tile.getFeature(Util.any(tilesAndFeatureTileDirections.get(tile)));
            if (castleOrWithShield instanceof CityPieceWithShield)
                result +=1;

        }
        if (isFinished())
            result *= 2;
        return result;
    }
}
