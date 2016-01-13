package carcassonne.view;

import carcassonne.controller.WindowLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 11/01/16.
 */
public class GameWindow extends JFrame{
    private JPanel rootPanel;
    private JPanel gameDrawer;
    private JPanel rightColumn;
    private JLabel tilesLeft;
    private JLabel currentPlayer;
    private JLabel numberOfFollowers;
    private JButton endTurnButton;
    private JPanel tilePreview;
    private JPanel playerColor;
    private JButton confirmTileButton;
    private WindowLogic windowLogic;

    public GameWindow(WindowLogic windowLogic) {
        super("Carcassonne");
        this.windowLogic = windowLogic;
        setContentPane(rootPanel);
        pack();
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowLogic.updateEndTurnButton();
            }
        });

        rootPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*
                 * If tile has been placed
                 * 1) save info
                 * 2) enable end move button
                 */
                windowLogic.updateTilePlaced(1, 0);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        confirmTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowLogic.updateTileConfirmed();
            }
        });
    }
    public void setConfirmTileButtonEnabled(boolean b) {
        confirmTileButton.setEnabled(b);
    }
    public void setConfirmTileButtonText(String text) {
        confirmTileButton.setText(text);
    }
    public void setPlayerColorRemainder(Color color) {
        playerColor.setBackground(color);
    }
    public void setTilesNumber(int tilesNumber) {
        this.tilesLeft.setText("Tiles left: " + tilesNumber);
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer.setText(currentPlayer);
    }

    public void setEndTurnEnabled(boolean b) {
        endTurnButton.setEnabled(b);
    }

}
