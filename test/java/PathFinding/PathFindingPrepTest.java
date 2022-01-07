package PathFinding;

import Graph.Node;
import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.*;

public class PathFindingPrepTest {
    private static Random random = new Random();

    @BeforeClass
    public static void setUp(){
        random.setSeed(System.currentTimeMillis());
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    /**
     * makes the starting node for path finding
     */
    @Test
    public void makeStartNode(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.makeStartNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor);
        // then, should find an end node
        assertNotNull(tempNode);
    }

    /**
     * makes the starting node for path finding
     * type key 1
     */
    @Test
    public void findEndNode(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.findEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor, 1);
        // then
        assertNotNull(tempNode);
        //assertEquals(tempNode.getNodeType(), "EXIT");
    }

    /**
     * makes the starting node for path finding
     * type key 2
     */
    @Test
    public void findEndNode2(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.findEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor, 2);
        // then
        assertNotNull(tempNode);
        //assertEquals(tempNode.getNodeType(), "REST");
    }

    /**
     * makes the starting node for path finding
     * type key 3
     */
    @Test
    public void findEndNode3(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.findEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor, 3);
        // then
        assertNotNull(tempNode);
        //assertTrue(tempNode.getNodeType().equals("STAI") || tempNode.getNodeType().equals("RETL"));
    }

    /**
     * makes the starting node for path finding
     * type key 4
     */
    @Test
    public void findEndNode4(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.findEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor, 4);
        // then
        assertNotNull(tempNode);
       // assertEquals(tempNode.getNodeType(), "INFO");
    }

    /**
     * makes the starting node for path finding
     * type key 5
     */
    @Test
    public void findEndNode5(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.findEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor, 5);
        // then
        assertNotNull(tempNode);
        //assertEquals(tempNode.getNodeType(), "RETL");
    }

    /**
     * makes the starting node for path finding
     * type key 6
     */
    @Test
    public void findEndNode6(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.findEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor, 6);
        // then
        assertNotNull(tempNode);
        //assertEquals(tempNode.getNodeType(), "EXIT");
    }

    /**
     * makes the starting node for path finding
     * type key 7
     */
    @Test
    public void findEndNode7(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.findEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor, 7);
        // then
        assertNotNull(tempNode);
        //assertTrue(tempNode.getNodeType().equals("SERV") || tempNode.getNodeType().equals("INFO"));
    }

    /**
     * makes the starting node for path finding
     * end node on click
     */
    @Test
    public void clickEndNode(){
        // given
        // generates random coordinates
        double xCoord = random.nextInt(4000) + 1000; // xCoord 1000 to 5000
        double yCoord = random.nextInt(3000); // yCoord 1 to 3000
        int floor = random.nextInt(6) - 2; // floor -2 to 3
        // when
        Node tempNode = PathFindingPrep.clickEndNode(_Initialize.getEdgeHashMap(), xCoord, yCoord, floor);
        // then
        assertNotNull(tempNode);
    }
}