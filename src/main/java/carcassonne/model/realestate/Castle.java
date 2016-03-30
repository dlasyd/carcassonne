package carcassonne.model.realestate;

import carcassonne.model.Table;
import carcassonne.model.TilesOnTable;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;
import carcassonne.model.Feature.Feature;

import java.util.Set;

/**
 * Castle is a real estate that consists of City features
 */
class Castle extends RealEstate{
    Castle(Tile tile, TilesOnTable table) {
        super(tile, table);
    }

    @Override
    boolean isFinished() {
        if (!super.finished) {
            boolean finished = true;
            Set<Tile> tiles = super.getTileSet();
            outer:
            for (Tile tile : tiles) {
                Set<TileDirection> directions = elements.getTileDirectionSet(tile);
                for (TileDirection direction : directions) {
                    Tile neighbour = super.getTable().getNeighbouringTile(tile.getX(), tile.getY(), direction);
                    if (!elements.contains(neighbour)) {
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
        for (Tile tile: elements.getTileSet()) {
            result += 1;
            Feature castleOrWithShield = tile.getFeature(elements.getTileDirectionSet(tile).iterator().next());
            if (castleOrWithShield.hasShield())
                result +=1;

        }
        if (isFinished())
            result *= 2;
        return result;
    }
}
