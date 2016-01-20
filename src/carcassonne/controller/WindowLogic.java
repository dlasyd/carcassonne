package carcassonne.controller;


import carcassonne.view.ViewWindow;

/**
 * Created by Andrey on 01/12/15.
 */
public interface WindowLogic {
    void setGameWindow(ViewWindow gameWindow);

    void update(GameData gameData);

    GameData getLatestGameData();

    void updateTilePlaced(int x, int y);

    void updateTileConfirmed();

    boolean isTileFixed();

    void setDataToModel(DataToModel dataToModel);

    void pull();

    void updateEndTurnButton();

    void setLastTurn(Boolean value);

    void finishGame();

    boolean displayPossibleLocations();
}
