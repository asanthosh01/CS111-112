package test;

import static org.junit.Assert.*;
import org.junit.*;

import island.*;
import island.constants.Color;

/**
 * This is a JUnit test class, used to test code
 * 
 * You should test the SmartCat class by designing Island test cases
 * and filling in the JUnit tests according to the assignment
 * description.
 * 
 * @author Colin Sullivan
 */
public class SmartCatTest {

    public static Island pathIsland = new Island(new String[][] {
        {"L", "W", "L", "L", "L", "W", "L"},
        {"L", "W", "L", "W", "L", "W", "L"},
        {"L", "W", "L", "W", "L", "W", "L"},
        {"L", "W", "L", "W", "L", "W", "L"},
        {"L", "W", "L", "W", "L", "W", "L"},
        {"L", "W", "L", "W", "L", "W", "L"},
        {"L", "L", "L", "W", "L", "L", "L"}
    });

    public static Island yarnIsland = new Island(new String[][] {
        {"L", "Y", "L", "L", "L", "L", "L", "L", "L"},
        {"L", "W", "Y", "W", "L", "L", "W", "W", "L"},
        {"L", "L", "W", "L", "Y", "L", "L", "L", "L"},
        {"L", "W", "W", "L", "L", "W", "L", "Y", "L"},
        {"L", "L", "L", "L", "Y", "L", "L", "L", "L"},
        {"L", "W", "L", "W", "L", "W", "L", "Y", "L"},
        {"L", "L", "L", "W", "L", "L", "W", "L", "Y"},
        {"L", "W", "Y", "W", "W", "L", "L", "W", "L"},
        {"W", "L", "L", "Y", "W", "L", "L", "L", "W"}
    });

    public static Island mazeIsland = new Island(new String[][] {
        {"L", "W", "L", "L", "W", "L", "L", "L", "W", "L"},
        {"L", "W", "W", "L", "W", "L", "W", "L", "L", "L"},
        {"L", "W", "W", "L", "W", "L", "W", "L", "L", "L"},
        {"L", "W", "W", "L", "W", "L", "W", "W", "L", "L"},
        {"L", "W", "L", "W", "W", "L", "W", "L", "L", "L"},
        {"L", "W", "W", "L", "W", "L", "W", "L", "W", "W"},
        {"L", "W", "W", "L", "W", "L", "W", "L", "L", "W"},
        {"L", "W", "L", "W", "L", "L", "W", "W", "L", "L"},
        {"L", "W", "W", "W", "W", "L", "L", "W", "L", "W"},
        {"L", "L", "L", "L", "L", "L", "L", "W", "L", "L"}
    });

    @Test
    public void testWalkPath() {

        SmartCat cat = new SmartCat("TestCat", pathIsland, 0, 0, Color.GREY); // create new SmartCat on pathIsland at (0,0)
        cat.walkPath(); // call the walkPath() method
        
        int cols = pathIsland.getTiles()[0].length; // get # of columns
        
        assertEquals("Cat should be in row 0", 0, cat.getRow()); // assert that the cat ended in the top row
        
        assertEquals("Cat should be in the rightmost column", cols - 1, cat.getCol()); // assert that the cat ended in the rightmost column.
    }

    @Test
    public void testCollectAllYarn() {
        
        SmartCat cat = new SmartCat("TestCat", yarnIsland, 0, 0, Color.ORANGE); //create new SmartCat on pathIsland at (0,0)
        cat.collectAllYarn(); // call the collectAllYarn() method

        Tile[][] tiles = yarnIsland.getTiles(); // loop over all tiles and assert that no tile has yarn
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                assertFalse("Tile at (" + i + "," + j + ") should not have yarn", tiles[i][j].hasYarn);
            }
        }
    }

    @Test
    public void testSolveMaze() {

        SmartCat cat = new SmartCat("TestCat", mazeIsland, 0, 0, Color.BROWN); // create a new SmartCat on mazeIsland @ (0,0)
        cat.solveMaze(); // call the solveMaze() method

        int cols = mazeIsland.getTiles()[0].length; // get number of columns.

        assertEquals("Cat should be in row 0", 0, cat.getRow());

        assertEquals("Cat should be in the rightmost column", cols - 1, cat.getCol());

        assertTrue("Cat should have taken at least 30 steps", cat.numStepsTaken() >= 30);
    }

}
