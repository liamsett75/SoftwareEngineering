package RoomBooking;

import Databases.BookingDB;
import Databases.NodeDB;
import Graph.Node;
import RequestFacade.IScheduler;

import java.util.Calendar;
import java.util.LinkedList;

public class Room {
    String name;
    String id;
    String roomType;
    String floor;
    double xCoord;
    double yCoord;

    public Room(Node n) {
        if (n == null){
            this.name = null;
            this.id = null;
            this.roomType = null;
            this.floor = null;
            this.xCoord = 0;
            this.yCoord = 0;
        } else {
            this.name = n.getLongName();
            this.id = n.getNodeID();
            this.roomType = n.getNodeType();
            this.floor = n.getFloor();
            this.xCoord = n.getXCoord();
            this.yCoord = n.getYCoord();
        }

    }

    public Room(String roomId, String roomName, String roomType, String roomFloor, double xCoord, double yCoord){
        this.id = roomId;
        this.name = roomName;
        this.roomType = roomType;
        this.floor = roomFloor;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    //getters
    public String getName() { return name; }
    public String getId() { return id; }
    public String getRoomType() { return roomType; }
    public String getFloor() {
        return floor;
    }
    public double getxCoord() {
        return xCoord;
    }
    public double getyCoord() {
        return yCoord;
    }

    //setters
    public void setName(String name) { this.name = name; }
    public void setId(String id) { this.id = id; }
    public void setRoomType(String roomType) { this.roomType = roomType; }



//    public static LinkedList<Room> getBookableRooms(LinkedList<Node> allNodes){
//
//        LinkedList<Room> bookableRooms = new LinkedList<>();
//        //System.out.println("getting bookable rooms");
//        for (Node n : allNodes){
//            if ((n.getFloor().equals("4")) && (n.getNodeType().equals("CONF") || n.getNodeType().equals("CLAS") || n.getNodeType().equals("AUDI"))){
//                Room r = new Room(n);
//                bookableRooms.add(r);
//                //System.out.println(r.getName());
//            }
//        }
////        System.out.println("bookable room list size is: " + bookableRooms.size());
//        return bookableRooms;
//    }

    public static boolean isBooked(Room r, Calendar startTime, Calendar endTime){
        LinkedList<Booking> tempBook = BookingDB.getBookingByRoomIDDB(r.getName());
        if(tempBook.size()!=0) {
            for (Booking b: tempBook){
                if (!IScheduler.isAvailable(b.getScheduledTimeStart(), b.getScheduledTimeEnd(), startTime, endTime)){
                    return true;
                }
            }
        }
        return false;
    }

    //make list of existing scheduled rooms, given a date and time
    public static LinkedList<Room> getBookedRooms (LinkedList<Room> allRooms, Calendar startTime, Calendar endTime){
        LinkedList<Room> booked = new LinkedList<>();
        for (Room r : allRooms){
            LinkedList<Booking> tempBook = BookingDB.getBookingByRoomIDDB(r.getId());
            if (tempBook.size()!=0){
                for (Booking b : tempBook) {
                    if (!IScheduler.isAvailable(b.getScheduledTimeStart(), b.getScheduledTimeEnd(), startTime, endTime)) {
                        booked.add(r);
                    }
                }
            }
        }
        return booked;
    }

    public Room getRoomFromName(LinkedList<Room> allRooms, String name){
        Room room = new Room(null);
        for(Room r : allRooms){
            if( r.getName().equals(name)) {
                room = r;
            }
        }
        return room;
    }


    public static Node getNodeFromRoom(Room r) {
        Node n = NodeDB.getNodeDB(r.getId());
        return n;
    }

    @Override
    public boolean equals(Object obj) {
       if (obj == null) return false;
       if (((Room) obj).name == null) return false;
       if (name == null) return false;
       if (name.equals(((Room) obj).name)) return true;
       return false;
    }
}
