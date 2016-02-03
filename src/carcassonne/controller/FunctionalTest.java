package carcassonne.controller;

import carcassonne.model.*;
import carcassonne.view.FakeWindow;
import carcassonne.view.ViewWindow;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 02/02/16.
 */
public class FunctionalTest {
    FakeGame        fakeGame;
    Game            game;
    Table           table;
    WindowLogic     windowLogic;
    ViewWindow      viewWindow;
    FakeWindow      fakeWindow;

    @Before
    public final void setUp() {
        game = new Game();
        table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        game.setTable(table);
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);
    }

    public void prepareGame() {
        windowLogic = new GameWindowLogic();
        viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        game.nextPlayer();
        game.dragTile();
        game.notifyController();
        fakeWindow = (FakeWindow) viewWindow;
    }

    public void setUpFakeGame() {
        fakeGame = new FakeGame();

        table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        fakeGame.setTable(table);
        fakeGame.addPlayer("Anton" , Color.RED);
        fakeGame.addPlayer("Andrey", Color.YELLOW);
    }

    public void prepareFakeGame() {
        windowLogic = new GameWindowLogic();
        viewWindow = new FakeWindow(windowLogic);
        windowLogic.setGameWindow(viewWindow);
        windowLogic.setDataToModel(fakeGame);
        fakeGame.setWindowLogic(windowLogic);
        fakeGame.nextPlayer();
        fakeGame.dragTile();
        fakeGame.notifyController();
        fakeWindow = (FakeWindow) viewWindow;
    }

}