package RoomBooking;

import Graph.Node;
import Values.AppValues;

import java.util.HashMap;
import java.util.LinkedList;

public class RoomMaker {

    private HashMap <Room, Node> allRooms;

    //constructor
    public RoomMaker (){
        allRooms = new HashMap<Room,Node>();
    }

    public void makeRooms (LinkedList<Node> allNodes){
        for (Node n : allNodes){
            if((n.getFloor().equals("4")) && (n.getNodeType().equalsIgnoreCase("CONF") || n.getNodeType().equalsIgnoreCase("CLAS") || n.getNodeType().equalsIgnoreCase("AUDI"))){
                Room r = new Room (n);
                allRooms.putIfAbsent(r,n);
            }
        }
    }


    public HashMap<Room,Node> getRooms(){
        return this.allRooms;
    }

    public Room getRoomFromName (String longName) {
        for (Node n : AppValues.getInstance().allNodesList) {
            if (n.getLongName().equals(longName)) {
                for (Room r : allRooms.keySet()) {
                    if (allRooms.get(r).equals(n)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

}


