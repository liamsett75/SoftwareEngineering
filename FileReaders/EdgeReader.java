package FileReaders;

import Databases.EdgeDB;
import Graph.Edge;
import Graph.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

/*This class is responsible for receiving the data from the
edge csv file */
public class EdgeReader implements FileReader {

    HashMap<String, Node> nodeHashMap = new HashMap<>();

    //getter
    public HashMap<String, Node> getNodeHashMap() { return this.nodeHashMap; }
    //setter
    public void setNodeHashMap(HashMap<String, Node> nodeHashMap) { this.nodeHashMap = nodeHashMap; }

    /**
     * Reads in a csv file of edges, and then send that info to the data base and
     * populates the node objects with the info through their neighbor attribute.
     * This info is stored into EdgeReader's hashmap of Nodes and their IDs
     * @param fileName
     */
    public void readFile(String fileName) {
        BufferedReader br;
        String line;
        String edgeID;
        String startNodeID;
        String endNodeID;
        Node startNode, endNode;
        LinkedList<String> createdNodes = new LinkedList<>();

        EdgeDB.createEdgeTable();

        try {
            br = new BufferedReader(new java.io.FileReader(fileName));
            // Don't delete this line, is basically gets rid of the first line of the csv which is the label line
            line = br.readLine();

            while ((line = br.readLine()) != null) {
                try {
                    String[] lineArray = line.split(splitBy);
                    edgeID = lineArray[0];
                    startNodeID = lineArray[1];
                    endNodeID = lineArray[2];

                    Edge ed = new Edge(edgeID, startNodeID, endNodeID);

                    try
                    {
                        EdgeDB.addEdgeDB(ed);
                    }catch (SQLException e)
                    {
                        e.printStackTrace();
                    }

                    if(!nodeHashMap.isEmpty()) {
                        startNode = nodeHashMap.get(startNodeID);
                        endNode = nodeHashMap.get(endNodeID);
                        startNode.getNeighbors().putIfAbsent(edgeID, endNode);
                        endNode.getNeighbors().putIfAbsent(edgeID, startNode);
                        nodeHashMap.replace(startNode.getNodeID(), startNode);
                        nodeHashMap.replace(endNode.getNodeID(), endNode);
                    }
                }
                catch (java.lang.NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
