package carcassonne.view;

import carcassonne.controller.WindowLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 11/01/16.
 */
public class GameWindow extends JFrame implements ViewWindow{
    private final WindowLogic windowLogic;
    private JPanel rootPanel;
    private JPanel gameDrawer;
    private JPanel rightColumn;
    private JLabel tilesLeft;
    private JLabel currentPlayer;
    private JLabel numberOfFollowers;
    private JButton endTurnButton;
    private JPanel tilePreviewArea;
    private JPanel playerColor;
    private JButton confirmTileButton;
    private JLabel currentPoints;
    private int endTurnButtonPressed = 1;
    private JDialog gameEndWindow = new GameEndWindow();
    private boolean tilePreviewEnabled;
    private JPanel tilePreview;
    private DrawableTile currentTile;

    public GameWindow(WindowLogic windowLogic) {
        super("Carcassonne");
        this.windowLogic = windowLogic;
        setContentPane(rootPanel);
        pack();
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        tilePreview = new TilePreview();
        tilePreviewArea.add(tilePreview);

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowLogic.updateEndTurnButton();
                endTurnButtonPressed++;
            }
        });

        gameDrawer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*
                 * If tile has been placed
                 * 1) save info
                 * 2) enable end move button
                 */
                windowLogic.updateTilePlaced(endTurnButtonPressed, 0);
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

        setVisible(true);
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

    @Override
    public void displayEndgameWindow() {
        gameEndWindow.setVisible(true);
    }

    public void setTilePreviewEnabled(boolean b) {
        tilePreviewEnabled = b;
    }

    @Override
    public void repaintWindow() {
        tilePreview.repaint();
    }

    @Override
    public void setCurrentTile(DrawableTile currentTile) {
        this.currentTile = currentTile;
    }

    private class TilePreview extends JPanel {
        private javaxt.io.Image tileImage;

        TilePreview() {
            tileImage = new javaxt.io.Image(new File("res/tiles/road4.png"));
            assert (tileImage.getBufferedImage() != null) : "image was not loaded";
        }


        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tilePreviewEnabled) {
                g.drawImage(tileImage.getBufferedImage(), 0, 0, null);
            }

        }
    }

    private class GamePanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {

        }
    }
}
