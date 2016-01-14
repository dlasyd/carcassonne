package carcassonne.controller;

import carcassonne.view.GameWindow;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 12/01/16.
 */
public class StartGame {
    public static void main(String[] args) {
        WindowLogic windowLogic = new DummyWindowLogic();
        GameWindow window = new GameWindow(windowLogic);
        windowLogic.setGameWindow(window);

    }
}
