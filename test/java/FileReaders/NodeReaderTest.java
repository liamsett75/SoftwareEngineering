package FileReaders;

import Graph.Node;
import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertNotEquals;

public class NodeReaderTest {
    @BeforeClass
    public static void setUp(){
        _Initialize.setClearNodeFlag(true);
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    /**
     * reads a node into the database
     * function must retrieve the LinkedList<Node> from the database
     */
    @Test
    public void nodeRead() {
        // node hashmap must be made first
        _Initialize.getNodeReader().readFile("nodesv4.csv");
        HashMap<String, Node> nodeHashMap = _Initialize.getNodeReader().getNodeHashMap();
        assertNotEquals(nodeHashMap.size(),0);
    }
}