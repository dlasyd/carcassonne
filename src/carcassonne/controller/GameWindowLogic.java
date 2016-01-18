package carcassonne.controller;

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
    private int currentX, currentY;
    private boolean gameEnded = false;

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
        gameWindow.setNumberOfFollwers(gameData.getFollowers());
        gameWindow.setCurrentPoints(gameData.getPoints());
        gameWindow.setPlayerColorRemainder(gameData.getPlayerColor());
        gameWindow.setTilesNumber(gameData.getTilesLeft());
        gameWindow.setTilePreviewEnabled(true);
        gameWindow.setCurrentTileFileName(gameData.getCurrentTile().getFileName());
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
            currentX = x;
            currentY = y;
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

    @Override
    public boolean isTileFixed() {
        return false;
    }

    @Override
    public void updateEndTurnButton() {
        dataToModel.turnActions(currentX ,currentY);
        gameWindow.setEndTurnEnabled(false);
    }

    @Override
    public void finishGame() {
        gameWindow.setConfirmTileButtonEnabled(false);
        gameWindow.setEndTurnEnabled(false);
        gameWindow.displayEndgameWindow();
        gameEnded = true;
    }
}

