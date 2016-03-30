package carcassonne.model;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;

/**
 * Used by RealEstate
 */
public interface TilesOnTable {
    Tile getTile(int x, int y);
    Tile getNeighbouringTile(int x, int y, TileDirection direction);
}
