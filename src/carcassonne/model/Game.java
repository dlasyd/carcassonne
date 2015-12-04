package carcassonne.model;

import java.awt.*;
import java.util.ArrayList;

import carcassonne.model.Player;

public class Game {
    private ArrayList<Player> players = new ArrayList<Player>();
    private Player currentPlayer;
    private Table table;
    private static Game game;
    private boolean currentTileConfirmed;
    private boolean followerFriendly;           //determines if a tile has a vacant place for follower
    private TilePile tilePile = new TilePile();
    private boolean finished;

    public static Game getInstance() {
        if (game == null){
            game =   new Game();
            game.table = new Table();
        }
        return game;
    }

    int getNumberOfPlayers() {
        // TODO Fake it
        return players.size();
    }

    Player getCurrentPlayer() {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
            gameFinished();
    }

    private void gameFinished() {
        finished = true;
    }


    public boolean followerCanBePlaced() {
        return true;
    }

    /*
     * This function was not tested with JUnit
     */
    public void loadTestTiles() {
        game.getTilePile().addTile(new Tile());
        game.getTilePile().addTile(new Tile());
        game.getTilePile().addTile(new Tile());
    }

    public String getCurrentPlayerName() {
        return currentPlayer.getName();
    }
}