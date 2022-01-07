package Databases;

import Graph.Node;
//import java.sql.Connection;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;

/**
 * This class is used for creating and querying the Node table in the database
 */
public class NodeDB {

    private static final String nodesPath = "nodesv4.csv";

    private static HashMap<String, Node> nodeHashMapWONeighbors = new HashMap<>();

    //getter
    public static HashMap<String, Node> getNodeHashMap() {
        return nodeHashMapWONeighbors;
    }

    //setter
    public static void setNodeHashMap(HashMap<String, Node> nodeHashMap) {
        nodeHashMapWONeighbors = nodeHashMap;
    }


    /***
     * Connects to the database and creates a tbale called Node
     * Also it makes sure to drop the table if it already exists
     * before making it again
     */
    public static void createNodeTable() {


        // tries to create the table, throws an exception if the table already exists
        try {
            String nodeTable = "CREATE TABLE Node (nodeID Varchar(10) Primary Key, xCoord FLOAT, yCoord FLOAT, floor Varchar(2), building Varchar(20), nodeType Varchar(4), longName Varchar(100), shortName Varchar(50))";
            PreparedStatement pstmt1 = getConnection().prepareStatement(nodeTable);
            pstmt1.executeUpdate();
        } catch (SQLException e) {
            try {
                String dropTable = "DROP TABLE Node";
                PreparedStatement pstmt = getConnection().prepareStatement(dropTable);
                pstmt.executeUpdate();

                String nodeTable = "CREATE TABLE Node (nodeID Varchar(10) Primary Key, xCoord FLOAT, yCoord FLOAT, floor Varchar(2), building Varchar(20), nodeType Varchar(4), longName Varchar(100), shortName Varchar(50))";
                PreparedStatement pstmt1 = getConnection().prepareStatement(nodeTable);
                pstmt1.executeUpdate();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * Inserts node n into database
     * @param n the node to add
     * @throws java.sql.SQLException problems sending node data to the database
     */
    public static void addNodeDB(Node n) throws java.sql.SQLException {

        try {
            // creates a prepared statement to made the node
            PreparedStatement nodeStatement = getConnection().prepareStatement("Insert into Node values (?, ?, ?, ?, ?, ?, ?, ?)");

            nodeStatement.setString(1, n.getNodeID());
            nodeStatement.setString(2, String.valueOf(n.getXCoord()));
            nodeStatement.setString(3, String.valueOf(n.getYCoord()));
            nodeStatement.setString(4, String.valueOf(n.getFloor()));
            nodeStatement.setString(5, n.getBuilding());
            nodeStatement.setString(6, n.getNodeType());
            nodeStatement.setString(7, n.getLongName());
            nodeStatement.setString(8, n.getShortName());
            // inserts the node into the database
            int i = nodeStatement.executeUpdate();
           // System.out.println("UPDATED DB:" + i);
            //System.out.println("node added");

        } catch (SQLException e) {
            e.printStackTrace();
            //throw new java.sql.SQLException("NodeID nodeID:%s already exists", n.getNodeID());
        }


    }

    /**
     * delete a node object from the database
     *
     * @param n the node to delete
     * @throws SQLException problems sending node data to the database
     */
    public static void deleteNodeDB(Node n) throws SQLException {
        try {
            // deletes the nodeID n from the database
//            System.out.println("gets to delete try");
            Statement nodeDelete = getConnection().createStatement();
            String deleteStatement = String.format("DELETE FROM Node WHERE nodeID='%s'", n.getNodeID());
            nodeDelete.executeUpdate(deleteStatement); // KIOSK ERRORS HERE
//            System.out.println("executes delete");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * public static void editNodeDB(Node oldNode, Node newNode) - use to edit a node object in the table and database
     *
     * @param oldNode the previous node to be deleted
     * @param newNode the new node to replace the old node
     * @throws SQLException problems sending node data to the database
     */
    public static void editNodeDB(Node oldNode, Node newNode) throws SQLException {
        try {
            deleteNodeDB(oldNode); // This line causes issues with the kiosk
            addNodeDB(newNode);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Node does not exist");
        }
    }

    /***
     * Creates a LinkedList of nodes from the database
     * @return LinkedList<Node> that contains node objects of all nodes
     * represented in the database
     */
    public static LinkedList<Node> getDBNodes() {
        LinkedList<Node> tempList = new LinkedList<Node>();
        Node tempNode;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM NODE";
            ResultSet rset = stmt.executeQuery(query);

            String tempNodeID;
            double tempXCoord;
            double tempYCoord;
            String tempFloor;
            String tempBuilding;
            String tempNodeType;
            String tempLongName;
            String tempShortName;

            while (rset.next()) {
                tempNodeID = rset.getString("nodeID");
                tempXCoord = rset.getDouble("xCoord");
                tempYCoord = rset.getDouble("yCoord");
                tempFloor = rset.getString("floor");
                tempBuilding = rset.getString("building");
                tempNodeType = rset.getString("nodeType");
                tempLongName = rset.getString("longName");
                tempShortName = rset.getString("shortName");

                tempNode = new Node(tempNodeID, tempXCoord, tempYCoord, tempFloor, tempBuilding, tempNodeType, tempLongName, tempShortName);
                tempList.add(tempNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempList;
    }

    /**
     * Returns node object of specified id
     * @param id represents the nodeID represented as a string
     * @return Node object that has same nodeID as id
     */
    public static Node getNodeDB(String id) {
        Node tempNode = null;

        String tempNodeID;
        double tempXCoord;
        double tempYCoord;
        String tempFloor;
        String tempBuilding;
        String tempNodeType;
        String tempLongName;
        String tempShortName;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM NODE WHERE NODEID = " + "'" + id + "'";
            System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);
            rset.next();

            tempNodeID = rset.getString("nodeID");
            tempXCoord = rset.getDouble("xCoord");
            tempYCoord = rset.getDouble("yCoord");
            tempFloor = rset.getString("floor");
            tempBuilding = rset.getString("building");
            tempNodeType = rset.getString("nodeType");
            tempLongName = rset.getString("longName");
            tempShortName = rset.getString("shortName");

            tempNode = new Node(tempNodeID, tempXCoord, tempYCoord, tempFloor, tempBuilding, tempNodeType, tempLongName, tempShortName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempNode;
    }

    /**
     * Gets a LinkedList of Nodes from database and uses it
     * to create a hashmap of all of the nodes
     */
    public static void createHashMapDB() {
        nodeHashMapWONeighbors.clear();
        LinkedList<Node> allNodes = getDBNodes();
//        System.out.println("Node size: " + allNodes.size());
//        System.out.println("create hash map is of size " + allNodes.size());

        for (Node n: allNodes) {
            nodeHashMapWONeighbors.put(n.getNodeID(), n);
        }
//        System.out.println("node db hasmap size is " + nodeHashMapWONeighbors.size());

    }
}
