package PathFinding;

import Graph.Node;
import _Initialize.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomPathTest {

    @BeforeClass
    public static void setUp(){
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    @Test
    public void getPath() {
        Context context = new Context(new RandomPath(), _Initialize.getEdgeHashMap(),
                ((Node) RandomObject.retrieveRandom("Node")).getNodeID(), ((Node) RandomObject.retrieveRandom("Node")).getNodeID(),1);
    }
}