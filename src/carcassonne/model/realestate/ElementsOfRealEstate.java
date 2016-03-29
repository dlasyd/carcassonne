package carcassonne.model.realestate;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;

import java.util.*;

/**
 * Encapsulates information about tiles and tileDirections that belong to a specific realEstate.
 * Every RealEstate object should have one ElementsOfRealEstate instance
 */
public class ElementsOfRealEstate {
    private Map<Tile, Set<TileDirection>> tilesToDirections = new HashMap();

    public ElementsOfRealEstate() {}

    private ElementsOfRealEstate(ElementsOfRealEstate element, Tile newTile, Set<TileDirection> directions) {
        this.tilesToDirections = new HashMap<>(element.tilesToDirections);
        this.tilesToDirections.put(newTile, directions);
    }

    private ElementsOfRealEstate(ElementsOfRealEstate element, Map<Tile, Set<TileDirection>> tilesMap) {
        this.tilesToDirections = new HashMap<>(element.tilesToDirections);
        for (Tile tile : tilesMap.keySet()) {
//            if (tilesToDirections.containsKey(tile))
//                throw new RuntimeException("Cannot add because tile has already been added");
            tilesToDirections.put(tile, tilesMap.get(tile));
        }
    }
    public ElementsOfRealEstate add(Tile tile, TileDirection... direction) {
        return add(tile, new HashSet<>(Arrays.asList(direction)));
    }

    public ElementsOfRealEstate add(Tile tile, Set<TileDirection> directions) {
        //TODO uncomment code later
//        if (tilesToDirections.containsKey(tile))
//            throw new RuntimeException("Cannot add because tile has already been added");
        return new ElementsOfRealEstate(this, tile, directions);
    }

    public Set<Tile> getTileSet() {
        return new HashSet<>(tilesToDirections.keySet());
    }

    public boolean contains(Tile tile) {
        return tilesToDirections.containsKey(tile);
    }

    public Set<TileDirection> getTileDirectionSet(Tile tile) {
        if (!tilesToDirections.containsKey(tile))
            throw new RuntimeException("Trying to get realEstate specific " +
                    "tile directions of tile that was not added");
        return new HashSet<>(tilesToDirections.get(tile));
    }

    public ElementsOfRealEstate addAll(Map<Tile, Set<TileDirection>> tilesToTileDirections) {
        return new ElementsOfRealEstate(this, tilesToTileDirections);
    }

    public int nubmerOfTiles() {
        return tilesToDirections.size();
    }

    public Map<Tile,Set<TileDirection>> getElementsMap() {
        return new HashMap<>(tilesToDirections);
    }
}
