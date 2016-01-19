package carcassonne.model;

import java.util.HashMap;

/*
 * Table on which table games are played, not a table that has rows and columns
 */
public class Table {
    private static Table table;
    private Tile currentTile;
    private Tile tilePlacedLast;
    private RealEstateManager realEstateManager;
    /*
     * firstTile is always the same and should be placed in the center of the table before the game starts
     */
    private final Tile firstTile = Tile.getInstance(0, 0, TileName.CITY1RWE);
    private HashMap<Coordinates, Tile> placedTiles = new HashMap<>();

    public Table() {
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
        if (placedTiles.get(coord) != null)
            return placedTiles.get(coord);
        else
            return Tile.getNullInstance();
    }

    void placeTile(Tile currentTile) {
        if (placedTiles.containsKey(currentTile.getCoordinates()))
            throw new RuntimeException("Trying to place tile on an occupied space");
        placedTiles.put(currentTile.getCoordinates(), currentTile);
        notifyObservers(currentTile);
        tilePlacedLast = currentTile;
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

    public Tile getNeighbouringTile(int x, int y, TileDirections direction) {
        switch (direction) {
            case NNE:
            case NNW:
            case NORTH:
                return getTile(x, y - 1);
            case SSE:
            case SSW:
            case SOUTH:
                return getTile(x, y + 1);
            case WWN:
            case WWS:
            case WEST:
                return getTile(x - 1, y);
            case EEN:
            case EES:
            case EAST:
                return getTile(x + 1, y);
        }
        return Tile.getNullInstance();
    }

    public void setRealEstateManager(RealEstateManager realEstateManager) {
        this.realEstateManager = realEstateManager;
    }

    private void notifyObservers(Tile tile) {
        //TODO refactor me!
        if (realEstateManager != null)
            realEstateManager.update(tile);
    }

    public void placeFollower(Player player, TileDirections direction) {
        // if tilePlacedLast direction is not part of existing real estate
        if (realEstateManager.isPartOfRealEstate(tilePlacedLast, direction))
            throw new RuntimeException("Cannot place follower on existing real estate");
        tilePlacedLast.placeFollower(player, direction);
        realEstateManager.createAsset(player, tilePlacedLast);
    }

    public Tile getPreviouslyPlacedTile() {
        return tilePlacedLast;
    }
}
