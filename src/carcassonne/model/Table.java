package carcassonne.model;

import java.util.HashMap;

/*
 * Table on which table games are played, not a table that has rows and columns
 */
public class Table {
    private static Table table;
    private Tile currentTile;
    /*
     * firstTile is always the same and should be placed in the center of the table before the game starts
     */
    private final Tile firstTile = Tile.getInstance(0, 0);
    private HashMap<Coordinates, Tile> placedTiles = new HashMap<>();

    Table() {
        placeTile(firstTile);
    }

    static Table getInstance() {
        if (table == null)
            table = new Table();
        return table;
    }

    Tile getFirstTile() {
        return firstTile;
    }

    Tile getTile(int i, int j) {
        Coordinates coord = new Coordinates(i, j);
        return placedTiles.get(coord);
    }

    void placeTile(Tile currentTile) {
        if (placedTiles.containsKey(currentTile.getCoordinates()))
            throw new RuntimeException("Trying to place tile on an occupied space");
        placedTiles.put(currentTile.getCoordinates(), currentTile);
    }

    public int placedTilesAmount() {
        return placedTiles.size();
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void dragNewCurrentTile() {
        currentTile = Tile.getInstance();
    }

    void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    HashMap<Coordinates,Tile> getPlacedTiles() {
        return placedTiles;
    }
}
