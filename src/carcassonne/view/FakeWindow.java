package carcassonne.view;

import carcassonne.controller.WindowLogic;

import java.awt.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 14/01/16.
 */
public class FakeWindow implements ViewWindow {
    String currentPlayerName = "";
    private WindowLogic windowLogic;
    private String currentPoints;
    private String numberOfFollwers;
    private String tilesLeft;
    public FakeWindow(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;
    }

    @Override
    public void setConfirmTileButtonEnabled(boolean b) {

    }

    @Override
    public void setConfirmTileButtonText(String text) {

    }

    @Override
    public void setPlayerColorRemainder(Color color) {

    }

    @Override
    public void setTilesNumber(String tilesNumber) {
        this.tilesLeft = tilesNumber;
    }



    @Override
    public void setCurrentPlayerName(String currentPlayer) {
        this.currentPlayerName = currentPlayer;
    }

    @Override
    public void setEndTurnEnabled(boolean b) {

    }

    @Override
    public void setNumberOfFollwers(String numberOfFollwers) {
        this.numberOfFollwers = numberOfFollwers;
    }

    @Override
    public void setCurrentPoints(String currentPoints) {
        this.currentPoints = currentPoints;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void pressEndTurnButton() {
        windowLogic.updateEndTurnButton();
    }

    public String getCurrentPoints() {
        return currentPoints;
    }

    public String getNumberOfFollwers() {
        return numberOfFollwers;
    }

    public String getTilesLeft() {
        return tilesLeft;
    }
}
