package carcassonne.view;

import carcassonne.controller.WindowLogic;
import carcassonne.model.Coordinates;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
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
    private GamePanel gamePanel;
    private Set<Coordinates> possibleTileLocations;
    private DrawableTile currentTile;

    public GameWindow(WindowLogic windowLogic) {
        super("Carcassonne");
        this.windowLogic = windowLogic;
        setContentPane(rootPanel);
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

    //<editor-fold desc="Setters used by controller">
    @Override
    public void setConfirmTileButtonEnabled(boolean b) {
        confirmTileButton.setEnabled(b);
    }

    @Override
    public void setConfirmTileButtonText(String text) {
        confirmTileButton.setText(text);
    }

    @Override
    public void setEndTurnButtonEnabled(boolean b) {
        endTurnButton.setEnabled(b);
    }

    @Override
    public void setPlayerColorRemainder(Color color) {
        playerColor.setBackground(color);
    }

    @Override
    public void setCurrentPlayerName(String currentPlayer) {
        this.currentPlayer.setText(currentPlayer);
    }

    @Override
    public void setTilesNumber(String tilesNumber) {
        this.tilesLeft.setText("Tiles left: " + tilesNumber);
    }

    @Override
    public void setNumberOfFollowers(String numberOfFollowers) {
        this.numberOfFollowers.setText("Followers: " + numberOfFollowers);
    }

    @Override
    public void setCurrentPoints(String currentPoints) {
        this.currentPoints.setText("Current points: " + currentPoints);
    }

    @Override
    public void addTileOnTable(DrawableTile tile) {
        gamePanel.addTileOnTable(tile);
    }


    public void setTilePreviewEnabled(boolean b) {
        tilePreviewEnabled = b;
    }

    @Override
    public void setPossibleTileLocations(Set<Coordinates> possibleTileLocations) {
        this.possibleTileLocations = possibleTileLocations;
    }

    @Override
    public void setCurrentTile(DrawableTile drawableTile) {
        this.currentTile = drawableTile;
    }
    //</editor-fold>

    @Override
    public void displayEndgameWindow() {
        gameEndWindow.setVisible(true);
    }

    @Override
    public void repaintWindow() {
        tilePreview.repaint();
        gamePanel.repaint();
    }

    private class TilePreview extends JPanel {

        TilePreview() {
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tilePreviewEnabled) {
                g.drawImage(currentTile.getBufferedImage(), 0, 0, null);
            }
        }
    }

    private class GamePanel extends JPanel {
        private javaxt.io.Image tileImage;
        private double windowLocalX = 235;
        private double windowLocalY = 244;
        private boolean firstMouseDrag = true;
        private int previousMouseX = 0, previousMouseY = 0;
        private double tileSize = 90;
        private int MIN_TILE_SIZE = 45;
        private int TILE_SIZE_VARIATION = 90;
        private Set<DrawableTile> tilesOnTable = new HashSet<>();
        private final int RECTANGLE_DIVIDER = 9;
        private double rectangleMargin = 10;
        private double previousScaleMultiplier = 2;

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

                    if (!windowLogic.isTileConfirmed()) {
                        if (windowLogic.isCurrentTileOnTheTable()) {
                            if ((e.getX() > windowLocalX + tileSize *  windowLogic.getCurrentTileX() &&
                                    e.getX() < (windowLocalX + tileSize *  windowLogic.getCurrentTileX() + tileSize)) &&
                                    (e.getY() > windowLocalY + tileSize *  windowLogic.getCurrentTileY() &&
                                            e.getY() < (windowLocalY + tileSize *  windowLogic.getCurrentTileY() + tileSize))) {
                                windowLogic.clickOnPlacedTile();
                                return;
                            }

                        }

                        for (Coordinates coordinates: possibleTileLocations) {
                            if ((e.getX() > windowLocalX + tileSize * coordinates.getX() &&
                                    e.getX() < (windowLocalX + tileSize * coordinates.getX() + tileSize)) &&
                                    (e.getY() > windowLocalY + tileSize * coordinates.getY() &&
                                            e.getY() < (windowLocalY + tileSize * coordinates.getY() + tileSize))) {
                                windowLogic.updateTilePlaced(coordinates.getX(), coordinates.getY());
                                break;
                            }
                        }
                        repaint();
                    }
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
            double windowWidth = this.getWidth();
            double windowHeight = this.getHeight();
            tileSize = MIN_TILE_SIZE + (int)(TILE_SIZE_VARIATION  * scale / 100);
            rectangleMargin = (tileSize / RECTANGLE_DIVIDER);

            double scaleMultiplier = 1 + 2 * (scale / 100);

            double offsetX = windowWidth / 2 - windowLocalX;
            double newOffsetX = offsetX / previousScaleMultiplier;
            newOffsetX *= scaleMultiplier;
            windowLocalX =  windowWidth / 2 - newOffsetX;


            double offsetY = windowHeight / 2 - windowLocalY;
            double newOffsetY = offsetY / previousScaleMultiplier;
            newOffsetY *= scaleMultiplier;
            windowLocalY = windowHeight / 2 - newOffsetY;

            previousScaleMultiplier = scaleMultiplier;
            repaint();
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
                g.drawImage(tile.getBufferedImage(),
                        (int) (windowLocalX + tileSize * tile.getX()),
                        (int) (windowLocalY + tileSize * tile.getY()),
                        (int) tileSize, (int) tileSize, null);
            }

            /*
             * Sometimes possibleTileLocations is null for a moment when the window is displayed.
             * The programme works ok after.
             */
            if (windowLogic.displayPossibleLocations() && possibleTileLocations != null) {
                for (Coordinates coordinates: possibleTileLocations) {
                    g.drawRect((int) (windowLocalX + tileSize * coordinates.getX() + rectangleMargin),
                            (int) (windowLocalY + tileSize * coordinates.getY()  + rectangleMargin),
                            (int) (tileSize - 2 * rectangleMargin),
                            (int) (tileSize - 2 * rectangleMargin));
                }
            }

            if (windowLogic.isCurrentTileOnTheTable()) {
                g.drawImage(currentTile.getBufferedImage(),
                        (int) (windowLocalX + tileSize * windowLogic.getCurrentTileX()),
                        (int) (windowLocalY + tileSize * windowLogic.getCurrentTileY()),
                        (int) tileSize, (int) tileSize, null);
            }

        }
    }
}
