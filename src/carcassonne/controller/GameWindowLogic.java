package carcassonne.controller;

import carcassonne.model.Coordinates;
import carcassonne.model.Rotation;
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
    }

    @Override
    public void updateTilePlaced(int x, int y) {
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
    public void updateTileConfirmed() {
        assert (currentTileOnTheTable);
        if (tileConfirmed) {
            tileConfirmed = false;
            gameWindow.setConfirmTileButtonText("Confirm tile");
            gameWindow.setEndTurnButtonEnabled(false);
        } else {
            tileConfirmed = true;
            gameWindow.setConfirmTileButtonText("Relocate tile");
            gameWindow.setPossibleFollowerLocations(dataToModel.getPossibleFollowerLocations(currentTileX, currentTileY));
            gameWindow.setEndTurnButtonEnabled(true);
        }
        gameWindow.repaintWindow();
    }

    @Override
    public void clickOnPlacedTile() {
        while (true) {
            currentTileRotation = Rotation.values()[(currentTileRotation.ordinal() + 1) % 4];
            if (possibleCurrentTileRotations.contains(currentTileRotation))
                break;
        }
        gameWindow.getCurrentTile().setRotation(currentTileRotation);
        gameWindow.repaintWindow();
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
    public void placeFollower(double xM, double yM) {
        currentFollowerLocation = new double [] {xM, yM};
        gameWindow.setCurrentFollowerLocation(currentFollowerLocation);
        gameWindow.setTemporaryFollowerDisplayed(true);
    }

    @Override
    public void updateEndTurnButton() {
        dataToModel.turnActions(currentTileX, currentTileY, currentTileRotation);
        gameWindow.setEndTurnButtonEnabled(false);
        currentTileOnTheTable = false;
        currentTileRotation = Rotation.DEG_0;
        if (gameEnded)
            gameWindow.displayEndgameWindow();
    }

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

    public void setDataToModel(DataToModel dataToModel) {
        this.dataToModel = dataToModel;
    }

    //</editor-fold>
}

