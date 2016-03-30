package carcassonne.view;

import carcassonne.controller.WindowLogic;
import carcassonne.model.tile.Coordinates;
import carcassonne.model.tile.Rotation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This is a part of Carcassonne project.
 * The project is created for learning and practicing java
 * and not intended for distribution.
 * Created by Andrey on 11/01/16.
 */
public class GameWindow implements ViewWindow {

    private final WindowLogic windowLogic;

    private final JFrame mainFrame = new JFrame();
    private final JPanel rightPanel = new JPanel();
    private final JPanel rightContainer = new JPanel();
    private final JDialog gameEndWindow = new GameEndWindow();

    private final JSlider scaleSlider = new JSlider();

    private final JPanel tilePreview = new TilePreview();
    private final GamePanel gamePanel = new GamePanel();

    private final TableModel tableData = new CurrentScoresTable();
    private final JTable playersStatsTable = new JTable(tableData);
    private final JScrollPane tableScrollPane = new JScrollPane(playersStatsTable);

    private final JLabel labelTilesLeft = new JLabel();
    private final JLabel labelCurrentPlayer = new JLabel();
    private final JLabel labelPoints = new JLabel();
    private final JLabel labelFollowers = new JLabel();

    private final JButton buttonConfirm = new JButton();
    private final JButton buttonEndTurn = new JButton();

    private DrawableTile currentTile;
    private boolean tilePreviewEnabled;
    private Set<Coordinates> possibleTileLocations;
    private Set<double[]> followerPossibleLocations;
    private Set<DrawablePlacedFollower> drawablePlacedFollowers = new HashSet<>();

    public GameWindow(WindowLogic windowLogic) {
        this.windowLogic = windowLogic;

        setupLayout();
        setupLabels();
        setupButtons();
        setupScoreTable();
        setupListeners();
        setupWindow();
    }

    private void setupScoreTable() {
        playersStatsTable.setPreferredScrollableViewportSize(new Dimension(100, 80));   //too wide without this line
    }

    private void setupListeners() {
        setListenersToButtons();
        setMouseWheelListener();
        setSliderListener();
    }

    private void setSliderListener() {
        scaleSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            gamePanel.changeScale(source.getValue());
        });
    }

    private void setMouseWheelListener() {
        MouseWheelListener wheelListener = (event) -> {
            int value = scaleSlider.getValue();
            value += 2 * event.getWheelRotation();
            scaleSlider.setValue(value);
        };
        mainFrame.addMouseWheelListener(wheelListener);
    }

    private void setListenersToButtons() {
        buttonConfirm.addActionListener(event -> windowLogic.updateTileConfirmedButton());
        buttonEndTurn.addActionListener(event -> windowLogic.updateEndTurnButton());
    }

    private void setupButtons() {
        buttonConfirm.setFont(new Font("sans-serif", Font.PLAIN, 18));
        buttonEndTurn.setFont(new Font("sans-serif", Font.PLAIN, 18));
        buttonConfirm.setText("Confirm tile");
        buttonEndTurn.setText("End turn");
        buttonConfirm.setEnabled(false);
        buttonEndTurn.setEnabled(false);
        buttonEndTurn.setPreferredSize(new Dimension(170, 50));
        buttonConfirm.setPreferredSize(new Dimension(170, 50));
    }

    private void setupLabels() {
        labelsDefaultText();
        labelsFontStyle();
    }

    private void labelsFontStyle() {
        labelTilesLeft.setFont(new Font("sans-serif", Font.PLAIN, 14));
        labelCurrentPlayer.setFont(new Font("sans-serif", Font.PLAIN, 14));
        labelPoints.setFont(new Font("sans-serif", Font.PLAIN, 14));
        labelFollowers.setFont(new Font("sans-serif", Font.PLAIN, 14));
    }

    private void labelsDefaultText() {
        labelTilesLeft.setText("Tiles left: ERROR");
        labelCurrentPlayer.setText("ERROR is current player");
        labelPoints.setText("Points: ERROR");
        labelFollowers.setText("Followers: ERROR");
    }

    private void setupWindow() {
        mainFrame.setName("Carcassonne");
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    //<editor-fold desc="Setters from ViewWindow interface">
    @Override
    public void setConfirmTileButtonEnabled(boolean b) {
        buttonConfirm.setEnabled(b);
    }

    @Override
    public void setConfirmTileButtonText(String text) {
        buttonConfirm.setText(text);
    }

    @Override
    public void setPlayerColorRemainder(Color color) {
        //TODO implement
    }

    @Override
    public void setTilesNumber(String tilesNumber) {
        labelTilesLeft.setText(tilesNumber);
    }

    @Override
    public void setCurrentPlayerName(String currentPlayerName) {
        labelCurrentPlayer.setText(currentPlayerName);
    }

    @Override
    public void setEndTurnButtonEnabled(boolean b) {
        buttonEndTurn.setEnabled(b);
    }

    @Override
    public void setNumberOfFollowers(String numberOfFollowers) {
        labelFollowers.setText(numberOfFollowers);
    }

    @Override
    public void setCurrentPoints(String currentPoints) {
        labelPoints.setText(currentPoints);
    }

    @Override
    public void addTileOnTable(DrawableTile tile) {
        gamePanel.addTileOnTable(tile);
    }
    //</editor-fold>

    private void setupLayout() {
        mainFrame.getContentPane().add(BorderLayout.CENTER, gamePanel);
        mainFrame.getContentPane().add(rightPanel, BorderLayout.EAST);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        rightPanel.add(rightContainer, BorderLayout.NORTH);

        rightContainer.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        rightContainer.add(labelTilesLeft, nextRow(c, 5, 2, 10, 2));
        rightContainer.add(tilePreview, nextRow(c, 0, 0, 0, 0));
        rightContainer.add(labelCurrentPlayer, nextRow(c, 10, 2, 0, 2));
        rightContainer.add(labelPoints, nextRow(c, 5, 2, 0, 2));
        rightContainer.add(labelFollowers, nextRow(c, 5, 2, 0, 2));
        rightContainer.add(tableScrollPane, nextRow(c, 15, 2, 0, 2));
        rightContainer.add(buttonConfirm, nextRow(c, 10, 0, 0, 0));
        rightContainer.add(buttonEndTurn, nextRow(c, 100, 0, 0, 0));
    }

    private GridBagConstraints nextRow(GridBagConstraints c, int top, int left, int bottom, int right) {
        c.gridy++;
        c.insets = new Insets(top, left, bottom, right);
        return c;
    }

    @Override
    public void setTableRowValues(int row, String[] data) {
        tableData.setValueAt(data[0], row, 0);
        tableData.setValueAt(data[1], row, 1);
        tableData.setValueAt(data[2], row, 2);
    }

    @Override
    public void displayEndgameWindow() {

    }

    @Override
    public void setTilePreviewEnabled(boolean b) {
        tilePreviewEnabled = b;
        repaintWindow();
    }

    @Override
    public void repaintWindow() {
        rightPanel.revalidate();
        mainFrame.repaint();
    }

    @Override
    public void setPossibleTileLocations(Set<Coordinates> possibleTileLocations) {
        this.possibleTileLocations = possibleTileLocations;
    }

    @Override
    public DrawableTile getCurrentTile() {
        return currentTile;
    }

    @Override
    public void setCurrentTile(DrawableTile drawableTile) {
        this.currentTile = drawableTile;
    }

    @Override
    public void setPossibleFollowerLocations(Set<double[]> followerLocations) {
        followerPossibleLocations = followerLocations;
    }

    @Override
    public void setDrawablePlacedFollowersSet(Set<DrawablePlacedFollower> drawablePlacedFollowers) {
        this.drawablePlacedFollowers = drawablePlacedFollowers;
    }


    private class GamePanel extends JPanel {
        private final int MIN_TILE_SIZE = 45;
        private final int TILE_SIZE_VARIATION = 90;
        private final Set<DrawableTile> tilesOnTable = new HashSet<>();
        private final int RECTANGLE_DIVIDER = 9;            // used to compute rectangleMargin of possible tile rectangle
        private double windowLocalX = 235;
        private double windowLocalY = 244;
        private double previousScaleMultiplier = 2;
        private double tileSize = 90;
        private boolean clickBeforeDragHappened = false;
        private int previousMouseX = 0, previousMouseY = 0;
        private double rectangleMargin = tileSize / RECTANGLE_DIVIDER;

        GamePanel() {
            this.setBackground(Color.GRAY);
            setupMouseListener();
            setupMouseMotionListener();
        }

        private void setupMouseMotionListener() {
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    if (!clickBeforeDragHappened) {
                        clickBeforeDragHappened = true;
                        previousMouseX = mouseX;
                        previousMouseY = mouseY;
                    } else {
                        windowLocalX -= previousMouseX - mouseX;
                        previousMouseX = mouseX;
                        windowLocalY -= previousMouseY - mouseY;
                        previousMouseY = mouseY;
                    }
                    repaintWindow();
                }

            });
        }

        private void setupMouseListener() {
            this.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    checkClickOnCurrentTile(e);
                    checkClickOnPossibleLocations(e);
                    checkClickOnFollowerLocations(e);
                    repaintWindow();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    clickBeforeDragHappened = false;
                }

                private void checkClickOnFollowerLocations(MouseEvent e) {
                    if (followerPossibleLocations != null) {
                        for (double[] followerPosition : followerPossibleLocations) {
                            double[] rotatedFollowerPosition =
                                    rotateMultipliers(followerPosition, currentTile.getRotation());
                            if (clickedOnPossibleFollowerLocation(e, rotatedFollowerPosition)) {
                                windowLogic.clickOnCurrentTile(followerPosition[0], followerPosition[1]);
                            }
                        }
                    }
                }

                private void checkClickOnPossibleLocations(MouseEvent e) {
                    for (Coordinates coordinates : possibleTileLocations) {
                        if (clickedOnPossibleLocation(e, coordinates)) {
                            windowLogic.clickOnPossibleLocation(coordinates.getX(), coordinates.getY());
                            break;
                        }
                    }
                }

                private void checkClickOnCurrentTile(MouseEvent e) {
                    if (currentTileClicked(e)) {
                        windowLogic.clickOnCurrentTile();
                    } else {
                        windowLogic.clickOffCurrentTile();
                    }
                }

                private boolean clickedOnPossibleFollowerLocation(MouseEvent e, double[] rotatedFollowerPosition) {
                    return (e.getX() > windowLocalX + tileSize * windowLogic.getCurrentTileX()
                            + tileSize * rotatedFollowerPosition[0] - 12) &&
                            (e.getX() < windowLocalX + tileSize * windowLogic.getCurrentTileX()
                                    + tileSize * rotatedFollowerPosition[0] + 12) &&
                            (e.getY() > windowLocalY + tileSize * windowLogic.getCurrentTileY()
                                    + tileSize * rotatedFollowerPosition[1] - 12) &&
                            (e.getY() < windowLocalY + tileSize * windowLogic.getCurrentTileY()
                                    + tileSize * rotatedFollowerPosition[1] + 12);
                }

                private boolean clickedOnPossibleLocation(MouseEvent e, Coordinates coordinates) {
                    return (e.getX() > windowLocalX + tileSize * coordinates.getX() &&
                            e.getX() < (windowLocalX + tileSize * coordinates.getX() + tileSize)) &&
                            (e.getY() > windowLocalY + tileSize * coordinates.getY() &&
                                    e.getY() < (windowLocalY + tileSize * coordinates.getY() + tileSize));
                }

                private boolean currentTileClicked(MouseEvent e) {
                    return (e.getX() > getXOffset() &&
                            e.getX() < (windowLocalX + tileSize * windowLogic.getCurrentTileX() + tileSize)) &&
                            (e.getY() > getYOffset() &&
                                    e.getY() < (windowLocalY + tileSize * windowLogic.getCurrentTileY() + tileSize));
                }

                //<editor-fold desc="Empty implementations">
                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
                //</editor-fold>
            });
        }


        private void changeScale(double scale) {
            double windowWidth = this.getWidth();
            double windowHeight = this.getHeight();
            tileSize = MIN_TILE_SIZE + (int) (TILE_SIZE_VARIATION * scale / 100);
            rectangleMargin = (tileSize / RECTANGLE_DIVIDER);

            /*
             * Everything below moves the windowLocalX and Y in such way that
             * when scale happens, center pixel stays in the center
             */
            double scaleMultiplier = 1 + 2 * (scale / 100);

            double offsetX = windowWidth / 2 - windowLocalX;
            double newOffsetX = offsetX / previousScaleMultiplier;
            newOffsetX *= scaleMultiplier;
            windowLocalX = windowWidth / 2 - newOffsetX;


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
            Graphics2D g2 = (Graphics2D) g;

            for (DrawableTile tile : tilesOnTable) {
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
                for (Coordinates coordinates : possibleTileLocations) {
                    g.drawRect((int) (windowLocalX + tileSize * coordinates.getX() + rectangleMargin),
                            (int) (windowLocalY + tileSize * coordinates.getY() + rectangleMargin),
                            (int) (tileSize - 2 * rectangleMargin),
                            (int) (tileSize - 2 * rectangleMargin));
                }
            }

            if (windowLogic.isCurrentTileOnTheTable()) {
                g.drawImage(currentTile.getBufferedImage(),
                        (int) (getXOffset()),
                        (int) (getYOffset()),
                        (int) tileSize, (int) tileSize, null);
            }

            if (windowLogic.isFollowerPlaceDisplayed()) {
                g.setColor(windowLogic.getCurrentPlayerColor());
                g2.setStroke(new BasicStroke(2));
                for (double[] xyMultipliers : followerPossibleLocations) {
                    drawFollowerPossibleLocation(g, xyMultipliers);
                }
            }

            if (windowLogic.isTemporaryFollowerDisplayed()) {
                drawPlacedFollower(g, windowLogic.getCurrentFollowerLocation());
            }

            for (DrawablePlacedFollower follower : drawablePlacedFollowers) {
                g.setColor(follower.getColor());
                double[] xyMultipliers = rotateMultipliers(follower.getXyMultipliers(), follower.getRotation());
                drawPreviouslyPlacedFollower(g, xyMultipliers, follower.getTileX(), follower.getTileY());
            }
        }

        private double getYOffset() {
            return windowLocalY + tileSize * windowLogic.getCurrentTileY();
        }

        private double getXOffset() {
            return windowLocalX + tileSize * windowLogic.getCurrentTileX();
        }



        private void drawFollowerPossibleLocation(Graphics g, double[] xyMultipliers) {
            xyMultipliers = rotateMultipliers(xyMultipliers, currentTile.getRotation());
            double circleDiameter = tileSize / 4;
            double circleRadius = tileSize / 8;
            g.drawOval((int) (getXOffset()
                            + tileSize * xyMultipliers[0] - circleRadius),
                    (int) (getYOffset()
                            + tileSize * xyMultipliers[1] - circleRadius),
                    (int) circleDiameter, (int) circleDiameter);
        }

        private void drawPlacedFollower(Graphics g, double[] xyMultipliers) {
            xyMultipliers = rotateMultipliers(xyMultipliers, currentTile.getRotation());
            double circleDiameter = tileSize / 4;
            double circleRadius = tileSize / 8;
            g.fillOval((int) (windowLocalX + tileSize * windowLogic.getCurrentTileX()
                            + tileSize * xyMultipliers[0] - circleRadius),
                    (int) (windowLocalY + tileSize * windowLogic.getCurrentTileY()
                            + tileSize * xyMultipliers[1] - circleRadius),
                    (int) circleDiameter, (int) circleDiameter);
        }

        private void drawPreviouslyPlacedFollower(Graphics g, double[] xyMultipliers, int tileX, int tileY) {
            double circleDiameter = tileSize / 4;
            double circleRadius = tileSize / 8;
            g.fillOval((int) (windowLocalX + tileSize * tileX + tileSize * xyMultipliers[0] - circleRadius),
                    (int) (windowLocalY + tileSize * tileY + tileSize * xyMultipliers[1] - circleRadius),
                    (int) circleDiameter, (int) circleDiameter);
        }

        private double[] rotateMultipliers(double[] xyMultipliers, Rotation angle) {
            switch (angle) {
                case DEG_0:
                    return xyMultipliers;
                case DEG_90:
                    return new double[]{1 - xyMultipliers[1], xyMultipliers[0]};
                case DEG_180:
                    return new double[]{1 - xyMultipliers[0], 1 - xyMultipliers[1]};
                case DEG_270:
                    return new double[]{xyMultipliers[1], 1 - xyMultipliers[0]};
            }
            return xyMultipliers;
        }

    }

    private class TilePreview extends JPanel {
        TilePreview() {
            this.setPreferredSize(new Dimension(170, 90));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tilePreviewEnabled) {
                g.drawImage(currentTile.getBufferedImage(), 0, 0, null);
            }
        }
    }

    private class CurrentScoresTable extends AbstractTableModel {
        private final String[] columnNames = {"Player", "Followers", "Points"};
        private final ArrayList<String[]> rowData = new ArrayList<>(Arrays.asList(new String[]{
                "Player1", "-1", "-1"
        }, new String[]{
                "Player2", "-1", "-1"
        }));

        @Override
        public int getRowCount() {
            return rowData.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return rowData.get(rowIndex)[columnIndex];
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            rowData.get(row)[col] = value.toString();
        }
    }
}