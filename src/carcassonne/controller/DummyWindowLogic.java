package carcassonne.controller;

import carcassonne.view.GameWindow;

import java.awt.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class DummyWindowLogic implements WindowLogic {
    private GameWindow gameWindow;
    private int tiles = 72;
    private boolean currentTileOnTheTable;
    private boolean tileConfirmed;
    private String[] players = {"Anton", "Andrey"};
    private Color[] colors = {Color.ORANGE, Color.YELLOW};

    @Override
    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void updateGameInformation(GameData gameData) {

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
            tiles -= 1;
            gameWindow.setTilesNumber(tiles);
            gameWindow.setCurrentPlayer(players[tiles % 2]);
            gameWindow.setPlayerColorRemainder(colors[tiles % 2]);
            gameWindow.setEndTurnEnabled(false);
    }

}

