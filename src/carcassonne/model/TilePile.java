package carcassonne.model;

import java.util.ArrayList;

public class TilePile {
    private static TilePile tilePile;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();

    public static TilePile getInstance() {
        if (tilePile == null)
            tilePile = new TilePile();
        return tilePile;
    }

    public int getNumberOfTiles() {
        return tiles.size();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    public Tile dragTile() {
        if (tiles.isEmpty())
            throw new RuntimeException("Trying to drag a tile from an empty pile");
        int randomIndex = (int) (Math.random() * tiles.size());
        Tile tile = tiles.get(randomIndex);
        tiles.remove(randomIndex);
        return tile;
    }

    public boolean hasTiles() {
        return tiles.isEmpty() != true;
    }
}
