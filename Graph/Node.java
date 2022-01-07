package Graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * This class contains all the necessary code to deal with Nodes. A node has all all the properties found in the csv file, and 6 additional properties
 * The 6 additional properties include the 4 costs needed for A*, the neighbors of the node (effectively how we operate the graph class / edges), and the parent node, used to return the final path for A*
 */

public class Node {
    String nodeID;
    double xCoord;
    double yCoord;
    String floor;

    String building;
    String nodeType; //types can be CONF, HALL, DEPT, INFO, LABS, REST, SERV, STAI
    String longName;
    String shortName;

    Node parent;
    double gVal;
    double hVal;
    double zVal;
    double fVal;

    HashMap<String, Node> neighbors; // where String is the edge ID and Node is the endNode


    public Node(String nodeID, double xCoord, double yCoord, String floor, String building, String nodeType, String longName, String shortName, Node parent, double gval, double hval, double zVal, double fval) {
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.parent = null;
        this.neighbors = new HashMap<String, Node>();
        this.gVal = gVal;
        this.hVal = hVal;
        this.zVal = zVal;
        this.fVal = fVal;
    }

    public Node(String nodeID, double xCoord, double yCoord, String floor, String building, String nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.parent = null;
        this.neighbors = new HashMap<String, Node>();
        this.gVal = 0;
        this.hVal = 0;
        this.zVal = 0;
        this.fVal = 0;
    }

    public Boolean equals (Node b){

        //make sure all attributes between nodes are the same
        return (this.nodeID.equals(b.nodeID)     && this.xCoord == b.xCoord          &&
                this.yCoord == b.yCoord          && this.floor == b.floor         &&
                this.building.equals(b.building) && this.nodeType.equals(b.nodeType) &&
                this.longName.equals(b.longName) && this.shortName.equals(b.shortName) &&
                b.neighbors.equals(this.neighbors));
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
  
    public double getXCoord() {
        return this.xCoord;
    }

    public double getYCoord() {
        return this.yCoord;
    }

    public void setCoord(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public HashMap<String,Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(HashMap<String,Node> neighbors) {
        this.neighbors = neighbors;
    }


    //getters
    public String getFloor() {
        return floor;
    }
    public String getBuilding() {
        return building;
    }
    public String getNodeType() {
        return nodeType;
    }
    public String getLongName() {
        return longName.trim();
    }
    public String getShortName() {
        return shortName.trim();
    }
    public Node getParent() { return this.parent; }

    public double getGVal() {
        return gVal;
    }
    public double getHVal() {
        return hVal;
    }
    public double getZVal() {
        return zVal;
    }
    public double getFVal() {
        return fVal;
    }


    //setters
    public void setFloor(String floor) {
        this.floor = floor;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
    public void setLongName(String longName) {
        this.longName = longName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public void setParent(Node pNode) {
        this.parent = pNode;
    }
    public void setGVal(double gVal) {
        this.gVal = gVal;
    }
    public void setHVal(double hVal) {
        this.hVal = hVal;
    }
    public void setZVal(double zVal) {
        this.zVal = zVal;
    }
    public void setFVal(double fVal) {
        this.fVal = fVal;
    }

    //other methods
    /**
     * Retrieves a specific node from its ID
     * @param id
     * @return a Node if it exists, throws exception if Node cannot be found
     */
    public static Node getNodeFromID (HashMap<String, Node> nodes, String id) {
        try{
            nodes.get(id);
        }
        catch (Exception e){
            System.out.println("Node with the following ID does not exist in Database");
        }
        return null;
    }

    /**
     * absorb() - absorbs the result set fields into the object itself
     * @param resultSet you must call resultSet.next() before passing it to this function or it will cause deadlock
     * @throws SQLException for any result set errors
     */
    public void absorb(ResultSet resultSet) throws SQLException{
        try {
            this.nodeID = resultSet.getString("nodeID");
            this.xCoord = resultSet.getInt("xCoord");
            this.yCoord = resultSet.getInt("yCoord");
            this.floor = resultSet.getString("floor");
            this.building = resultSet.getString("building");
            this.nodeType = resultSet.getString("nodeType");
            this.longName = resultSet.getString("longName");
            this.shortName = resultSet.getString("shortName");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Gets a Node from it's long name by iterating through the list of nodes
     * @param //Hash map of all the nodes that need to be compared to
     * @param longName Longname of the node that is desired to be retrieved
     * @return Retunrs the desired node
     */
    static public Node getNodeFromLongName(HashMap<String, Node> allNodes, String longName){
        for (Node n : allNodes.values()){
            if (n.getLongName().equalsIgnoreCase(longName)){
                return n;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(node.xCoord, xCoord) == 0 &&
                Double.compare(node.yCoord, yCoord) == 0 &&
                nodeID.equals(node.nodeID) &&
                floor.equals(node.floor) &&
                building.equals(node.building) &&
                nodeType.equals(node.nodeType) &&
                longName.equals(node.longName) &&
                shortName.equals(node.shortName);
    }
}