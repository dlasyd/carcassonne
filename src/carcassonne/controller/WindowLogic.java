package carcassonne.controller;


import carcassonne.view.ViewWindow;

/**
 * Created by Andrey on 01/12/15.
 */
public interface WindowLogic {
    public void setGameWindow(ViewWindow gameWindow);

    void update(GameData gameData);

    GameData getLatestGameData();

    void updateTilePlaced(int x, int y);

    void updateTileConfirmed();

    boolean isTileFixed();

    public void setDataToModel(DataToModel dataToModel);

    void pull();

    void updateEndTurnButton(int x, int y);

    void finishGame();
}
