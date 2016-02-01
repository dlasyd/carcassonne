package carcassonne.controller;

import carcassonne.model.Coordinates;
import carcassonne.model.Rotation;
import carcassonne.model.TileDirections;
import carcassonne.view.DrawableTile;
import carcassonne.view.ViewWindow;

import java.awt.*;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class GameWindowLogic implements WindowLogic {
    private boolean tilePreviewEnabled = true;
    private boolean currentTileOnTheTable;
    private boolean tileConfirmed;
    private boolean gameEnded = false;
    private int     currentTileX, currentTileY;
    private GameData        gameData;
    private ViewWindow      gameWindow;
    private DataToModel     dataToModel;
    private Rotation        currentTileRotation = Rotation.DEG_0;
    private Set<Rotation>   possibleCurrentTileRotations;
    private double[]        currentFollowerLocation;
    private boolean         temporaryFollowerDisplayed;
    private boolean         canFollowerBePlaced;


    @Override
    public void setGameWindow(ViewWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void update(GameData gameData) {
        this.gameData = gameData;

        if(!gameEnded) {
            tilePreviewEnabled = true;
        }

        updateUI();
        tileConfirmed = false;
        gameWindow.setConfirmTileButtonText("Confirm tile");
        gameWindow.setConfirmTileButtonEnabled(false);
    }

    private void updateUI() {
        gameWindow.setCurrentPlayerName(gameData.getName());
        gameWindow.setNumberOfFollowers(gameData.getFollowers());
        gameWindow.setCurrentPoints(gameData.getPoints());
        gameWindow.setPlayerColorRemainder(gameData.getPlayerColor());
        gameWindow.setTilesNumber(gameData.getTilesLeft());
        gameWindow.setCurrentTile(new DrawableTile(gameData.getCurrentTile()));
        gameWindow.setTilePreviewEnabled(tilePreviewEnabled);
        gameWindow.setPossibleTileLocations(gameData.getPossibleTileLocations());
        gameWindow.addTileOnTable(new DrawableTile(gameData.getPreviouslyPlacedTile()));
        gameWindow.setPlacedFollowers(gameData.getPlacedFollowers());
    }

    /*
     * Methods that are invoked by action listeners of GameWindow
     * BEGINNING
     */

    /**
     * This method must be invoked when an area that represents a tile possible location
     * is clicked.
     * <p>The method checks if required conditions are met and passes information to model.</p>
     *
     * @param x Logical x of a location
     * @param y Logical y of a location
     */
    @Override
    public void clickOnPossibleLocation(int x, int y) {
        /*
         * Things that are checked as part of window logic:
         * 1) if the tile is already on the table and logical coordinates are the same,
         * should not do anything. Otherwise the rotation of tile will be changed;
         */
        if (isCurrentTileOnTheTable() && currentTileX == x && currentTileY == y) {
            return;
        }

        if (isTileConfirmed()) {
            return;
        }
        if (!gameEnded) {
            currentTileOnTheTable = true;
            gameWindow.setConfirmTileButtonEnabled(true);
            currentTileX = x;
            currentTileY = y;
            possibleCurrentTileRotations = gameData.getPossibleLocationsAndRotations().get(new Coordinates(x, y));
            gameWindow.getCurrentTile().setRotation(Rotation.DEG_0);
            if (!possibleCurrentTileRotations.contains(Rotation.DEG_0)) {
                for (int i = 1; i < 4; i++) {
                    if (possibleCurrentTileRotations.contains(Rotation.values()[i])) {
                        currentTileRotation = Rotation.values()[i];
                        gameWindow.getCurrentTile().setRotation(currentTileRotation);
                        break;
                    }
                }
            } else {
                currentTileRotation = Rotation.DEG_0;
            }
        }
        gameWindow.setTilePreviewEnabled(false);
        tilePreviewEnabled = false;
        gameWindow.repaintWindow();
    }

    @Override
    public void updateTileConfirmedButton() {
        assert (currentTileOnTheTable);
        if (tileConfirmed) {
            tileConfirmed = false;
            canFollowerBePlaced = false;
            temporaryFollowerDisplayed = false;
            gameWindow.setConfirmTileButtonText("Confirm tile");
            gameWindow.setEndTurnButtonEnabled(false);
        } else {
            tileConfirmed = true;
            canFollowerBePlaced = true;
            gameWindow.setConfirmTileButtonText("Relocate tile");
            gameWindow.setPossibleFollowerLocations(dataToModel.getPossibleFollowerLocations(currentTileX, currentTileY));
            gameWindow.setEndTurnButtonEnabled(true);
        }
        gameWindow.repaintWindow();
    }

    @Override
    public void updateEndTurnButton() {
        if (temporaryFollowerDisplayed)
            dataToModel.turnActions(currentTileX, currentTileY, currentTileRotation, currentFollowerLocation);
        else
            dataToModel.turnActions(currentTileX, currentTileY, currentTileRotation);

        gameWindow.setEndTurnButtonEnabled(false);
        currentTileOnTheTable = false;
        canFollowerBePlaced = false;
        temporaryFollowerDisplayed = false;
        currentTileRotation = Rotation.DEG_0;
        if (gameEnded)
            gameWindow.displayEndgameWindow();
    }

    /**
     * Should be invoked when click on temporarily placed tile happens.
     *<p>Parameters determine a position of a follower within temporary tile in the coordinate system where
     * top left corner of a tile is the origin.</p>
     * <p>If follower cannot be placed in the current moment, method will invoke clickOnCurrentTile()</p>
     * @param xMultiplier Tile size relative X multiplier such that relativeX = xMultiplier * tileSize;
     * @param yMultiplier Tile size relative Y multiplier such that relativeY = yMultiplier * tileSize;
     */
    @Override
    public void clickOnCurrentTile(double xMultiplier, double yMultiplier) {
        if (canFollowerBePlaced()) {
            placeFollower(xMultiplier, yMultiplier);
            return;
        } else {
            clickOffCurrentTile();
        }
    }

    @Override
    public void clickOnCurrentTile() {
        if (!isCurrentTileOnTheTable()) {
            return;
        }

        if (isTileConfirmed()) {
            return;
        }

        while (true) {
            currentTileRotation = Rotation.values()[(currentTileRotation.ordinal() + 1) % 4];
            if (possibleCurrentTileRotations.contains(currentTileRotation))
                break;
        }
        gameWindow.getCurrentTile().setRotation(currentTileRotation);
        gameWindow.repaintWindow();
    }

    @Override
    public void clickOffCurrentTile() {
        if (temporaryFollowerDisplayed) {
            temporaryFollowerDisplayed = false;
        }
    }
    /*
     * Methods that are invoked by action listeners of GameWindow
     * END
     */

    @Override
    public void finishGame() {
        gameWindow.setConfirmTileButtonEnabled(false);
        gameWindow.setEndTurnButtonEnabled(false);
        gameWindow.setTilePreviewEnabled(false);
        gameEnded = true;
    }

    @Override
    public void pull() {
        dataToModel.forceNotify();
    }

    @Override
    public boolean displayPossibleLocations() {
        return !gameEnded;
    }

    private void placeFollower(double xM, double yM) {
        currentFollowerLocation = new double [] {xM, yM};
        gameWindow.setCurrentFollowerLocation(currentFollowerLocation);
        temporaryFollowerDisplayed = true;
    }

    //<editor-fold desc="Getters and setters">
    @Override
    public int getCurrentTileX() {
        return currentTileX;
    }

    @Override
    public int getCurrentTileY() {
        return currentTileY;
    }

    @Override
    public boolean isCurrentTileOnTheTable() {
        return currentTileOnTheTable;
    }

    @Override
    public boolean isTileConfirmed() {
        return tileConfirmed;
    }

    @Override
    public GameData getLatestGameData() {
        return null;
    }

    @Override
    public boolean isFollowerPlaceDisplayed() {
        return tileConfirmed;
    }

    @Override
    public Color getCurrentPlayerColor() {
        return gameData.getPlayerColor();
    }

    @Override
    public boolean isTemporaryFollowerDisplayed() {
        return temporaryFollowerDisplayed;
    }

    @Override
    public boolean canFollowerBePlaced() {
        return canFollowerBePlaced;
    }

    @Override
    public double[] getCurrentFollowerLocation() {
        return currentFollowerLocation;
    }

    public void setDataToModel(DataToModel dataToModel) {
        this.dataToModel = dataToModel;
    }

    //</editor-fold>
}

