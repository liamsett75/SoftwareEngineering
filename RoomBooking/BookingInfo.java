package RoomBooking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BookingInfo {

    /*
    private String bookingID;
    private String bookingType; //can be conf or lab
    private String description;
    private Employee requester;
    private Room room;
    private Calendar timeRequested;
    private Calendar scheduledTimeStart;
    private Calendar scheduledTimeEnd;
    private boolean completed;
    private boolean cancelled;
    */

    private String bookingID;
    private String bookingType; //can be conf or lab
    private String description;
    private String requesterID;
    private String roomName;
    private String timeRequested;
    private String scheduledTimeStart;
    private String scheduledTimeEnd;
    private boolean completed;
    private boolean cancelled;

    public BookingInfo(String bookingID, String bookingType, String description, String requesterID, String roomName, String timeRequested, String scheduledTimeStart, String scheduledTimeEnd, boolean completed, boolean cancelled) {
        this.bookingID = bookingID;
        this.bookingType = bookingType;
        this.description = description;
        this.requesterID = requesterID;
        this.roomName = roomName;
        this.timeRequested = timeRequested;
        this.scheduledTimeStart = scheduledTimeStart;
        this.scheduledTimeEnd = scheduledTimeEnd;
        this.completed = completed;
        this.cancelled = cancelled;
    }

    public BookingInfo() {
        this.bookingID = "";
        this.bookingType = "";
        this.description = "";
        this.requesterID = "";
        this.roomName = "";
        this.timeRequested = "";
        this.scheduledTimeStart = "";
        this.scheduledTimeEnd = "";
        this.completed = false;
        this.cancelled = false;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(String timeRequested) {
        this.timeRequested = timeRequested;
    }

    public String getScheduledTimeStart() {
        return scheduledTimeStart;
    }

    public void setScheduledTimeStart(String scheduledTimeStart) {
        this.scheduledTimeStart = scheduledTimeStart;
    }

    public String getScheduledTimeEnd() {
        return scheduledTimeEnd;
    }

    public void setScheduledTimeEnd(String scheduledTimeEnd) {
        this.scheduledTimeEnd = scheduledTimeEnd;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

 //--------------------------------------------------------------

    public static BookingInfo bookingToInfo(Booking b)
    {
        DateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/YYYY HH:MM");

        BookingInfo bi = new BookingInfo();
        bi.setBookingID(b.getBookingID());
        bi.setDescription(b.getDescription());
        bi.setRequesterID(b.getRequester().getEmpID());
        bi.setRoomName(b.getRoom().getName());
        SimpleDateFormat time = new SimpleDateFormat("EEE MMM dd");
        bi.setTimeRequested(b.getTimeRequested().toString());

       // bi.setScheduledTimeStart(b.getScheduledTimeStart().toString());
      //  bi.setScheduledTimeEnd(b.getScheduledTimeEnd().toString());
        return bi;
    }
}