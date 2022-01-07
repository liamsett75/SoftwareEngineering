package Databases;

import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class NodeDBTest {
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
     * CreateNodeTable() - table should be created to test the class methods
     */
    @Before
    public void CreateNodeTable() {
        NodeDB.createNodeTable();
    }

//    /**
//     *
//     */
//    private void addNodesLoop
//    private void addEmployeesLoop(int toAdd, LinkedList<Employee> testList){
//        LinkedList<String> empIDs = new LinkedList<>(); // has to check employee ID's are not the same but still fit in the DB
//        int employeesAdded = 0;
//        do{
//            Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
//
//            // if ID matches a previous one try again
//            if(empIDs.contains(employee.getEmpID())){
//                continue;
//            }
//            empIDs.add(employee.getEmpID()); // keeps track of the empIDs added
//
//            // adds to the db
//            try{
//                EmployeeDB.addEmployeeDB(employee);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            // adds to the manual list
//            testList.add(employee);
//
//            employeesAdded++;
//        } while (employeesAdded < toAdd); // 4 is arbitrary
//    }

//    /**
//     * GetDBNodes() - gets all nodes from the database
//     * order is not assumed
//     */
//    @Test
//    public void aGetDBNodes(){
//        // not part of the test
//        try{
//            NodeDB.addNodeDB(sampleNode1);
//            NodeDB.addNodeDB(sampleNode2);
//            NodeDB.addNodeDB(sampleNode3);
//        } catch(SQLException e) {
//            fail(); // should not fail
//        }
//
//        // manual node of linked lists
//        LinkedList<Node> testList = new LinkedList<>();
//        testList.add(sampleNode1);
//        testList.add(sampleNode2);
//        testList.add(sampleNode3);
//
//        // gets all the nodes from the database and returns LinkedList<Node>
//        LinkedList<Node> tempList = NodeDB.getDBNodes();
//        // tests both directions
//
//        assertTrue(testList.containsAll(tempList));
//        assertTrue(tempList.containsAll(testList));
//    }
//
//    /**
//     * AddNodeDB() - tests adding a node
//     * tests adding a node to the database
//     * checks that node is there in the database and matches the node sent
//     */
//    @Test
//    public void bAddNodeDB() {
//        try {
//            // main function tested
//            NodeDB.addNodeDB(sampleNode1);
//        } catch (SQLException e) {
//            fail();
//        }
//
//        // manuel list
//        LinkedList<Node> testList = new LinkedList<>();
//        testList.add(sampleNode1);
//
//        // not the method tested
//        LinkedList<Node> tempList = NodeDB.getDBNodes();
//        // tests both directions
//
//        assertTrue(testList.containsAll(tempList));
//        assertTrue(tempList.containsAll(testList));
//    }
//
//    /**
//     * DeleteNodeDB() - tests deleting a node
//     * tries to delete a node that was added earlier
//     * makes sure the node is not in the database
//     */
//    @Test
//    public void cDeleteNodeDB() {
//        // not part of the test
//        try {
//            NodeDB.addNodeDB(sampleNode2);
//        } catch (SQLException e) {
//            fail();
//        }
//
//        // delete a node object
//        try {
//            NodeDB.deleteNodeDB(sampleNode2);
//        } catch (SQLException e) {
//            fail();
//        }
//        // not the method tested
//        LinkedList<Node> tempList = NodeDB.getDBNodes();
//        assertEquals(tempList.size(),0);
//    }
//
//    /**
//     * EditNodeDB() - tests editing a node that was added easier
//     * it is assumed, from the UI level, that the nodeID for both nodes are the same
//     */
//    @Test
//    public void dEditNodeDB() {
//        // not part of the test
//        try {
//            NodeDB.addNodeDB(sampleNode1);
//            NodeDB.addNodeDB(sampleNode2);
//
//        } catch (SQLException e) {
//            fail();
//        }
//
//        // manual list
//        LinkedList<Node> testList = new LinkedList<>();
//        testList.add(sampleNode2);
//        testList.add(sampleNodeEdit);
//
//        try {
//            // calls the tested function
//            NodeDB.editNodeDB(sampleNode1, sampleNodeEdit);
//            // it is assumed, from the UI level, that the node IDs are the same
//            assertEquals(sampleNode1.getNodeID(), sampleNodeEdit.getNodeID());
//
//            // tested in parallel, not the tested method
//            LinkedList<Node> tempList = NodeDB.getDBNodes();
//            // order cannot be assumed
//
//            assertTrue(testList.containsAll(tempList));
//            assertTrue(tempList.containsAll(testList));
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    /**
//     * createNodeHashMap() - makes a hashmap of the nodes inside the database
//     * returns a hashmap with no neighbors
//     */
//    @Test
//    public void eCreateHashMapDB(){
//        // setup, not part of the test
//        try{
//            NodeDB.addNodeDB(sampleNode1);
//            NodeDB.addNodeDB(sampleNode2);
//            NodeDB.addNodeDB(sampleNode3);
//        } catch(SQLException e) {
//            fail(); // should not fail
//        }
//        // manual hashmap
//        HashMap<String,Node> testMap = new HashMap<>();
//        testMap.put(sampleNode1.getNodeID(),sampleNode1);
//        testMap.put(sampleNode2.getNodeID(),sampleNode2);
//        testMap.put(sampleNode3.getNodeID(),sampleNode3);
//
//        // calls method to be tested
//        NodeDB.createHashMapDB();
//
//        // calls the getter for that method
//        HashMap<String,Node> tempMap = NodeDB.getNodeHashMap();
//        // might be a better method for testing two hashmaps are equal, can't just override equals
//        //noinspection SuspiciousMethodCalls
//        assertEquals(tempMap.get(sampleNode1),testMap.get(sampleNode1));
//        //noinspection SuspiciousMethodCalls
//        assertEquals(tempMap.get(sampleNode2),testMap.get(sampleNode2));
//        //noinspection SuspiciousMethodCalls
//        assertEquals(tempMap.get(sampleNode3),testMap.get(sampleNode3));
//    }
}
