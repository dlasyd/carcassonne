package carcassonne.controller;

import carcassonne.model.Game;
import carcassonne.model.RealEstateManager;
import carcassonne.model.Table;
import carcassonne.view.GameWindow;

import java.awt.*;

/**
 * This class creates all necessary objects, does all the preparation and starts the game
 */
public class StartGame {
    public static void main(String[] args) {
        /*
         * prepare game
         */
        Game game = new Game();
        Table table = new Table();
        RealEstateManager manager = new RealEstateManager(table);
        table.setRealEstateManager(manager);
        game.setTable(table);
        game.addPlayer("Anton" , Color.RED);
        game.addPlayer("Andrey", Color.YELLOW);
        //game.getTilePile().addXCrossroads(5);
        game.getTilePile().add5DifferentTiles();
        game.nextPlayer();
        game.dragTile();

        /*
         * prepare mvc
         */
        WindowLogic windowLogic = new GameWindowLogic();

        /*
         * Should be done before creating a window instance
         */
        windowLogic.setDataToModel(game);
        game.setWindowLogic(windowLogic);
        GameWindow window = new GameWindow(windowLogic);
        windowLogic.setGameWindow(window);
        windowLogic.pull();
    }
}
