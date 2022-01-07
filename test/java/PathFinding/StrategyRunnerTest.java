package PathFinding;

import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StrategyRunnerTest {
    @BeforeClass
    public static void setUp(){
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }


    private static StrategyRunner strategyRunner = new StrategyRunner();

    // USES TOO MUCH MEMORY TO RUN AND CAN OVERFLOW THE JAVA HEAP
    // DO NOT TEST
//    /**
//     * tests the threading strategy runner
//     */
//    @Test
//    public void testStrategyRunner(){
//        // implements sudo test
//        strategyRunner.run(_Initialize.getEdgeHashMap(),((Node) RandomObject.retrieveRandom("Node")).getNodeID(), ((Node) RandomObject.retrieveRandom("Node")).getNodeID(),1);
//        assertNotNull(strategyRunner.getCoords()); // path could be anything
//    }
}