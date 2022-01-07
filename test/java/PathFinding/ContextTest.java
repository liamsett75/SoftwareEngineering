package PathFinding;

import Graph.Node;
import _Initialize.*;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContextTest {
    @BeforeClass
    public static void setUp(){
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    /**
     * AStar context
     */
    @Test
    public void runOneAStar() {
        Context context = new Context(new AStar(), _Initialize.getEdgeHashMap(),
                ((Node) RandomObject.retrieveRandom("Node")).getNodeID(), ((Node) RandomObject.retrieveRandom("Node")).getNodeID(),1);
        double[][] coords = context.runOne();
        assertNotEquals(0, coords.length);
    }

    /**
     * BreadthFirst context
     */
    @Test
    public void runOneBreadthFirst() {
        Context context = new Context(new BreadthFirst(), _Initialize.getEdgeHashMap(),
                ((Node) RandomObject.retrieveRandom("Node")).getNodeID(), ((Node) RandomObject.retrieveRandom("Node")).getNodeID(),1);
        double[][] coords = context.runOne();
        assertNotEquals(0, coords.length);
    }

    /**
     * DepthFirst() context
     */
    @Test
    public void runOneDepthFirst() {
        Context context = new Context(new DepthFirst(), _Initialize.getEdgeHashMap(),
                ((Node) RandomObject.retrieveRandom("Node")).getNodeID(), ((Node) RandomObject.retrieveRandom("Node")).getNodeID(),1);
        double[][] coords = context.runOne();
        assertNotEquals(0, coords.length);
    }

    /**
     * same coordinate time
     */
    @Test
    public void calcTotalTime1() {
        double [][] tempCoords = new double[1][3];
        tempCoords[0][0] = 1000.0;
        tempCoords[0][1] = 1000.0;
        tempCoords[0][2] = 2.0;
        assertEquals(Context.calcTotalTime(tempCoords),0.0,0.01);
    }

    /**
     * different coordinate time
     */
    @Test
    public void calcTotalTime2() {
        double [][] tempCoords = new double[2][3];
        tempCoords[0][0] = 1000.0;
        tempCoords[0][1] = 1000.0;
        tempCoords[0][2] = 2.0;
        tempCoords[1][0] = 2000.0;
        tempCoords[1][1] = 3000.0;
        tempCoords[1][2] = 44.0;
        assertEquals(Context.calcTotalTime(tempCoords),140.08,0.01);
    }
}