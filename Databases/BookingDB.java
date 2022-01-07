package Databases;

import RoomBooking.Booking;
import Values.AppValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;

public class BookingDB {

    // useful if you wan to drop every time the program starts
    public static void createBookingTable(){
        try {
            String bookingTable = "CREATE TABLE BOOKING(BookingID CHAR(6) PRIMARY KEY, RequesterID VARCHAR(10), RoomID VARCHAR(100), Description VARCHAR(100), TimeRequested VARCHAR(19), ScheduledTimeStart VARCHAR(19), ScheduledTimeEnd VARCHAR(19), IsFullDay CHAR(1), CONSTRAINT FullDay CHECK (IsFullDay IN ('T','F')))";
            PreparedStatement preparedStatement = getConnection().prepareStatement(bookingTable);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                String dropTable = "DROP TABLE BOOKING";
                PreparedStatement preparedStatement = getConnection().prepareStatement(dropTable);
                preparedStatement.executeUpdate();

                String bookingTable = "CREATE TABLE BOOKING(BookingID CHAR(6) PRIMARY KEY, RequesterID VARCHAR(10), RoomID VARCHAR(100), Description VARCHAR(100), TimeRequested VARCHAR(19), ScheduledTimeStart VARCHAR(19), ScheduledTimeEnd VARCHAR(19), IsFullDay CHAR(1), CONSTRAINT FullDay CHECK (IsFullDay IN ('T','F')))";
                PreparedStatement preparedStatement1 = getConnection().prepareStatement(bookingTable);
                preparedStatement1.executeUpdate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void addBookingDB(Booking booking){
        try {
            PreparedStatement addBookingStatement = getConnection().prepareStatement("Insert into BOOKING values (?, ?, ?, ?, ?, ?, ?, ?)");
            addBookingStatement.setString(1, booking.getBookingID());
            addBookingStatement.setString(2, booking.getRequester().getEmpID());
            addBookingStatement.setString(3, booking.getRoom().getName());
            addBookingStatement.setString(4, booking.getDescription());
            addBookingStatement.setString(5, AppValues.getInstance().simpleDateFormat.format(booking.getTimeRequested().getTime()));
            addBookingStatement.setString(6, AppValues.getInstance().simpleDateFormat.format(booking.getScheduledTimeStart().getTime()));
            addBookingStatement.setString(7, AppValues.getInstance().simpleDateFormat.format(booking.getScheduledTimeEnd().getTime()));

            if(booking.isFullDay()){
                addBookingStatement.setString(8, "T");
            }
            else{
                addBookingStatement.setString(8, "F");
            }

            addBookingStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBookingDB(Booking booking){
        try {
            PreparedStatement deleteBookingStatement = getConnection().prepareStatement("DELETE FROM BOOKING WHERE BookingID=?");
            deleteBookingStatement.setString(1, booking.getBookingID());
            deleteBookingStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editBookingDB(Booking oldBooking, Booking newBooking){
        deleteBookingDB(oldBooking);
        addBookingDB(newBooking);
    }

    public static void updateBookingDB(Booking newBooking){
        // updates the old etry
        try{
            PreparedStatement updateStatement = getConnection().prepareStatement("UPDATE BOOKING SET RequesterID=?, RoomID=?, Description=?, TimeRequested=?, ScheduledTimeStart=?, ScheduledTimeEnd=?, IsFullDay=?  WHERE BookingID=? ");
            updateStatement.setString(1, newBooking.getRequester().getEmpID());
            updateStatement.setString(2, newBooking.getRoom().getName());
            updateStatement.setString(3, newBooking.getDescription());
            updateStatement.setString(4, AppValues.getInstance().simpleDateFormat.format(newBooking.getTimeRequested().getTime()));
            updateStatement.setString(5, AppValues.getInstance().simpleDateFormat.format(newBooking.getScheduledTimeStart().getTime()));
            updateStatement.setString(6, AppValues.getInstance().simpleDateFormat.format(newBooking.getScheduledTimeEnd().getTime()));
            if(newBooking.isFullDay()){
                updateStatement.setString(7, "T");
            }
            else{
                updateStatement.setString(7, "F");
            }

            updateStatement.setString(8, newBooking.getBookingID());
            updateStatement.executeUpdate();

        } catch(SQLException e){

        }
    }


    public static LinkedList<Booking> getAllBookingDB(){
        Booking b = new Booking();
        LinkedList<Booking> bookingLinkedList = new LinkedList<>();
        try {
            PreparedStatement allBookings = getConnection().prepareStatement("SELECT * FROM BOOKING");
            ResultSet allBookingsResult = allBookings.executeQuery();

            while(allBookingsResult.next()){
                bookingLinkedList.add(b.makeBookingForDB(allBookingsResult));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingLinkedList;
    }


    // gets the booking by the bookingID
    public static Booking getBookingbyIDDB(String bookingID){
        Booking b = new Booking();
        Booking temp = null;
        try{
            PreparedStatement bookingStatement = getConnection().prepareStatement("SELECT * FROM BOOKING WHERE BookingID=?");
            bookingStatement.setString(1, bookingID);
            ResultSet bookingResult = bookingStatement.executeQuery();

            while(bookingResult.next()){
                temp = b.makeBookingForDB(bookingResult);
            }
        } catch (SQLException e){

        }
        return temp;
    }

    //
    public static LinkedList<Booking> getBookingByRequesterDB(String EmployeeID){
        Booking b = new Booking();
        LinkedList<Booking> bookingLinkedList = new LinkedList<>();
        try{
            PreparedStatement bookingsByReqester = getConnection().prepareStatement("SELECT * FROM BOOKING WHERE RequesterID=?");
            bookingsByReqester.setString(1, EmployeeID);
            ResultSet bookingResult = bookingsByReqester.executeQuery();

            while(bookingResult.next()){
                bookingLinkedList.add(b.makeBookingForDB(bookingResult));
            }

        } catch(SQLException e){

        }
        return bookingLinkedList;
    }


    public static LinkedList<Booking> getBookingByRoomIDDB(String RoomID){
        Booking b = new Booking();
        LinkedList<Booking> bookingLinkedList = new LinkedList<>();
        try{
            PreparedStatement bookingsByRoomID = getConnection().prepareStatement("SELECT * FROM BOOKING WHERE RoomID=?");
            bookingsByRoomID.setString(1, RoomID);
            ResultSet bookingResult = bookingsByRoomID.executeQuery();

            while (bookingResult.next()){
                bookingLinkedList.add(b.makeBookingForDB(bookingResult));
            }
        } catch(SQLException e){

        }
        return bookingLinkedList;
    }


    //
//    public static LinkedList<Booking> getBookingsWithinTimeFrameDB(Calendar calendarStart, Calendar calendarEnd, Room room){
//        Booking b = new Booking();
//        LinkedList<Booking> bookingLinkedList = new LinkedList<>();
//        PreparedStatement bookingsWithinTimeFrame = null; // depends if the room is null
//        ResultSet bookingResult = null; // depends on if the room is null
//        if(room!=null){
//            try {
//                bookingsWithinTimeFrame = getConnection().prepareStatement("SELECT * FROM BOOKING WHERE RoomID=?");
//                bookingsWithinTimeFrame.setString(1,room.getId());
//                bookingResult = bookingsWithinTimeFrame.executeQuery();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            try {
//                bookingsWithinTimeFrame = getConnection().prepareStatement("SELECT * FROM BOOKING");
//                bookingResult = bookingsWithinTimeFrame.executeQuery();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try{
//            while(bookingResult.next()){
//                Booking booking = b.makeBookingForDB(bookingResult);
//
//            }
//        } catch(SQLException e){
//            e.printStackTrace();
//        }
//
//
//
//    }
}
