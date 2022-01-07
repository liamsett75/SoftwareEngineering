package FileReaders;

import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

    import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EdgeReaderTest {
    @BeforeClass
    public static void setUp(){
        _Initialize.setClearNodeFlag(true);
        _Initialize.setClearEdgeFlag(true);
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    /**
     * tests readFile reads in a file and populates the hashmap
     */
    @Test
    public void readFile(){
        // node hashmap must be made first
        _Initialize.getNodeReader().readFile("nodesv4.csv");
        _Initialize.getEdgeReader().setNodeHashMap(_Initialize.getNodeReader().getNodeHashMap());
        // then reads in the edge file
        _Initialize.getEdgeReader().readFile("edgesv4.csv");
        assertNotEquals(_Initialize.getEdgeHashMap().size(), 0); // should not be empty
    }
}