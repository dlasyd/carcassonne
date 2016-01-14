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
public class GameWindow extends JFrame implements ViewWindow{
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
    private JLabel currentPoints;
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

    @Override
    public void setConfirmTileButtonEnabled(boolean b) {
        confirmTileButton.setEnabled(b);
    }

    @Override
    public void setConfirmTileButtonText(String text) {
        confirmTileButton.setText(text);
    }

    @Override
    public void setPlayerColorRemainder(Color color) {
        playerColor.setBackground(color);
    }

    @Override
    public void setTilesNumber(String tilesNumber) {
        this.tilesLeft.setText("Tiles left: " + tilesNumber);
    }

    @Override
    public void setCurrentPlayerName(String currentPlayer) {
        this.currentPlayer.setText(currentPlayer);
    }

    @Override
    public void setEndTurnEnabled(boolean b) {
        endTurnButton.setEnabled(b);
    }

    @Override
    public void setNumberOfFollwers(String numberOfFollwers) {
        this.numberOfFollowers.setText("Followers: " + numberOfFollwers);
    }

    @Override
    public void setCurrentPoints(String currentPoints) {
        this.currentPoints.setText("Current points: " + currentPoints);
    }
}
