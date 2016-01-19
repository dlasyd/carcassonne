package carcassonne.view;

import carcassonne.controller.WindowLogic;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 11/01/16.
 */
public class GameWindow extends JFrame implements ViewWindow{
    private final WindowLogic windowLogic;
    private JPanel rootPanel;
    private JPanel gamePanelArea;
    private JPanel rightColumn;
    private JLabel tilesLeft;
    private JLabel currentPlayer;
    private JLabel numberOfFollowers;
    private JButton endTurnButton;
    private JPanel tilePreviewArea;
    private JPanel playerColor;
    private JButton confirmTileButton;
    private JLabel currentPoints;
    private JSlider scaleSlider;
    private int endTurnButtonPressed = 1;
    private JDialog gameEndWindow = new GameEndWindow();
    private boolean tilePreviewEnabled;
    private JPanel tilePreview;
    private String currentTileFileName;
    private GamePanel gamePanel;

    public GameWindow(WindowLogic windowLogic) {
        super("Carcassonne");
        this.windowLogic = windowLogic;
        setContentPane(rootPanel);
        //pack();
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        tilePreview = new TilePreview();
        tilePreviewArea.add(tilePreview);
        gamePanel = new GamePanel();
        gamePanelArea.add(gamePanel);

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowLogic.updateEndTurnButton();
                endTurnButtonPressed++;
            }
        });

        confirmTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowLogic.updateTileConfirmed();
            }
        });

        setVisible(true);
        scaleSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                gamePanel.changeScale(source.getValue());
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
    public void setNumberOfFollowers(String numberOfFollwers) {
        this.numberOfFollowers.setText("Followers: " + numberOfFollwers);
    }

    @Override
    public void setCurrentPoints(String currentPoints) {
        this.currentPoints.setText("Current points: " + currentPoints);
    }

    @Override
    public void addTileOnTable(DrawableTile tile) {
        gamePanel.addTileOnTable(tile);
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
    public void setCurrentTileFileName(String currentTileFileName) {
        this.currentTileFileName = currentTileFileName;
        ((TilePreview) tilePreview).loadTileImage(currentTileFileName);
    }

    private class TilePreview extends JPanel {
        private javaxt.io.Image tileImage;

        TilePreview() {
            loadTileImage(currentTileFileName);
        }

        void loadTileImage(String imageFileName) {
            tileImage = new javaxt.io.Image(new File("res/tiles/" + imageFileName));
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
        private javaxt.io.Image tileImage;
        private int windowLocalX = 200;
        private int windowLocalY = 200;
        private boolean firstMouseDrag = true;
        private int previousMouseX = 0, previousMouseY = 0;
        private int tileSize = 90;
        private int previousTileSize = tileSize;
        private int MIN_TILE_SIZE = 45;
        private int TILE_SIZE_VARIATION = 90;
        private Set<DrawableTile> tilesOnTable = new HashSet<>();

        GamePanel() {
            this.setBackground(Color.GRAY);
            this.addMouseListener(new MouseListener() {

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
                    firstMouseDrag = true;
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    if (firstMouseDrag) {
                        /*
                         * when mouse is clicked but not yet moved
                         * windowLocal coordinates should not change
                         */
                        firstMouseDrag = false;
                        previousMouseX = mouseX;
                        previousMouseY = mouseY;
                    } else {
                        windowLocalX -= previousMouseX - mouseX;
                        previousMouseX = mouseX;
                        windowLocalY -= previousMouseY - mouseY;
                        previousMouseY = mouseY;
                    }

                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                }
            });
        }

        void changeScale(double scale) {
            tileSize = MIN_TILE_SIZE + (int)(TILE_SIZE_VARIATION  * scale / 100);
            //TODO scale relative to window center
            windowLocalX += previousTileSize - tileSize;
            windowLocalY += previousTileSize - tileSize;
            repaint();
            previousTileSize = tileSize;
        }

        void addTileOnTable(DrawableTile tile) {
            if (tile.noCoordinates())
                throw new RuntimeException("Cannot draw placed tile with no coordinates");
            tilesOnTable.add(tile);
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (DrawableTile tile: tilesOnTable) {
                tileImage = new javaxt.io.Image(new File("res/tiles/" + tile.getFileName()));
                g.drawImage(tileImage.getBufferedImage(),
                        windowLocalX + tileSize * tile.getX(), windowLocalY + tileSize * tile.getY(), tileSize, tileSize, null);
            }
        }
    }
}
