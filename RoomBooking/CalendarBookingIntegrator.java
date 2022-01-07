package RoomBooking;

import Databases.BookingDB;
import Databases.RoomDB;
import RequestFacade.IScheduler;
import UIControllers.MainScreenController;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import Values.AppValues;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.YEARS;

//import java.util.Calendar;

//import sun.applet.Main;

public class CalendarBookingIntegrator {

    @FXML
    MainScreenController mainScreenController;

    public CalendarBookingIntegrator(){

    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    //master list of all bookings
    private LinkedList<Booking> allBookings = new LinkedList<>();

//
//
//    /**
//     * Adds the given Booking to the list of all Bookings
//     *
//     * @param b A single Booking
//     */
//    public synchronized void addBooking(Booking b) {
//        AppValues.getInstance().cbi.getAllBookings().add(b);
////      System.out.println("size of all bookings is " + allBookings.size());
//    }

    public synchronized void removeBooking(Booking b) {

        if (BookingDB.getAllBookingDB().contains(b)) {
            System.out.println("booking found! -- removeBooking");
            //deleting booking from employees list of bookings
            for (Booking tempB : BookingDB.getBookingByRequesterDB(AppValues.getInstance().curEmp.getEmpID())) {
                if (tempB.getBookingID().equals(b.getBookingID())) {
                    AppValues.getInstance().curEmp.getRequestedBookings().remove(tempB);

                }
            }
            //remove booking from database
            BookingDB.deleteBookingDB(b);
        }
    }

    //update booking
    public Booking updateBooking(Entry entry) {
        //check through list of all bookings for booking id = entry id or booking description =
        //"Taken from Calendar, with cal id: " + entry.getId()
        if (BookingDB.getAllBookingDB() != null) {
            for (Booking b : BookingDB.getAllBookingDB()) {
                //if booking found, then delete that booking and make booking from entry with new params
                if (b.getBookingID().equals(entry.getId()) || b.getDescription().equals("# " + entry.getId() + " #")) {
                    removeBooking(b);
                    Booking newBooking = makeBookingFromEntry(entry);
                    return b;
                }
            }
        }

        //when booking not found or list of bookings is empty, then just make booking from entry
        return makeBookingFromEntry(entry);
    }

    public Entry makeEntryFromBooking(CalendarView cv, Calendar cal, Booking b) {
        //getting start and end datetime objects
        TimeZone tz = TimeZone.getDefault();
        LocalDateTime startDT = LocalDateTime.ofInstant(b.getScheduledTimeStart().toInstant(), ZoneId.systemDefault());
        LocalDateTime endDt = LocalDateTime.ofInstant(b.getScheduledTimeEnd().toInstant(), ZoneId.systemDefault());
        System.out.println("local datetime in makeEntryFromBooking: start : " + startDT.toString() + " end : " + endDt.toString());
        //creating interval object from dates and times

        //System.out.println("zone is:  " + tz.toString());
//        System.out.println("local start time is: " + startDT.toString());
        Interval interval = new Interval(startDT, endDt);
        String location = b.getRoom().getName();

        //creating agenda item with time and location
        Entry newEntry = new Entry("Auto-Generated Booking for " + location, interval);
        newEntry.setLocation(location);
        newEntry.setCalendar(cal);
        newEntry.setFullDay(false);
        newEntry.setId("ENT"+AppValues.getInstance().entryId++  );
        cal.addEntry(newEntry);

        return newEntry;

        //TODO: for each employee, generate a personal Calendar and add this booking there when made
        //TODO: add full day and recurring as options for bookings (add the fields in the constructor)
    }



    public LinkedList<Booking> makeBookingFromAllEntries(Calendar cal, Entry entry) {
//        System.out.println("booking from entry called");

        List<Entry> calBookings = new LinkedList<>();
        LinkedList<Booking> newBookings = new LinkedList<>();

        Map<LocalDate, List<Entry<?>>> allEntries = cal.findEntries(LocalDate.now().minus(1, YEARS), LocalDate.now().plus(1, YEARS), ZoneId.systemDefault());


        for (List<Entry<?>> list : allEntries.values()) {
            calBookings = list.stream().filter(CalendarBookingIntegrator::titleFilterString).collect(Collectors.toList());
        }

        System.out.println(calBookings);
        for (Entry<?> e : calBookings){
            newBookings.add(makeBookingFromEntry(e));
        }

        for(Booking b : newBookings){
            if (b==null){
//                System.out.println("b is null");
            } else {
//                System.out.println("booking not null, has id of: " + b.getBookingID());
            }
        }
        return newBookings;
    }

    public Booking makeBookingFromEntry(Entry entry){

        //getting dates from appointment and converting to Calendar objects
        LocalDateTime startDT = entry.getStartAsLocalDateTime().withSecond(0).withNano(0);
        LocalDateTime endDt = entry.getEndAsLocalDateTime().withSecond(0).withNano(0);

        Date dateStart = Date.from(startDT.atZone(ZoneId.systemDefault()).withSecond(0).withNano(0).toInstant());
        Date dateEnd = Date.from(endDt.atZone(ZoneId.systemDefault()).withSecond(0).withNano(0).toInstant());

        java.util.Calendar timeStart = java.util.Calendar.getInstance();
        java.util.Calendar timeEnd = java.util.Calendar.getInstance();

        timeStart.setTime(dateStart);
        timeEnd.setTime(dateEnd);

        Booking tempBooking = null;

        LinkedList<Booking> toBeDeleted = new LinkedList<>();


        //check if other booking of same room is made at exact same time or about the same time
        Room r = RoomDB.getRoomDBFromName(entry.getLocation());
        if (r != null) {
            for (Booking b : BookingDB.getBookingByRoomIDDB(r.getId())) { //concurrency happens here!

                LocalDateTime bookingStartDT = LocalDateTime.ofInstant(b.getScheduledTimeStart().toInstant(), b.getScheduledTimeStart().getTimeZone().toZoneId()).withSecond(0).withNano(0);
                LocalDateTime bookingEndDT = LocalDateTime.ofInstant(b.getScheduledTimeEnd().toInstant(), b.getScheduledTimeEnd().getTimeZone().toZoneId()).withSecond(0).withNano(0);

                System.out.println();
                System.out.println("booking times: start --- " + bookingStartDT.toString());
                System.out.println("end --- " + bookingEndDT.toString());
                System.out.println("entry times: start --- " + startDT.toString());
                System.out.println("end --- " + endDt.toString());
                System.out.println("testing out equality of startitmes:" + bookingStartDT.equals(entry.getStartAsLocalDateTime().withSecond(0).withNano(0)));
                System.out.println();


                //if it is at exact same time, then remove the previous booking and add the new one
                if (bookingStartDT.equals(entry.getStartAsLocalDateTime().withSecond(0).withNano(0)) && bookingEndDT.equals(entry.getEndAsLocalDateTime().withSecond(0).withNano(0))) {
                    System.out.println("exact same timeframe found. will remove booking");
                    removeBooking(b);
                } else if (!IScheduler.isAvailableLDT(bookingStartDT, bookingEndDT, startDT, endDt)){
                    //if it is at relative same time, then cannot make booking, return null
                    System.out.println("there exists another booking in the same room and timeframe as this booking");
                } else {
                    System.out.println("no issues with scheduler in make booking from entry");
                }

            }
        }


        boolean isFullDay = entry.isFullDay();
        //CHANGE ALL INSTANCES OF JAVA CALENDAR TO LOCALDATETIME IN BOOKING!!
        tempBooking = AppValues.getInstance().bm.makeSafeBooking(AppValues.getInstance().curEmp, RoomDB.getRoomDBFromName(entry.getLocation()), "Taken from Calendar, with entry id: " + entry.getId(), timeStart, timeEnd, isFullDay);
        if (tempBooking != null) {
            System.out.println("booking not null!! -- makeBookingFromEntry");
        } else {
            System.out.println("booking is null! -- makeBookingFromEntry -- something wrong with makeSafeBooking");
        }
        System.out.println();

//        System.out.println("going through other for loop inside makebookingfromentry");
//        //check if any other booking was taken from Calendar with same entry id
//        for (Booking bb : AppValues.getInstance().cbi.getAllBookings()){
//            System.out.println("checking for duplicate entries bookings booking with info: id: " + bb.getRoom().getId() + " with room: " + bb.getRoom().getName());
//            if (bb.getDescription()!=null){
//                if (bb.getDescription().equalsIgnoreCase("Taken from Calendar, with entry id: " + entry.getId())){
//                    System.out.println("booking with same entry id exists! removing now");
//                    removeBooking(bb);
//                }
//            }
//        }
        return tempBooking;
    }



        //if it is then stop and say you cant make booking (return null)
        //if it isnt, continue making booking
        //then check if other booking of same id exists. if it does, delete it



//        //getting dates from appointment and converting to Calendar objects
//        LocalDateTime startDT = entry.getStartAsLocalDateTime();
//        LocalDateTime endDt = entry.getEndAsLocalDateTime();
//
//        Date dateStart = Date.from(startDT.atZone(ZoneId.systemDefault()).toInstant());
//        Date dateEnd = Date.from(endDt.atZone(ZoneId.systemDefault()).toInstant());
//
//        java.util.Calendar timeStart = java.util.Calendar.getInstance();
//        java.util.Calendar timeEnd = java.util.Calendar.getInstance();
//
//        timeStart.setTime(dateStart);
//        timeEnd.setTime(dateEnd);
//
//        boolean isFullDay = entry.isFullDay();
//
//        //getting description
//        System.out.println("time start of new booking from entry is : " + timeStart.getTime().toString() + "time end is: " + timeEnd.getTime().toString());
//
//        //checking if previous identical booking exists
//        if(findBookingOfEntry(entry)!=null){
//            System.out.println("booking matching this entry was found!!");
//        } else {
//            System.out.println("no other booking matches this entry");
//            //making room from info
////        System.out.println("size of room list is: " + AppValues.getInstance().rm.getRooms().size());
//            for (Room r : AppValues.getInstance().rm.getRooms().keySet()){
//                System.out.println("currently checking room " + r.getName());
//                if (r.getName().equals(entry.getLocation())){
//                    //making booking from info
//                    System.out.println("room found, making bookings");
//                    Booking b = AppValues.getInstance().bm.makeSafeBooking(AppValues.getInstance().curEmp, r, "Taken from Calendar, with cal id: " + entry.getId(), timeStart, timeEnd, isFullDay);
//                    if (b!=null){
//                        System.out.println("booking not null!! -- makeBookingFromEntry");
//                    } else {
//                        System.out.println("booking is null! -- makeBookingFromEntry");
//                    }
//                    System.out.println(r.getName());
//                    return b;
//                }
//            }
//        }
//        return null;

    public LinkedList<java.util.Calendar> calendarDatesFromEntry(Entry entry){
        LinkedList<java.util.Calendar> ll = new LinkedList();
        java.util.Calendar startDT = java.util.Calendar.getInstance();
        java.util.Calendar endDT = java.util.Calendar.getInstance();

        LocalDateTime entryStartDT = entry.getInterval().getStartDateTime().withSecond(0).withNano(0);
        LocalDateTime entryEndDT = entry.getInterval().getEndDateTime().withSecond(0).withNano(0);

        startDT.set(entryStartDT.getYear(), entryStartDT.getMonthValue()-1, entryStartDT.getDayOfMonth(), entryStartDT.getHour(), entryStartDT.getMinute(), 0);
        endDT.set(entryEndDT.getYear(), entryEndDT.getMonthValue()-1, entryEndDT.getDayOfMonth(), entryEndDT.getHour(), entryEndDT.getMinute(), 0);
        ll.addFirst(startDT);
        ll.addLast(endDT);
        System.out.println("real start date is: " + entry.getStartAsLocalDateTime().toString());
        System.out.println("real end date is: " + entry.getEndAsLocalDateTime().toString());
        System.out.println("converted start date is: " + ll.getFirst().getTime().toString());
        System.out.println("converted end date is: " + ll.getLast().getTime().toString());
        return ll;
    }


    public Booking findBookingOfEntry(Entry entry){
        for (Booking b : BookingDB.getAllBookingDB()) {
            String[] idFromDesc = b.getDescription().split("#");
            System.out.println("complete desc is: " + b.getDescription());
            System.out.println("id from desc is:" + idFromDesc[1].trim());
            System.out.println("current entry id is:" + entry.getId());
            if (entry.getId().equals(idFromDesc[1].trim())) {
                System.out.println("booking for this entry is found");
                return b;
            } else {
                LocalDateTime entryST = entry.getInterval().getStartDateTime().withSecond(0).withNano(0);
                LocalDateTime entryET = entry.getInterval().getEndDateTime().withSecond(0).withNano(0);

                java.util.Calendar startCal = calendarDatesFromEntry(entry).getFirst();
                java.util.Calendar endCal = calendarDatesFromEntry(entry).getLast();


                if (b.getScheduledTimeStart().equals(startCal) && b.getScheduledTimeEnd().equals(endCal) && b.getRoom().getName().equals(entry.getLocation())){
//                    System.out.println("booking for this entry is found");
                    return b;
                }
            }
        }
//        System.out.println("booking for this entry not found");
        return null;
    }

    private static boolean titleFilterString(Entry entry) {
        return entry.getTitle().contains("Auto-Generated Booking for ");
    }

    public int updateBookingTimeOnEntryChanged(Entry<?> oldEntry, Entry<?> newEntry){
        //returns 0 if booking made
        //returns 1 if booking of this entry not found
        //returns -1 if new booking of this entry could not be made
        System.out.println("update booking time on entry changed called");
        //finding instance of booking of old entry
        Booking b = findBookingOfEntry(oldEntry);
        if(b!=null) {
            //if that instance exists, remove it
            System.out.println("booking instance of old entry found and being removed");
            removeBooking(b);
        }
        for (Room r : RoomDB.getDBRooms()){
            if (r.getName().equals(newEntry.getLocation())){
                if (makeBookingFromEntry(newEntry)!=null){
                    System.out.println("new booking for this entry made -- updateBookingTime");
                    return 0;
                } else {
//                    System.out.println("new booking for this entry couldn't be made -- updateBookingTime");
                    return -1;
                }
            }
        }
        //booking for this entry not found
        System.out.println("booking for this entry not found, will create new booking -- updateBookingTime");
//        makeBookingFromEntry(ntry);
        return 1;
    }

    public boolean updateBookingLocationOnEntryChanged(Entry entry){
        Booking b = findBookingOfEntry(entry);
        if(b!=null){
            Room r = RoomDB.getRoomDBFromName(entry.getLocation());
            if (r!=null){
                removeBooking(b);
                makeBookingFromEntry(entry);
                return true;
            }
        }
        return false;
    }

    public void deleteBookingOnEntryDeleted(Entry entry){
        removeBooking(findBookingOfEntry(entry));
    }

    public void updateBookingRecurrence(){

    }

    public  void updateBookingFullDay(Entry entry){
        Booking b = findBookingOfEntry(entry);
        if(b!=null){
            removeBooking(b);
            AppValues.getInstance().bm.makeSafeBooking(b.getRequester(),b.getRoom(),b.getDescription(),b.getScheduledTimeStart(),b.getScheduledTimeEnd(),entry.isFullDay());
        }
    }

   int i = 0;
   public String generateEntryID(){
        return "APP" + i;
   }




}


//    public void setAppointmentCategoryByLocation(Agenda.Appointment appt, String location){
//        switch (location){
//            case "BRB Classroom 1":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group0"));
//                break;
//            case "BRB Classroom 2":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
//                break;
//            case "BRB Classroom 3":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group2"));
//                break;
//            case "BRB Classroom 4":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group3"));
//                break;
//            case "BRB Classroom 5":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group4"));
//                break;
//            case "BRB Classroom 6":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group5"));
//                break;
//            case "BRB Classroom 7":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group6"));
//                break;
//            case "BRB Classroom 8":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group7"));
//                break;
//            case "Mission Hill Auditorium":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group8"));
//                break;
//            case "Mission Hill Conference Room":
//                appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group9").withDescription("Conference Room"));
//                break;
//                default:
//                    appt.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group10"));
//                    break;
//
//        }
//    }
//
//    public void setCalendarCategories(ICalendarAgenda agenda){
//        agenda.getCategories().add(0,"Classroom1");
//        agenda.getCategories().add(1,"Classroom2");
//        agenda.getCategories().add(2,"Classroom3");
//        agenda.getCategories().add(3,"Classroom4");
//        agenda.getCategories().add(4,"Classroom5");
//        agenda.getCategories().add(5,"Classroom6");
//        agenda.getCategories().add(6,"Classroom7");
//        agenda.getCategories().add(7,"Classroom8");
//        agenda.getCategories().add(8,"Auditorium");
//        agenda.getCategories().add(9,"Conference Room");
//        agenda.getCategories().add(10,"Lab");
//
//    }