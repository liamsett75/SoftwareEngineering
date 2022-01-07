package RoomBooking;

import Employee.Employee;

import java.sql.ResultSet;
import java.util.Calendar;

public class BookingMaker {
    private Booking rb = new Booking();

    public Booking makeSafeBooking(Employee requester, Room room, String description, Calendar timeStart, Calendar timeEnd, boolean isFullDay) {
        return rb.makeSafeBooking(requester, room, description, timeStart, timeEnd, isFullDay);
    }

    int i = 0;
    public String generateID() {
        return "BOOK" + i++;
    }


    public Booking makeBookingDB(ResultSet allBookingsResult){
        return rb.makeBookingForDB(allBookingsResult);
    }
}