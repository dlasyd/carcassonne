package carcassonne.controller;


import carcassonne.model.Rotation;
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

    void setDataToModel(DataToModel dataToModel);

    void pull();

    void updateEndTurnButton();

    void finishGame();

    boolean displayPossibleLocations();

    int getCurrentTileX();

    int getCurrentTileY();

    boolean isCurrentTileOnTheTable();

    boolean isTileConfirmed();

    void clickOnPlacedTile();
}
