package PathFinding;

import Graph.Node;
import _Initialize.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DepthFirstTest {
    @BeforeClass
    public static void setUp(){
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    /**
     * DepthFirst between any two random nodes
     * time is inaccurate due to filtering
     */
    @Test
    public void randomPathAny(){
        Context context = new Context(new DepthFirst(), _Initialize.getEdgeHashMap(),
                ((Node) RandomObject.retrieveRandom("Node")).getNodeID(), ((Node) RandomObject.retrieveRandom("Node")).getNodeID(),1);
        double[][] coords = context.runOne();
        assertNotEquals(0, coords.length);
        // extremely small chance the path length is one
    }

    /**
     * DepthFirst between any two random nodes on the same floor
     * time is inaccurate due to filtering
     */
    @Test
    public void randomPathSameFloor(){
        Node node1 = (Node) RandomObject.retrieveRandom("Node");
        Node node2;
        do{
            node2 = (Node) RandomObject.retrieveRandom("Node");
        } while(!node1.getFloor().equals(node2.getFloor()));
        Context context = new Context(new DepthFirst(), _Initialize.getEdgeHashMap(), node1.getNodeID(), node2.getNodeID(), 1);
        double[][] coords = context.runOne();
        assertNotEquals(0, coords.length);
        // small chance the path length is one
    }

    /**
     * DepthFirst between any two random nodes on a different floor
     * time is inaccurate due to filtering
     */
    @Test
    public void randomPathDifferentFloor(){
        Node node1 = (Node) RandomObject.retrieveRandom("Node");
        Node node2;
        do{
            node2 = (Node) RandomObject.retrieveRandom("Node");
        } while(node1.getFloor().equals(node2.getFloor()));
        Context context = new Context(new DepthFirst(), _Initialize.getEdgeHashMap(), node1.getNodeID(), node2.getNodeID(), 1);
        double[][] coords = context.runOne();
        assertNotEquals(0, coords.length); // should not be empty
        assertNotEquals(1, coords.length); // should not be one
    }

    /**
     * DepthFirst between the same node
     */
    @Test
    public void randomPathSameNode(){
        Node node1 = (Node) RandomObject.retrieveRandom("Node");
        Context context = new Context(new DepthFirst(), _Initialize.getEdgeHashMap(), node1.getNodeID(), node1.getNodeID(), 1);
        double[][] coords = context.runOne();
        assertEquals(1, coords.length); // should be one
    }
}