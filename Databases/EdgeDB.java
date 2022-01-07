package Databases;

import Graph.Edge;
import Graph.Node;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;

public class EdgeDB {

    private static HashMap<String, Node> nodeHashMapWNeighbors = new HashMap<>();

    //getter
    public static HashMap<String, Node> getNodeHashMap() {
        return nodeHashMapWNeighbors;
    }

    //setter
    public static void setNodeHashMap(HashMap<String, Node> nodeHashMap) {
//        System.out.println("parameter hasmmap is of size" + nodeHashMap.size());
        nodeHashMapWNeighbors = nodeHashMap;
//        System.out.println("hashmap with neighbors is of size " + nodeHashMapWNeighbors.size());
    }


    /***
     * Connects to the database and creates a table called Edge
     * Also it makes sure to drop the table if it already exists
     * before making it again
     */
    public static void createEdgeTable() {
        try {
            // tries to create the table, throws an exception if the table already exists
            String edgeTable = "CREATE TABLE Edge (edgeID Varchar(30) PRIMARY KEY , startNode Varchar(15), endNode Varchar(15))";
            PreparedStatement pstmt1 = getConnection().prepareStatement(edgeTable);
            pstmt1.executeUpdate();

        } catch (SQLException e) {
            try {
                String dropTable = "DROP TABLE Edge";
                PreparedStatement pstmt = getConnection().prepareStatement(dropTable);
                pstmt.executeUpdate();

                String edgeTable = "CREATE TABLE Edge (edgeID Varchar(30) PRIMARY KEY , startNode Varchar(15), endNode Varchar(15))";
                PreparedStatement pstmt1 = getConnection().prepareStatement(edgeTable);
                pstmt1.executeUpdate();


            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * Inserts Edge ed into database
     * @param ed the edge to add
     * @throws java.sql.SQLException problems sending edge data to the database
     */
    public static void addEdgeDB(Edge ed) throws SQLException {
        Connection conn = getConnection();
        try {
            // creates a prepared statement to made the node
            PreparedStatement edgeStatement = getConnection().prepareStatement("Insert into Edge values (?, ?, ?)");

            edgeStatement.setString(1, ed.getEdgeID());
            edgeStatement.setString(2, ed.getStartNodeID());
            edgeStatement.setString(3, ed.getEndNodeID());
            // inserts the node into the database
            edgeStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Deletes an edge from the database
     * @param ed the edge to delete
     * @throws SQLException problems sending edge data to the database
     */
    public static void deleteEdgeDB(Edge ed) throws SQLException {
        Connection conn = getConnection();
        try {
            // deletes the nodeID n from the database
            Statement edgeDelete = getConnection().createStatement();
            String deleteStatement = String.format("DELETE FROM Edge WHERE edgeID='%s'", ed.getEdgeID());
            // inserts the node into the database
            edgeDelete.executeUpdate(deleteStatement);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Edits edge object in the database
     * @param oldEdge the previous edge to be deleted
     * @param newEdge the new edge to replace the old edge
     * @throws SQLException problems sending edge data to the database
     */
    public static void editEdgeDB(Edge oldEdge, Edge newEdge) throws SQLException {
        Connection conn = getConnection();
        try {
            deleteEdgeDB(oldEdge);
            addEdgeDB(newEdge);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Node does not exist");
        }
    }

    /**
     * Creates a linked list of all edges from the database
     * @return LinkedList<Edge> that contains edge objects of all edges
     * represented in the database
     */
    public static LinkedList<Edge> getDBEdges() {
        LinkedList<Edge> tempList = new LinkedList<Edge>();
        Edge tempEdge;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM Edge";
            ResultSet rset = stmt.executeQuery(query);

            String tempEdgeID;
            String tempStartNode;
            String tempEndNode;

            while (rset.next()) {
                tempEdgeID = rset.getString("edgeID");
                tempStartNode = rset.getString("startNode");
                tempEndNode = rset.getString("endNode");

                tempEdge = new Edge(tempEdgeID, tempStartNode, tempEndNode);
                tempList.add(tempEdge);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempList;
    }

    /**
     * populates the hashmap of Nodes with the neighbors
     * in order for the hashmap to be ready to be used by AStar
     */
    public static void populateHashMapDB() {

        NodeDB.createHashMapDB();

//        System.out.println("in edge db get node hash map is of size " + NodeDB.getNodeHashMap().size());

        setNodeHashMap(NodeDB.getNodeHashMap());

//        System.out.println("populate hash map is of size " + getNodeHashMap().size());

        LinkedList<Edge> allEdges = getDBEdges();
//        System.out.println("Edge DB size is: " + allEdges.size());

        Node startNode = null;
        Node endNode = null;

        for (Edge e : allEdges) {
            if (!nodeHashMapWNeighbors.isEmpty()) {
                if(nodeHashMapWNeighbors.containsKey(e.getStartNodeID()) && nodeHashMapWNeighbors.containsKey(e.getEndNodeID())) {
                    startNode = nodeHashMapWNeighbors.get(e.getStartNodeID());
                    endNode = nodeHashMapWNeighbors.get(e.getEndNodeID());
                    startNode.getNeighbors().putIfAbsent(e.getEdgeID(), endNode);
                    endNode.getNeighbors().putIfAbsent(e.getEdgeID(), startNode);
                    nodeHashMapWNeighbors.replace(startNode.getNodeID(), startNode);
                    nodeHashMapWNeighbors.replace(endNode.getNodeID(), endNode);
                }
            }
        }
    }

    public static void removeHashMapDBNeighbors() {

        for (Node n: nodeHashMapWNeighbors.values()) {
            n.setNeighbors(null);
        }
    }

    public static void deleteEdgeWithNodeID(String nodeID) {
        //if edge start node or edge end node is the same as node ID then delete
        LinkedList<Edge> edgeLinkedList = getDBEdges();

        for(Edge ed: edgeLinkedList) {
            if(ed.getStartNodeID().equals(nodeID) || ed.getEndNodeID().equals(nodeID)) {
                try {
                    deleteEdgeDB(ed);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   public static void deleteEdgeHashmap(Edge delEdge) {
        String startNodeID = delEdge.getStartNodeID();
        String endNodeID = delEdge.getStartNodeID();

        Node startNode = NodeDB.getNodeDB(startNodeID);
        Node endNode = NodeDB.getNodeDB(endNodeID);

        HashMap<String,Node> tempStartHM = startNode.getNeighbors();
       HashMap<String,Node> tempEndHM = endNode.getNeighbors();

       tempStartHM.remove(endNodeID);
       tempEndHM.remove(startNodeID);

       startNode.setNeighbors(tempStartHM);
       endNode.setNeighbors(tempEndHM);
   }
}

