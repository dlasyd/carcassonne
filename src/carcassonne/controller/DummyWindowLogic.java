package carcassonne.controller;

import carcassonne.view.ViewWindow;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class DummyWindowLogic implements WindowLogic {
    private ViewWindow gameWindow;
    private boolean currentTileOnTheTable;
    private boolean tileConfirmed;
    private DataToModel dataToModel;
    private Map<DataType, String> interfaceData = new HashMap();
    private GameData gameData;

    @Override
    public void setGameWindow(ViewWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void update(GameData gameData) {
        interfaceData.put(DataType.NAME, gameData.getName());
        interfaceData.put(DataType.FOLLOWES, gameData.getFollowers());
        interfaceData.put(DataType.POINTS, gameData.getPoints());
        this.gameData = gameData;
        updateUI();
    }

    private void updateUI() {
        gameWindow.setCurrentPlayerName(interfaceData.get(DataType.NAME));
        gameWindow.setNumberOfFollwers(interfaceData.get(DataType.FOLLOWES));
        gameWindow.setCurrentPoints(interfaceData.get(DataType.POINTS));
        gameWindow.setPlayerColorRemainder(gameData.getPlayerColor());
        gameWindow.setTilesNumber(gameData.getTilesLeft());
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
        if (!isTileFixed()) {
            currentTileOnTheTable = true;
            gameWindow.setConfirmTileButtonEnabled(true);
        }
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
        dataToModel.turnActions(1 ,0);
        gameWindow.setEndTurnEnabled(false);
    }


    enum DataType {NAME, POINTS, FOLLOWES}
}

