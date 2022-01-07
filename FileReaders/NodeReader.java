package FileReaders;

import Databases.NodeDB;
import Databases.RoomDB;
import Graph.Node;
import RoomBooking.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

/*This class is responsible for receiving the data from the
nodes csv file */
public class NodeReader implements FileReader {
    HashMap<String, Node> nodeHashMap = new HashMap<>();
    private static LinkedList<Node> nodeList = new LinkedList<>();
    //also makes rooms when nodes are being made and added
    private static LinkedList<Room> roomList = new LinkedList<Room>();

    //getters
    public HashMap<String, Node> getNodeHashMap() {
        return nodeHashMap;
    }
    public static LinkedList<Node> getNodeList() {
        return nodeList;
    }
    public static LinkedList<Room> getRoomList() { return roomList; }
    //setters
    public void setNodeHashMap(HashMap<String, Node> nodeHashMap) {
        this.nodeHashMap = nodeHashMap;
    }
    public static void setNodeList(LinkedList<Node> nodeList) {
        NodeReader.nodeList = nodeList;
    }
    public static  void setRoomList(LinkedList<Room> roomList) { NodeReader.roomList = roomList; }

    /**
     * Takes in a csv file and reads the nodes from it. Stores a hashmap of the nodes
     * inside the NodeReader's nodeHashMap property, with key NodeID and value Node
     * @param fileName name of node csv to read - currently nodesv3.csv
     */
    public void readFile(String fileName) {
        BufferedReader br;
        String line;
        String tempNodeID;
        double tempXCoord;
        double tempYCoord;
        String tempFloor;
        String tempBuilding;
        String tempNodeType;
        String tempLongName;
        String tempShortName;
        Node tempParent;
        double tempGVal;
        double tempHVal;
        double tempZVal;
        double tempFVal;

        NodeDB.createNodeTable();

        try {
            br = new BufferedReader(new java.io.FileReader(fileName));

            line = br.readLine(); //allows first line (header to be skipped)
            while ((line = br.readLine()) != null) {
                try {
                    String[] lineArray = line.split(splitBy);
                    tempNodeID = lineArray[0];
                    tempXCoord = Double.parseDouble(lineArray[1]);
                    tempYCoord = Double.parseDouble(lineArray[2]);
                    tempFloor = lineArray[3];
                    tempBuilding = lineArray[4];
                    tempNodeType = lineArray[5];
                    tempLongName = lineArray[6];
                    tempShortName = lineArray[7];

                    Node n = new Node(tempNodeID, tempXCoord, tempYCoord, tempFloor, tempBuilding, tempNodeType, tempLongName, tempShortName);
                    Room r = null;
                    if (tempFloor.equals("4") && (tempNodeType.equals("CONF") || tempNodeType.equals("CLAS") || tempNodeType.equals("AUDI"))){
                        r = new Room(n);
                        System.out.println("Room made with id " + r.getId() + " name " + r.getName());
                    }

                    try {
                        if (r!=null){
                            RoomDB.addRoomDB(r);
                            System.out.println("Room " + r.getName() + " added to DB");
                        }
                        NodeDB.addNodeDB(n);
                        nodeHashMap.put(tempNodeID, n);

                    }catch (SQLException e)
                    {
                        e.printStackTrace();
                    }

                } catch (java.lang.NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}