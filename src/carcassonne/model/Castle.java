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
        boolean finished = true;
        Set<Tile> tiles = super.getTileSet();
        outer:
        for (Tile tile: tiles) {
            Set<TileDirections> directions = tilesAndFeatureTileDirections.get(tile);
            directions.remove(TileDirections.END);
            for (TileDirections direction: directions) {
                Tile neighbour = table.getNeighbouringTile(tile.getX(), tile.getY(), direction);
                if (!tilesAndFeatureTileDirections.containsKey(neighbour)) {
                    finished = false;
                    break outer;
                }
            }
        }
        return finished;
    }
}
