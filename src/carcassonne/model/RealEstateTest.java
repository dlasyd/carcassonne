package carcassonne.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 04/12/15.
 */
public class RealEstateTest {
    public Tile tile;
    public RealEstate realEstate;

    public Feature completeCrossroads(Tile tile, boolean... returnEast) {
        Feature resultWEST = new Feature();
        Feature resultEAST = new Feature();
        tile.addProperty(resultEAST, TileDirections.EAST);
        tile.addProperty(resultWEST, TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);
        tile.addProperty(new Feature(), TileDirections.NORTH);
        tile.addProperty(new Feature(), TileDirections.CENTER);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);
        if (returnEast.length > 0)
            return resultEAST;
        return resultWEST;
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        realEstate = new RealEstate();
    }

    @Test
    public void addTileNoCoordinatesThenException() {
        exception.expect(RuntimeException.class);
        Tile tile = new Tile();
        completeCrossroads(tile);
        realEstate.addTile(tile);
    }

    @Test
    public void addTileIncompleteThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile = new Tile(1, 1);
        tile.addProperty(new Feature(), TileDirections.WEST);
        tile.addProperty(new Feature(), TileDirections.SOUTH);

        tile.addProperty(new Feature(), TileDirections.CENTER);
        tile.addProperty(new Feature(), TileDirections.WWN, TileDirections.NNW);
        tile.addProperty(new Feature(), TileDirections.NNE, TileDirections.EEN);
        tile.addProperty(new Feature(), TileDirections.WWS, TileDirections.SSW);
        tile.addProperty(new Feature(), TileDirections.EES, TileDirections.SSE);
        realEstate.addTile(tile);
    }

    @Test
    public void addTileSameCoordinatesThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = new Tile(0, 0);
        Tile tile2 = new Tile(0, 0);
        completeCrossroads(tile1);
        completeCrossroads(tile2);
        realEstate.addTile(tile1);
        realEstate.addTile(tile2);
    }

    @Test
    public void addTileDisjointThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = new Tile(0, 0);
        Tile tile2 = new Tile(1, 1);
        completeCrossroads(tile1);
        completeCrossroads(tile2);
        realEstate.addTile(tile1);
        realEstate.addTile(tile2);
    }

    @Test
    public void createRealEstateFistTileHasFollowerOrException() {
        exception.expect(RuntimeException.class);
        Tile tile = new Tile(0, 0);
        completeCrossroads(tile);
        new RealEstate(tile);
    }

    @Test
    public void addTileIfNoFollowerOnRealEstateFeature() {
        Tile tile1 = new Tile(0, 0);
        Tile tile2 = new Tile(1, 0);
        Feature feature = completeCrossroads(tile1);
        tile1.placeFollower(new Player(), feature);
        completeCrossroads(tile2);
        RealEstate realEstate = new RealEstate(tile1);
        realEstate.addTile(tile2);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile1);
        expectedSet.add(tile2);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnUnconnectedRealEstateFeature() {
        Tile tile1 = new Tile(0, 0);
        Tile tile2 = new Tile(1, 0);
        Feature feature1 = completeCrossroads(tile1);
        tile1.placeFollower(new Player(), feature1);
        Feature feature2 = completeCrossroads(tile2);
        RealEstate realEstate = new RealEstate(tile1);
        tile2.placeFollower(new Player(),feature2);

        realEstate.addTile(tile2);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile1);
        expectedSet.add(tile2);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }

    @Test
    public void addTileIfFollowerOnConnectedRealEstateFeatureThenRuntimeException() {
        exception.expect(RuntimeException.class);
        Tile tile1 = new Tile(0, 0);
        Tile tile2 = new Tile(1, 0);
        Feature feature1 = completeCrossroads(tile1, true);
        tile1.placeFollower(new Player(), feature1);
        Feature feature2 = completeCrossroads(tile2);
        RealEstate realEstate = new RealEstate(tile1);
        tile2.placeFollower(new Player(),feature2);

        realEstate.addTile(tile2);

        Set<Tile> expectedSet = new HashSet<>();
        expectedSet.add(tile1);
        expectedSet.add(tile2);
        assertEquals("Added tiles, first has follower, second does not", expectedSet, realEstate.getTileSet());
    }
}