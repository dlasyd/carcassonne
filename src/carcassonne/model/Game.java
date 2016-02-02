package carcassonne.model;

import carcassonne.controller.DataToModel;
import carcassonne.controller.FollowerMap;
import carcassonne.controller.GameDataBuilder;
import carcassonne.controller.WindowLogic;
import carcassonne.view.DrawableTile;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.*;

import static carcassonne.model.TileDirections.*;

public class Game implements DataToModel{
    private static Game game;
    private Table               table;
    private WindowLogic         windowLogic;
    private TilePile            tilePile = new TilePile();
    private ArrayList<Player>   players = new ArrayList<>();
    private Player              currentPlayer;
    private boolean             finished;
    private boolean             currentTileConfirmed;
    private boolean             followerFriendly;           //determines if a tile has a vacant place for follower

    public static Game getInstance() {
        if (game == null){
            game = new Game();
            game.table = new Table();
        }
        return game;
    }

    @Override
    public void turnActions(int x, int y) {
        turnActions(x, y, Rotation.DEG_0);
    }

    @Override
    public void turnActions(int x, int y, Rotation angle) {
        Tile tile = getCurrentTile();
        tile.setCoordinates(x, y);
        tile.turnRight(angle);
        table.placeTile(tile);
        if (tilePile.hasTiles()) {
            nextPlayer();
            dragTile();
            notifyController();
    } else {
            finished = true;
            windowLogic.finishGame();
            notifyController();
        }
    }

    @Override
    public void turnActions(int x, int y, Rotation angle, TileDirections direction) {
        Tile tile = getCurrentTile();
        tile.setCoordinates(x, y);
        tile.turnRight(angle);
        table.placeTile(tile);
        table.placeFollower(getCurrentPlayer(), direction);
        if (tilePile.hasTiles()) {
            nextPlayer();
            dragTile();
            notifyController();
        } else {
            finished = true;
            windowLogic.finishGame();
            notifyController();
        }
    }

    @Override
    public void turnActions(int x, int y, Rotation angle, double[] currentFollower) {
        Tile tile = getCurrentTile();
        tile.setCoordinates(x, y);
        tile.turnRight(angle);
        table.placeTile(tile);
        if (tilePile.hasTiles()) {
            nextPlayer();
            dragTile();
            notifyController();
        } else {
            finished = true;
            windowLogic.finishGame();
            notifyController();
        }
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
                setPossibleLocationsAndRotations(table.getPossibleTileLocationsAndRotations()).
                setPlacedFollowers(table.getPlacedFollowers()).
                createGameData());
    }

    @Override
    public void forceNotify() {
        notifyController();
    }

    //<editor-fold desc="Getters">
    int getNumberOfPlayers() {
        return players.size();
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }
    boolean isCurrentTileConfirmed() {
        return currentTileConfirmed;
    }

    public Tile getCurrentTile() {
        return table.getCurrentTile();
    }

    public TilePile getTilePile() {
        return tilePile;
    }

    public boolean isFinished() {
        return finished;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    void setFollowerFriendly(boolean followerFriendly) {
        this.followerFriendly = followerFriendly;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void setWindowLogic(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }
    //</editor-fold>

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

    /*
     * Adding no param player is used only in testing
     */
    boolean addPlayer() {
        return addPlayer("Default", Color.BLACK);
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


    public void dragTile() {
        if (tilePile.hasTiles())
            table.setCurrentTile(tilePile.dragTile());
        else
            throw new RuntimeException("Trying to drag a tile from an empty pile");
    }

    //TODO rename,
    //TODO where is it used
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


}
