package carcassonne.model;

import carcassonne.controller.DataToModel;
import carcassonne.controller.FollowerMap;
import carcassonne.controller.WindowLogic;
import carcassonne.model.realestate.RealEstateManager;
import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirection;
import carcassonne.model.tile.TilePile;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class that extends Game with methods used for testing
 */
public class FakeGame implements DataToModel {
    private final Game                realGame;
    private Table               table;
    private RealEstateManager realEstateManager;
    private WindowLogic         windowLogic;
    private TilePile tilePile = new TilePile();
    private ArrayList<Player>   players = new ArrayList<>();
    private Player              currentPlayer;
    private boolean             finished;
    private boolean             currentTileConfirmed;
    private boolean             followerFriendly;           //determines if a tile has a vacant place for follower
    private TileDirection lastFollowerTileDirection;
    private FollowerMap         currentTileFollowerMap;

    public FakeGame() {
        realGame = new Game();
        realGame.setTable(table);
    }

    public TileDirection getLastFollowerTileDirection() {
        return lastFollowerTileDirection;
    }

    public int getPlayerScore() {
        return 0;
    }

    @Override
    public void turnActions(int x, int y) {
        realGame.turnActions(x, y);
    }

    @Override
    public void turnActions(int x, int y, Rotation angle) {
        turnActions(x, y, angle, null);
    }

    @Override
    public void turnActions(int x, int y, Rotation angle, TileDirection direction) {
        lastFollowerTileDirection = direction;
        realGame.turnActions(x, y, angle, direction);
    }

    public void notifyController() {
        realGame.notifyController();
    }

    @Override
    public void forceNotify() {
        realGame.forceNotify();
    }

    @Override
    public OwnershipChecker getOwnershipChecker() {
        return table;
    }

    //<editor-fold desc="Getters">
    int getNumberOfPlayers() {
        return realGame.getNumberOfPlayers();
    }

    Player getCurrentPlayer() {
        return realGame.getCurrentPlayer();
    }

    public Tile getCurrentTile() {
        return realGame.getCurrentTile();
    }

    public TilePile getTilePile() {
        return realGame.getTilePile();
    }

    public boolean isFinished() {
        return realGame.isFinished();
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setTable(Table table) {
        this.table = table;
        realGame.setTable(table);
    }

    public void setWindowLogic(WindowLogic windowLogic) {
        realGame.setWindowLogic(windowLogic);
    }
    //</editor-fold>

    public void nextPlayer() {
        realGame.nextPlayer();
    }

    public boolean addPlayer(String name, Color color) {
        return realGame.addPlayer(name, color);
    }

    public void dragTile() {
        realGame.dragTile();
    }
}
