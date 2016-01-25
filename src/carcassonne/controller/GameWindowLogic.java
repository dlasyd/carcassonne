package carcassonne.controller;

import carcassonne.model.Rotation;
import carcassonne.view.DrawableTile;
import carcassonne.view.ViewWindow;

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
        if (!isTileFixed() && !gameEnded) {
            currentTileOnTheTable = true;
            gameWindow.setConfirmTileButtonEnabled(true);
            currentTileX = x;
            currentTileY = y;
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
            gameWindow.setEndTurnButtonEnabled(true);
        }
    }

    @Override
    public void clickOnPlacedTile() {
        currentTileRotation = Rotation.values()[(currentTileRotation.ordinal() + 1) % 4];
        gameWindow.getCurrentTile().turnRight();
        gameWindow.repaintWindow();
    }

    @Override
    public void updateEndTurnButton() {
        dataToModel.turnActions(currentTileX, currentTileY, currentTileRotation);
        gameWindow.setEndTurnButtonEnabled(false);
        currentTileOnTheTable = false;
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

    //TODO remove
    @Override
    public boolean isTileFixed() {
        return false;
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

