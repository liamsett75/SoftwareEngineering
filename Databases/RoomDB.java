package Databases;

import RoomBooking.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;


public class RoomDB {


    public static void createRoomTable() {
        try {
            String roomTable = "CREATE TABLE ROOMS(roomID CHAR(10) PRIMARY KEY, roomName VARCHAR(100), roomType VARCHAR(4), roomFloor VARCHAR(2), roomXCoord FLOAT, roomYCoord FLOAT)";
            PreparedStatement preparedStatement = getConnection().prepareStatement(roomTable);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            try {
                String dropTable = "DROP TABLE ROOMS";
                PreparedStatement ps = getConnection().prepareStatement(dropTable);
                ps.executeUpdate();
                String roomTable = "CREATE TABLE ROOMS(roomID CHAR(10) PRIMARY KEY, roomName VARCHAR(100), roomType VARCHAR(4), roomFloor VARCHAR(2), roomXCoord FLOAT, roomYCoord FLOAT)";
                PreparedStatement preparedStatement = getConnection().prepareStatement(roomTable);
                preparedStatement.executeUpdate();


            } catch (SQLException e1){
                e1.printStackTrace();
            }
        }
    }


    //when rooms are created you automatically add them to the database, then put them on hm with their start and end dates, so you have a hm of room and (hm of start and end date)

    //adds room to db
    public static void addRoomDB (Room room){
        try{
            PreparedStatement addRoomStatement = getConnection().prepareStatement("Insert into ROOMS values (?,?,?,?,?,?)");
            addRoomStatement.setString(1,room.getId());
            addRoomStatement.setString(2,room.getName());
            addRoomStatement.setString(3,room.getRoomType());
            addRoomStatement.setString(4,room.getFloor());
            addRoomStatement.setFloat(5,(float)room.getxCoord());
            addRoomStatement.setFloat(6,(float)room.getyCoord());
            int i = addRoomStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //deletes room from db
    public static void deleteRoomDB (Room room){
        try {
            Statement roomDelete = getConnection().createStatement();
            String deleteStatement = String.format("DELETE FROM Room WHERE roomID=%'s'", room.getId());
            roomDelete.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //creates linked list of rooms from database
    public static LinkedList<Room> getDBRooms(){
        LinkedList<Room> tempList = new LinkedList<>();
        Room tempRoom;

        try{
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM ROOMS";
            ResultSet rset = stmt.executeQuery(query);

            String tempRoomId;
            String tempRoomName;
            String tempRoomType;
            String tempFloor;
            double tempRoomXCoord;
            double tempRoomYCoord;

            while (rset.next()){
                tempRoomId = rset.getString("roomId");
                tempRoomName = rset.getString("roomName");
                tempRoomType = rset.getString("roomType");
                tempFloor = rset.getString("roomFloor");
                tempRoomXCoord = rset.getFloat("roomXCoord");
                tempRoomYCoord = rset.getFloat("roomYCoord");

                tempRoom = new Room(tempRoomId, tempRoomName, tempRoomType, tempFloor, tempRoomXCoord, tempRoomYCoord);
                tempList.add(tempRoom);
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        return tempList;
    }

    //returns room object of specified id
    public static Room getRoomDBFromID(String id){
        Room tempRoom = null;

        String tempRoomId;
        String tempRoomName;
        String tempRoomType;
        String tempFloor;
        double tempRoomXCoord;
        double tempRoomYCoord;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM ROOMS WHERE ROOMID = " + "'" + id + "'";
            System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);
            rset.next();

            tempRoomId = rset.getString("roomId");
            tempRoomName = rset.getString("roomName");
            tempRoomType = rset.getString("roomType");
            tempFloor = rset.getString("roomFloor");
            tempRoomXCoord = rset.getFloat("roomXCoord");
            tempRoomYCoord = rset.getFloat("roomYCoord");
            rset.close();

            tempRoom = new Room(tempRoomId, tempRoomName, tempRoomType, tempFloor, tempRoomXCoord, tempRoomYCoord);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempRoom;
    }



    //returns room object of specified name
    public static Room getRoomDBFromName(String roomName){
        Room tempRoom = null;

        String tempRoomId;
        String tempRoomName;
        String tempRoomType;
        String tempFloor;
        double tempRoomXCoord;
        double tempRoomYCoord;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM ROOMS WHERE ROOMNAME = " + "'" + roomName + "'";
//            System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);
            rset.next();

            tempRoomId = rset.getString("roomId");
            tempRoomName = rset.getString("roomName");
            tempRoomType = rset.getString("roomType");
            tempFloor = rset.getString("roomFloor");
            tempRoomXCoord = rset.getFloat("roomXCoord");
            tempRoomYCoord = rset.getFloat("roomYCoord");
            rset.close();

            tempRoom = new Room(tempRoomId, tempRoomName, tempRoomType, tempFloor, tempRoomXCoord, tempRoomYCoord);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempRoom;
    }
}