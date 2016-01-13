package carcassonne.controller;


import carcassonne.view.GameWindow;

/**
 * Created by Andrey on 01/12/15.
 */
public interface WindowLogic {
    public void setGameWindow(GameWindow gameWindow);

    void updateGameInformation(GameData gameData);

    GameData getLatestGameData();

    void updateTilePlaced(int x, int y);

    void updateTileConfirmed();

    boolean isTileFixed();

    void updateEndTurnButton();
}
