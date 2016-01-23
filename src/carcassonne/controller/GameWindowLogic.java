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
    private DrawableTile currentTile;
    private boolean lastTurn;
    private boolean tilePreviewEnabled = true;

    @Override
    public void setGameWindow(ViewWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void update(GameData gameData) {
        this.gameData = gameData;

        if(!gameEnded) {
            this.currentTile = new DrawableTile(gameData.getCurrentTile());
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
        gameWindow.setCurrentTile(currentTile);
        gameWindow.setTilePreviewEnabled(tilePreviewEnabled);
        gameWindow.setPossibleTileLocations(gameData.getPossibleTileLocations());
        gameWindow.addTileOnTable(new DrawableTile(gameData.getPreviouslyPlacedTile()));
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

    //TODO remove
    @Override
    public boolean isTileFixed() {
        return false;
    }

    @Override
    public void updateEndTurnButton() {
        dataToModel.turnActions(currentTileX, currentTileY);
        gameWindow.setEndTurnButtonEnabled(false);
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
        gameWindow.setEndTurnButtonEnabled(false);
        gameWindow.setTilePreviewEnabled(false);
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

    @Override
    public void clickOnPlacedTile() {
        currentTile.turnRight();
        gameWindow.repaintWindow();
    }


}

