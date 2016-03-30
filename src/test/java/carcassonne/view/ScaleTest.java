package carcassonne.view;

import carcassonne.model.tile.Coordinates;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * Created by Andrey on 20/01/16.
 */
public class ScaleTest {
    int windowLocalX, windowLocalY;
    int windowHeight;
    int windowWidth;
    double previousScale = 50;
    double previousScaleMultiplier = 2;

    public void scale(double scale) {
        //double scaleMultiplier = ((Math.pow((scale / 100), 2)) + 0.5 * (scale / 100) + 0.5); // 0,5 - 1.5
        double scaleMultiplier = 1 + 2 * (scale / 100); // 1 - 3

        int offsetX = windowWidth / 2 - windowLocalX;
        double newOffsetX = offsetX / previousScaleMultiplier;
        newOffsetX *= scaleMultiplier;
        windowLocalX = (int) (windowWidth / 2 - newOffsetX);


        int offsetY = windowHeight / 2 - windowLocalY;
        double newOffsetY = offsetY / previousScaleMultiplier;
        newOffsetY *= scaleMultiplier;
        windowLocalY = (int) (windowHeight / 2 - newOffsetY);

        previousScaleMultiplier = scaleMultiplier;
    }

    @Before
    public void setUp() {
        windowHeight = 400;
        windowWidth  = 400;
    }

    @Test
    public void localIsCenterThePointDoesNotMove() {
        windowLocalX = windowWidth / 2;
        windowLocalY = windowHeight / 2 ;
        Coordinates coordsBeforeScale = new Coordinates(windowLocalX, windowLocalY);
        scale(100);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        assertEquals(coordsBeforeScale, coordsAfterScale);
    }

    @Test
    public void localTopLeftScaleDecrease() {
        windowLocalX = 180;
        windowLocalY = 180;
        scale(100);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        Coordinates expectedCoordinates = new Coordinates(170, 170);
        assertEquals(expectedCoordinates, coordsAfterScale);
    }

    @Test
    public void lessTopLeftScaleDecrease() {
        windowLocalX = 180;
        windowLocalY = 180;
        scale(75);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        Coordinates expectedCoordinates = new Coordinates(175, 175);    //170, 170 on paper
        assertEquals(expectedCoordinates, coordsAfterScale);
    }

    @Test
    public void topLeftScaleIncrease() {
        windowLocalX = 180;
        windowLocalY = 180;
        scale(0);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        Coordinates expectedCoordinates = new Coordinates(190, 190);
        assertEquals(expectedCoordinates, coordsAfterScale);
    }


    @Test
    public void localTopLeftScaleConstant() {
        windowLocalX = 180;
        windowLocalY = 180;
        scale(50);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        Coordinates expectedCoordinates = new Coordinates(180, 180);
        assertEquals(expectedCoordinates, coordsAfterScale);
    }

    @Test
    public void topLeftScaleIncreaseMoveCoordsToCenterAndScaleBack() {
        windowLocalX = 180;
        windowLocalY = 180;
        scale(0);
        windowLocalX = 200;
        windowLocalY = 200;
        scale(50);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        Coordinates expectedCoordinates = new Coordinates(200, 200);
        assertEquals(expectedCoordinates, coordsAfterScale);
    }

    @Test
    public void localTopLeftScaleDecreaseAndBack() {
        windowLocalX = 180;
        windowLocalY = 180;
        scale(100);
        scale(50);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        Coordinates expectedCoordinates = new Coordinates(180, 180);
        assertEquals(expectedCoordinates, coordsAfterScale);
    }

    @Test
    public void topLeftScaleIncreaseToAndScaleBack() {
        windowLocalX = 180;
        windowLocalY = 180;
        scale(0);
        assertEquals(new Coordinates(190, 190), new Coordinates(windowLocalX, windowLocalY));
        scale(50);
        assertEquals(new Coordinates(180, 180), new Coordinates(windowLocalX, windowLocalY));
        scale(0);
        Coordinates coordsAfterScale = new Coordinates(windowLocalX, windowLocalY);
        Coordinates expectedCoordinates = new Coordinates(190, 190);
        assertEquals(expectedCoordinates, coordsAfterScale);
    }
}
