package carcassonne.model.realestate;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;

import java.util.*;
import java.util.function.Supplier;

/**
 * Encapsulates information about tiles and tileDirections that belong to a specific realEstate.
 * Every RealEstate object should have one ElementsOfRealEstate instance
 */
public class ElementsOfRealEstate {
    private Map<Tile, Set<TileDirection>> tilesToDirections = new HashMap();

    public ElementsOfRealEstate() {
    }

    private ElementsOfRealEstate(ElementsOfRealEstate element, Map<Tile, Set<TileDirection>> tilesMap) {
        this.tilesToDirections = new HashMap<>(element.tilesToDirections);
        for (Tile tile : tilesMap.keySet()) {
            tilesToDirections.put(tile, tilesMap.get(tile));
        }
    }

    public ElementsOfRealEstate add(Tile tile, TileDirection... direction) {
        return add(tile, new HashSet<>(Arrays.asList(direction)));
    }

    public ElementsOfRealEstate add(Tile tile, Set<TileDirection> directions) {
        exceptionIfDuplicate(tile);
        Map<Tile, Set<TileDirection>> map = new HashMap<>();
        map.put(tile, directions);
        return addAll(map);
    }

    public ElementsOfRealEstate addAll(Map<Tile, Set<TileDirection>> tilesToTileDirections) {
        tilesToTileDirections.keySet().forEach(this::exceptionIfDuplicate);
        return new ElementsOfRealEstate(this, tilesToTileDirections);
    }

    public Set<Tile> getTileSet() {
        return new HashSet<>(tilesToDirections.keySet());
    }

    public boolean contains(Tile tile) {
        return tilesToDirections.containsKey(tile);
    }

    public Set<TileDirection> getTileDirectionSet(Tile tile) {
        if (!tilesToDirections.containsKey(tile))
            throw new TileDoesNotExist();
        return new HashSet<>(tilesToDirections.get(tile));
    }

    public int numberOfTiles() {
        return tilesToDirections.size();
    }

    public Map<Tile, Set<TileDirection>> getElementsMap() {
        return new HashMap<>(tilesToDirections);
    }

    private void exceptionIfDuplicate(Tile tile) {
        if (tilesToDirections.containsKey(tile))
            throw new AddingSameTile();
    }

    public ElementsOfRealEstate addFromSupplier(Supplier<Map<Tile, Set<TileDirection>>> supplier) {
        return new ElementsOfRealEstate(this, supplier.get());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ElementsOfRealEstate))
            return false;

        ElementsOfRealEstate other = (ElementsOfRealEstate) o;

        return this.tilesToDirections.equals(other.tilesToDirections);
    }

    public static class AddingSameTile extends RuntimeException{
    }

    public static class TileDoesNotExist extends RuntimeException{
    }
}


