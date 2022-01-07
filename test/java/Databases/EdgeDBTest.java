package Databases;

import Graph.Edge;
import _Initialize.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class EdgeDBTest {
    @BeforeClass
    public static void setUp(){
        _Initialize.setClearEdgeFlag(true);
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    /**
     *
     */
    public void addEdgesLoop(int toAdd, LinkedList<Edge> testList){
        LinkedList<String> edgeIDs = new LinkedList<>();
        int edgesAdded = 0;
        do{
            Edge edge = (Edge) RandomObject.retrieveRandom("Edge");

            // if ID matches a previous one try again
            if(edgeIDs.contains(edge.getEdgeID())){
                continue;
            }
            edgeIDs.add(edge.getEdgeID()); // keeps track of the edges added

            // adds to the db
            try{
                EdgeDB.addEdgeDB(edge);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // adds to the manual list
            testList.add(edge);

            edgesAdded++;
        } while (edgesAdded < toAdd);
    }

    /**
     * table should be created to test the class methods
     */
    @Before
    public void createEdgeTable() {
        EdgeDB.createEdgeTable();
    }

    /**
     * gets all the edges from the database
     * order is not assumed, each list is compared using containsAll
     * first test since most of the other tests depend on getEdges
     */
    @Test
    public void getEdges() {
        // given
        LinkedList<Edge> testList = new LinkedList<>();
        addEdgesLoop(4, testList);
        // when
        LinkedList<Edge> tempList = EdgeDB.getDBEdges();
        // then
        assertTrue(testList.containsAll(tempList));
    }

    /**
     * adding an edge
     * tests adding an edge to the database
     */
    @Test
    public void addEdgeDB() {
        // given
        LinkedList<Edge> testList = new LinkedList<>();
        addEdgesLoop(1, testList);
        // when
        LinkedList<Edge> tempList = EdgeDB.getDBEdges();
        // then
        assertTrue(testList.containsAll(tempList));
    }

    /**
     * deleting an edge
     * tries to delete an edge that was added
     * makes sure the edge is not in the database
     */
    @Test
    public void deleteEdgeDB() {
        // given
        LinkedList<Edge> testList = new LinkedList<>();
        addEdgesLoop(1, testList);
        // when
        // delete an edge object
        try {
            EdgeDB.deleteEdgeDB(testList.get(0));
        } catch (SQLException e) {
            fail();
        }
        // then
        LinkedList<Edge> tempList = EdgeDB.getDBEdges();
        assertEquals(tempList.size(),0);
    }

    /**
     * tests editing an edge that was added easier
     * it is assumed, from the UI level, that the edgeID for both nodes are the same
     */
    @Test
    public void editEdgeDB() {
        // given
        LinkedList<Edge> testList = new LinkedList<>();
        addEdgesLoop(2, testList);
        Edge editEdge = testList.get(0);
        editEdge.setStartNodeID("ID"); // edits the edge to add to the db
        testList.get(0).setStartNodeID("ID"); // edits the edge in the list
        try{
            // when
            EdgeDB.editEdgeDB(testList.get(0), editEdge);
            LinkedList<Edge> tempList = EdgeDB.getDBEdges();
            // then
            assertTrue(tempList.containsAll(testList));
        } catch (SQLException e) {
            fail();
        }
    }


    /**
     *  populates the node hashmap
     *  uses the edges to create the hashmap
     */
    @Test
    public void populateHashMapDB(){
//        LinkedList<Edge> testList = new LinkedList<>();
//        addEdgesLoop(2, testList);
//
//        try{
//            EdgeDB.populateHashMapDB(testList.get(0));
//        }
    }

    @Test
    public void removeHashMapDBNeighbors() {
    }

    @Test
    public void deleteEdgeWithNodeID() {
    }
}