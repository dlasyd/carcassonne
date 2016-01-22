package carcassonne.model;

import carcassonne.controller.DataToModel;
import carcassonne.controller.GameDataBuilder;
import carcassonne.controller.WindowLogic;
import carcassonne.view.DrawableTile;

import java.awt.*;
import java.util.ArrayList;

public class Game implements DataToModel{
    private static Game game;
    private ArrayList<Player> players = new ArrayList<Player>();
    private Player currentPlayer;
    private Table table;
    private boolean currentTileConfirmed;
    private boolean followerFriendly;           //determines if a tile has a vacant place for follower
    private TilePile tilePile = new TilePile();
    private boolean finished;
    private WindowLogic windowLogic;

    public static Game getInstance() {
        if (game == null){
            game =   new Game();
            game.table = new Table();
        }
        return game;
    }

    @Override
    public void turnActions(int x, int y) {
        Tile tile = getCurrentTile();
        tile.setCoordinates(x, y);
        table.placeTile(tile);
        if (tilePile.hasTiles()) {
            nextPlayer();
            dragTile();
            notifyController();
            if (!tilePile.hasTiles())
                windowLogic.setLastTurn(true);
        } else {
            finished = true;
            table.setCurrentTile(Tile.getNullInstance());
            windowLogic.finishGame();
            notifyController();
        }
    }

    @Override
    public void turnActions(int x, int y, TileDirections direction) {
        turnActions(x, y);
        table.placeFollower(getCurrentPlayer(), direction);
    }

    @Override
    public void forceNotify() {
        notifyController();
    }

    int getNumberOfPlayers() {
        return players.size();
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void nextPlayer() {
        if (currentPlayer == null) {
            currentPlayer = players.get(0);
        } else {
            currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        }
    }

    public boolean addPlayer(String name, Color color) {
        if (5 > getNumberOfPlayers()) {
            players.add(new Player(name, color));
            return true;
        } else {
            return false;
        }
    }

    boolean addPlayer() {
        return addPlayer("Default", Color.BLACK);
    }

    boolean isCurrentTileConfirmed() {
        return currentTileConfirmed;
    }

    void confirmCurrentTile(int x, int y) {
        currentTileConfirmed = true;
    }

    void confirmFollower() {
        if (!currentTileConfirmed)
            throw new RuntimeException("Cannot place follower if tile placement is not confirmed");
        if (!followerFriendly)
            throw new RuntimeException("Not followerFriendly (no vacant space for a follower)");
        currentPlayer.increaseNumberOfProperties();
        currentPlayer.placeFollower();
        nextPlayer();
    }

    void setFollowerFriendly(boolean followerFriendly) {
        this.followerFriendly = followerFriendly;
    }

    public Tile getCurrentTile() {
        return table.getCurrentTile();
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void setWindowLogic(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }

    public void notifyController() {
        windowLogic.update(new GameDataBuilder().setName(getCurrentPlayer().getName()).
                setPoints("" + getCurrentPlayer().getCurrentPoints()).
                setFollowers("" + getCurrentPlayer().getNumberOfFollowers()).
                setPlayerColor(getCurrentPlayer().getColor()).
                setTilesLeft("" + tilePile.getNumberOfTiles()).
                setCurrentTile(table.getCurrentTile()).
                setPreviouslyPlacedTile(table.getPreviouslyPlacedTile()).
                setPossibleTileLocations(table.getPossibleTileLocations()).
                createGameData());
    }

    public TilePile getTilePile() {
        return tilePile;
    }

    public boolean isFinished() {
        return finished;
    }

    public void dragTile() {
        if (tilePile.hasTiles())
            table.setCurrentTile(tilePile.dragTile());
        else
            throw new RuntimeException("Trying to drag a tile from an empty pile");
    }

    public boolean followerCanBePlaced() {
        return true;
    }

    /*
     * This function was not tested with JUnit
     * Used in testing
     */
    public void loadTestTiles() {
        game.getTilePile().addTile(Tile.getInstance());
        game.getTilePile().addTile(Tile.getInstance());
        game.getTilePile().addTile(Tile.getInstance());
    }

    public String getCurrentPlayerName() {
        return currentPlayer.getName();
    }
}
