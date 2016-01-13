package carcassonne.controller;

import carcassonne.view.GameWindow;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class DataCollector implements WindowLogic {
    private GameData latestGameData;



    @Override
    public void setGameWindow(GameWindow gameWindow) {

    }

    @Override
    public void updateGameInformation(GameData gameData) {
        latestGameData = gameData;
    }

    @Override
    public GameData getLatestGameData() {
        assert (latestGameData != null);
        return latestGameData;
    }

    @Override
    public void updateTilePlaced(int x, int y) {

    }

    @Override
    public void updateTileConfirmed() {

    }

    @Override
    public boolean isTileFixed() {
        return false;
    }

    @Override
    public void updateEndTurnButton() {

    }
}
