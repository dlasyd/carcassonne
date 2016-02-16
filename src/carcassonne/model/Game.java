package carcassonne.model;

import carcassonne.controller.DataToModel;
import carcassonne.controller.GameDataBuilder;
import carcassonne.controller.WindowLogic;
import carcassonne.model.tile.Rotation;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileDirections;
import carcassonne.model.tile.TilePile;

import java.awt.*;
import java.util.*;

public class Game implements DataToModel{
    private static Game game;
    private Table               table;
    private WindowLogic         windowLogic;
    private final TilePile tilePile = new TilePile();
    private final ArrayList<Player>   players = new ArrayList<>();
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
            if (table.getPossibleTileLocationsAndRotations().size() == 0)
                dragTile();
            notifyController();
        } else {
            finished = true;
            table.addEndGamePoints();
            windowLogic.finishGame();
            notifyController();
        }
    }

    public void notifyController() {
        windowLogic.update(new GameDataBuilder().setName(getCurrentPlayer().getName()).
                setPoints("" + getCurrentPlayer().getCurrentPoints()).
                setFollowers(getCurrentPlayer().getNumberOfFollowers()).
                setPlayerColor(getCurrentPlayer().getColor()).
                setTilesLeft("" + tilePile.getNumberOfTiles()).
                setCurrentTile(table.getCurrentTile()).
                setPreviouslyPlacedTile(table.getPreviouslyPlacedTile()).
                setPossibleLocationsAndRotations(table.getPossibleTileLocationsAndRotations()).
                setPlacedFollowers(table.getPlacedFollowers()).
                setPlayersStats(createPlayersStats()).
                createGameData());
    }

    private ArrayList<String[]> createPlayersStats() {
        ArrayList<String[]> result = new ArrayList<>();
        for (Player player: players) {
            result.add(new String[] {player.getName(),
                    "" + player.getNumberOfFollowers(), "" + player.getCurrentPoints()});
        }
        return result;
    }

    @Override
    public void forceNotify() {
        notifyController();
    }

    @Override
    public OwnershipChecker getOwnershipChecker() {
        return table;
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
