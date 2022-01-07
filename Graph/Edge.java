package Graph;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains all the necessary code to deal with Edges specifically for the database
 * An edge is made up of its ID, which is in the form endnodeID_startnodeID, and the respective individual IDs
 */

public class Edge
{
    //CREATE TABLE Edge (edgeID Varchar(30), startNode Varchar(15), endNode Varchar(15)

    private String edgeID;
    private String startNodeID;
    private String endNodeID;


    public Edge(String edgeID, String startNode, String endNode) {
        this.edgeID = edgeID;
        this.startNodeID = startNode;
        this.endNodeID = endNode;
    }
    public Edge(){
        this.edgeID = "";
        this.startNodeID = "";
        this.endNodeID = "";
    };

    public String getEdgeID() {
        return edgeID;
    }

    public void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    public String getStartNodeID() {
        return startNodeID;
    }

    public void setStartNodeID(String startNode) {
        this.startNodeID = startNode;
    }

    public String getEndNodeID() {
        return endNodeID;
    }

    public void setEndNodeID(String endNode) {
        this.endNodeID = endNode;
    }

    public void addEdgeToNodes()
    {

    }

    /**
     * equals() - compares the current Edge to another Edge object
     * @param edge the other edge object
     * @return returns true if the edge objects are the same
     */
    public Boolean equals (Edge edge){
        return (this.getEdgeID().equals(edge.getEdgeID()) &&
                this.getStartNodeID().equals(edge.getEdgeID()) &&
                this.getEndNodeID().equals(edge.getEndNodeID()));
    }

    /**
     * absorb() - absorbs the result set fields into the object itself
     * @param resultSet you must call resultSet.next() before passing it to this function or it will cause deadlock
     * @throws SQLException for any result set errors
     */
    public void absorb (ResultSet resultSet) throws SQLException{
        try {
            this.edgeID = resultSet.getString("edgeID");
            this.startNodeID = resultSet.getString("startNode");
            this.endNodeID = resultSet.getString("endNode");
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return edgeID.equals(edge.edgeID) &&
                startNodeID.equals(edge.startNodeID) &&
                endNodeID.equals(edge.endNodeID);
    }
}
