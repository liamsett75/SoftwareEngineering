package RoomBooking;

import Databases.BookingDB;
import Databases.EmployeeDB;
import Databases.RoomDB;
import Employee.Employee;
import RequestFacade.IScheduler;
import Values.AppValues;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Booking {
    private String bookingID;
    private String description;
    private Employee requester;
    private Room room;
    private Calendar timeRequested;
    private Calendar scheduledTimeStart;
    private Calendar scheduledTimeEnd;
    private boolean isFullDay;


    public Booking(String bookingID, String description, Employee requester, Room location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, boolean isFullDay) {
        this.bookingID = bookingID;
        this.description = description;
        this.requester = requester;
        this.room = location;
        this.timeRequested = timeRequested;
        this.scheduledTimeStart = scheduledTimeStart;
        this.scheduledTimeEnd = scheduledTimeEnd;
        this.isFullDay = isFullDay;
    }

    //alternate constructor
    public Booking() {
        this.bookingID = null;
        this.description = null;
        this.requester = null;
        this.room = null;
        this.timeRequested = null;
        this.scheduledTimeStart = null;
        this.scheduledTimeEnd = null;
        this.isFullDay = false;
    }

    // getters
    public String getBookingID() {
        return bookingID;
    }

    public String getDescription() {
        return description;
    }

    public Employee getRequester() {
        return requester;
    }

    public Room getRoom() {
        return room;
    }

    public Room getLocation() {
        return room;
    }

    public Calendar getTimeRequested() {
        return timeRequested;
    }

    public Calendar getScheduledTimeStart() {
        return scheduledTimeStart;
    }

    public Calendar getScheduledTimeEnd() {
        return scheduledTimeEnd;
    }

    public boolean isFullDay() { return isFullDay; }

    // setters
    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setRequester(Employee requester) {
        this.requester = requester;
    }

    public void setLocation(Room location) {
        this.room = location;
    }

    public void setTimeRequested(Calendar timeRequested) {
        this.timeRequested = timeRequested;
    }

    public void setScheduledTimeStart(Calendar scheduledTimeStart) {
        this.scheduledTimeStart = scheduledTimeStart;
    }

    public void setScheduledTimeEnd(Calendar scheduledTimeEnd) {
        this.scheduledTimeEnd = scheduledTimeEnd;
    }

    public void setFullDay(boolean fullDay) { this.isFullDay = fullDay; }
//
//    public static void setAllBookings(LinkedList<Booking> allBookings) {
//        Booking.allBookings = allBookings;
//    }



    /**
     * Checks if the booking is valid by making sure that start and
     * end time are feasible and the room is available
     *
     * @param room      the location
     * @param timeStart The start time
     * @param timeEnd   The end time
     * @return true if the room is a valid booking or false if the room
     * is not a valid booking
     */
    public static boolean isValidBooking(Room room, Calendar timeStart, Calendar timeEnd) {
        CalendarBookingIntegrator cbi = AppValues.getInstance().cbi;

        //if date is valid and room is bookable, then keep checking
        if (IScheduler.isValidDate(timeStart, timeEnd) == true) {

            //if size of all bookings is not zero, then check each booking
            if (BookingDB.getAllBookingDB().size() != 0) {

                // checks that room is available at given time
                for (Booking b : BookingDB.getAllBookingDB()) {

                    //if booking we're checking is of the same location, continue checking, else skip
                    if (b.getLocation().equals(room)) {

                        if (b.isFullDay == true) {
//                            System.out.println("booking not valid -- isValidBooking -- fullDay");
                            return false;

                            //if room is not available to be booked at given times, return false, else keep checking
                        } else if (IScheduler.isAvailable(b.getScheduledTimeStart(), b.getScheduledTimeEnd(), timeStart, timeEnd) == false) {
//                            System.out.println("booking not valid -- isValidBooking -- IScheduler isavailable");
                            return false;

                        }
                    }
                }
                //if no booking returns unavailable then this booking is valid
                return true;
            }
            //if size of all bookings is zero, return true
            return true;
        }
        return false;
    }

    /**
     * Method completes all steps necessary to compute a booking
     *
     * @param requester The Requester of type Employee
     */
    static void bookingProcedure(Booking b, Employee requester) {
        requester.addToRequestedBookings(b); // adds booking to requester's map of bookings
        BookingDB.addBookingDB(b);
    }


    
    /**
     * Makes booking, and checks validity of inputs
     * @param requester
     * @param r
     * @param description
     * @param timeStart
     * @param timeEnd
     * @return
     */
    public Booking makeSafeBooking(Employee requester, Room r, String description, Calendar timeStart, Calendar timeEnd, boolean isFullDay) {
        if (r == null) {
            System.out.println("room is null -- makesafebooking");
            return null;
        }
        String id = AppValues.getInstance().bm.generateID();
        Calendar timeRequested = Calendar.getInstance();
        timeStart.set(Calendar.SECOND,0);
        timeEnd.set(Calendar.SECOND,0);

        Booking b=null;
        if (Booking.isValidBooking(r, timeStart, timeEnd) == true) {
            b = new Booking(id, description, requester, r, timeRequested, timeStart, timeEnd, isFullDay);
            Booking.bookingProcedure(b, requester);
            return b;
        }
        if (b!=null){
//            System.out.println("booking made!");
        }
        return null;
    }



    public Booking makeBookingForDB(ResultSet allBookingsResult){
        //no need to check for availability or time compatibility
        Booking booking = new Booking();

        try {
            booking.setBookingID(allBookingsResult.getString("BookingID"));
            booking.setRequester(Employee.getEmpFromID(EmployeeDB.getDBEmployees(), allBookingsResult.getString("RequesterID")));
            booking.setRoom(RoomDB.getRoomDBFromName(allBookingsResult.getString("RoomID"))); // might make db if make table
            booking.setDescription(allBookingsResult.getString("Description"));
            try {
                Date date = AppValues.getInstance().simpleDateFormat.parse(allBookingsResult.getString("TimeRequested"));
                booking.setTimeRequested(Calendar.getInstance());
                booking.getTimeRequested().setTime(date);
                date = AppValues.getInstance().simpleDateFormat.parse(allBookingsResult.getString("ScheduledTimeStart"));
                booking.setScheduledTimeStart(Calendar.getInstance());
                booking.getScheduledTimeStart().setTime(date);
                date = AppValues.getInstance().simpleDateFormat.parse(allBookingsResult.getString("ScheduledTimeEnd"));
                booking.setScheduledTimeEnd(Calendar.getInstance());
                booking.getScheduledTimeEnd().setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(allBookingsResult.getString("IsFullDay").equals("T")){
                booking.setFullDay(true);
            }
            else{
                booking.setFullDay(false);
            }
            allBookingsResult.getString("IsFullDay");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booking;
    }
}
