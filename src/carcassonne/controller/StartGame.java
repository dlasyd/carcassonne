package carcassonne.controller;

import carcassonne.model.*;
import carcassonne.model.realEstate.RealEstateManager;
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
        //game.getTilePile().add5DifferentTiles();
        //game.getTilePile().addTile(CITY1, CITY1RSE, CITY1RSW, CITY1RWE, CITY1RSWE);
        //game.getTilePile().addTile(CITY3, CITY3SR, CITY3S, CITY3R);
        //game.getTilePile().addTile(CITY1RSE, CITY11NE, CITY2NWR, CITY11WE, CITY1RSE, CLOISTER, ROAD3, CITY2NWSR);
        //game.getTilePile().addEveryTileOnce();
        game.getTilePile().addRealTileSet();
        //game.getTilePile().setNonRandom(true);
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
