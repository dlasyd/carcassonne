package carcassonne.controller;

import carcassonne.view.DrawableTile;
import carcassonne.view.ViewWindow;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class GameWindowLogic implements WindowLogic {
    private ViewWindow gameWindow;
    private boolean currentTileOnTheTable;
    private boolean tileConfirmed;
    private DataToModel dataToModel;
    private GameData gameData;
    private int currentTileX, currentTileY;
    private boolean gameEnded = false;
    private boolean lastTurn = false;

    @Override
    public void setGameWindow(ViewWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void update(GameData gameData) {
        this.gameData = gameData;
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
        gameWindow.setTilePreviewEnabled(true);
        gameWindow.setCurrentTileFileName(new DrawableTile(gameData.getCurrentTile()).getFileName());
        gameWindow.setCurrentTile(new DrawableTile(gameData.getCurrentTile()));
        gameWindow.addTileOnTable(new DrawableTile(gameData.getPreviouslyPlacedTile()));
        gameWindow.setPossibleTileLocations(gameData.getPossibleTileLocations());
    }

    public void setDataToModel(DataToModel dataToModel) {
        this.dataToModel = dataToModel;
    }

    @Override
    public void pull() {
        dataToModel.forceNotify();
    }

    @Override
    public GameData getLatestGameData() {
        return null;
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
        gameWindow.repaintWindow();
    }

    @Override
    public void updateTileConfirmed() {
        assert (currentTileOnTheTable);
        if (tileConfirmed) {
            tileConfirmed = false;
            gameWindow.setConfirmTileButtonText("Confirm tile");
            gameWindow.setEndTurnEnabled(false);
        } else {
            tileConfirmed = true;
            gameWindow.setConfirmTileButtonText("Relocate tile");
            gameWindow.setEndTurnEnabled(true);
        }
    }

    //TODO removed
    @Override
    public boolean isTileFixed() {
        return false;
    }

    @Override
    public void updateEndTurnButton() {
        dataToModel.turnActions(currentTileX,currentTileY);
        gameWindow.setEndTurnEnabled(false);
        currentTileOnTheTable = false;
        if (gameEnded)
            gameWindow.displayEndgameWindow();
    }

    @Override
    public void setLastTurn(Boolean value) {
        lastTurn = value;
    }

    @Override
    public void finishGame() {
        gameWindow.setConfirmTileButtonEnabled(false);
        gameWindow.setEndTurnEnabled(false);
        gameEnded = true;
    }

    @Override
    public boolean displayPossibleLocations() {
        return !gameEnded;
    }

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


}

