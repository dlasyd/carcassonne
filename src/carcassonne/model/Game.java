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
        turnActions(x, y, angle, null);
    }

    /*
     * direction should correspond to a rotated tile
     */
    @Override
    public void turnActions(int x, int y, Rotation angle, TileDirections direction) {
        Tile tile = getCurrentTile();
        tile = tile.setCoordinates(x, y);
        tile = tile.turnRight(angle);
        if (direction != null)
            tile = tile.placeFollower(getCurrentPlayer(), direction);
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
                setPossibleLocationsAndRotations(table.getPossibleTileLocationsAndRotations()).
                setPlacedFollowers(table.getPlacedFollowers()).
                createGameData());
    }

    @Override
    public void forceNotify() {
        notifyController();
    }

    @Override
    public OwnershipChecker getOwnershipChecker() {
        return (OwnershipChecker)table;
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

    public void dragTile() {
        if (tilePile.hasTiles())
            table.setCurrentTile(tilePile.dragTile());
        else
            throw new RuntimeException("Trying to drag a tile from an empty pile");
    }

    //<editor-fold desc="Getters">
    int getNumberOfPlayers() {
        return players.size();
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    Tile getCurrentTile() {
        return table.getCurrentTile();
    }

    public TilePile getTilePile() {
        return tilePile;
    }

    boolean isFinished() {
        return finished;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setTable(Table table) {
        this.table = table;
    }

    public void setWindowLogic(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }
    //</editor-fold>

}
