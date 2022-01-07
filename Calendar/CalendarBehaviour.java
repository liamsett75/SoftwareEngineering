package Calendar;

import Databases.BookingDB;
import Databases.RoomDB;
import RoomBooking.Booking;
import RoomBooking.CalendarBookingIntegrator;
import RoomBooking.Room;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.event.EventHandler;
import Values.AppValues;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CalendarBehaviour {
    //constructor
    public CalendarBehaviour(){}
    //private Calendar bsCal = new Calendar();
    //DateControl dateControl;





    public EventHandler<CalendarEvent> handler = new EventHandler<CalendarEvent>() {

        boolean fuckThis = false;
        int iter = 0;

        @Override
        public void handle(CalendarEvent event) {
            AppValues.getInstance().calendarView.refreshData();
            CalendarBookingIntegrator cbi = AppValues.getInstance().cbi;

            if (event.getEventType().equals(CalendarEvent.ENTRY_INTERVAL_CHANGED)) {
//                System.out.println("update time is called");
//                System.out.println();
//                System.out.println("old interval is: start: " + event.getOldInterval().getStartTime().toString() + " end " + event.getOldInterval().getEndTime().toString());
//                System.out.println("new interval is: start: " + event.getEntry().getInterval().getStartTime().toString() + " end " + event.getEntry().getInterval().getEndTime().toString());
//
                Entry oldEntry = new Entry("temporary", event.getOldInterval());
                oldEntry.setLocation(event.getEntry().getLocation());
                oldEntry.setTitle(event.getEntry().getTitle());
                oldEntry.setId(event.getEntry().getId());
                System.out.println("old entry created with no problems");
                System.out.println("old entry params: " + "id " + oldEntry.getId() + " location " + oldEntry.getLocation());


                String oldBookingID = null;
                long durationInSecs = event.getEntry().getDuration().getSeconds();

                HashMap<LocalDate, List<Entry>> midEntries = (HashMap)event.getCalendar().findEntries(event.getEntry().getStartDate(), event.getEntry().getEndDate(), ZoneId.systemDefault());
                Collection c = midEntries.values();
                Iterator i = c.iterator();

                Booking oldBooking = null;
                Booking newBooking = null;

                while (i.hasNext()){
                    ArrayList<Entry> temp = (ArrayList<Entry>) i.next();
                    for(Entry e : temp){
                        if (e.getId().equals(event.getEntry().getId())){
                            //save booking id of old booking

                            //this should not return null pointer exception.
                            // if it does we've done something else wrong
                            oldBookingID = AppValues.getInstance().cbi.findBookingOfEntry(oldEntry).getBookingID();
                            //delete old booking
                            oldBooking = AppValues.getInstance().cbi.findBookingOfEntry(oldEntry);
                            BookingDB.deleteBookingDB(AppValues.getInstance().cbi.findBookingOfEntry(oldEntry));

                            //make new booking at the new spot
                            Room tempr = RoomDB.getRoomDBFromName(event.getEntry().getLocation());
                            java.util.Calendar startCal = AppValues.getInstance().cbi.calendarDatesFromEntry(event.getEntry()).getFirst();
                            java.util.Calendar endCal = AppValues.getInstance().cbi.calendarDatesFromEntry(event.getEntry()).getLast();
                            newBooking = new Booking (oldBookingID, "# " + event.getEntry().getId() + " #", AppValues.getInstance().curEmp, tempr, java.util.Calendar.getInstance(),startCal,endCal,event.getEntry().isFullDay());
                            BookingDB.addBookingDB(newBooking);
                            //save this booking in a class variable
                            //might end up in an infinite loop??

                        }
                        else if (e.getLocation().equals(event.getEntry().getLocation())){
                            //if there is another location with different event id later,
                            // then delete booking you just made and remake the old one
                            //only time when its null is when it didnt find another entry of same id, so only add old booking in not null
                            if (newBooking!=null){
                                BookingDB.deleteBookingDB(newBooking);
                                newBooking = null;
                                if (oldBooking!= null){
                                    BookingDB.addBookingDB(oldBooking);
                                } else return;
                            }
                            System.out.println("before setting to old interval");
                            event.getEntry().setInterval(event.getOldInterval());
                            System.out.println("after setting to old interval");
                            return;
                            //event.getCalendar().removeEntry(event.getEntry());
                        }
                    }
                    oldBooking=null;
                }
//                try {
//                    event.getEntry();
//                } catch (NullPointerException e) {
//                    System.out.println("Caught null pointer on event.getEntry");
//
//                    return;
//                }
//
//
//                if (event.getEntry() != null) {
//                    System.out.println("about to call make booking from event");
//
//                    System.out.println("Value of event.getEntry: " + event.getEntry());
//
//                    if (cbi.makeBookingFromEntry(event.getEntry()) != null) { //concurrency happens here
//                        System.out.println("booking from event made. showing entry");
//                        AppValues.getInstance().calendarView.showEntry(event.getEntry());
//
////                        Interval oldInterval = event.getOldInterval();
////                        Interval newInterval = event.getEntry().getInterval();
////                        System.out.println("update booking time on entry changed is false");
////                        event.getEntry().setInterval(oldInterval);
//////                        //TODO: show dialog box that says event can't be updated
//////                        //TODO: make Calendar not freak out on drag
//                    } else {
//                        event.getEntry().removeFromCalendar();
////                        oldEntry.setCalendar(event.getEntry().getCalendar());
////                        SOMEHOW DELETE OLD ENTRY HERE
//
//                        System.out.println("Hello petra");
//                        try {
//                            //event.getEntry().getCalendar().removeEntry(event.getEntry());
//                            //CustomEntry.setNull(event.getEntry());
//                            //oldEntry.getCalendar().removeEntry(event.getEntry());
//                            //AppValues.getInstance().defaultBookings.removeEntry(event.getEntry());
//                            System.out.println("Hello Petra PT 2");
//
//                            if (event.getCalendar() != null && !event.getCalendar().isReadOnly()) {
//                                //event.getEntry().removeFromCalendar();
//                                fuckThis = true;
//                                iter++;
//                                Booking b = cbi.findBookingOfEntry(oldEntry);
//                                if (b!=null){
//                                    System.out.println("booking of old entry not null");
//                                    AppValues.getInstance().cbi.removeBooking(b);
//
//                                } else {
//                                    System.out.println("booking of old entry is null");
//                                }
//                            }
//
//                            //CustomEntry.setStyle(event.getEntry());
//
//                        } catch (NullPointerException e) {
//                            System.out.println("Catching null pointer: Remove from Calendar");
//                        }
//
//                        try {
//                            //AppValues.getInstance().calendarView.showEntry(oldEntry);
//                        } catch (NullPointerException e) {
//                            System.out.println("Hello Petra Numero Tres");
//                        }
//
//
//                    }
//                    System.out.println("printing list of all bookings at the moment");
//                    for (Booking b : BookingDB.getAllBookingDB()){
//                        System.out.println(b.getBookingID() + ": " + b.getRoom().getName() + " start: " + b.getScheduledTimeStart().getTime() + " end: " + b.getScheduledTimeEnd().getTime());
//                    }

            } else if (event.getEventType().equals(CalendarEvent.ENTRY_LOCATION_CHANGED)) {
//                    cbi.updateBookingLocationOnEntryChanged(event.getEntry());
                return;
            } else if (event.getEventType().equals(CalendarEvent.ENTRY_FULL_DAY_CHANGED)) {
//                    cbi.updateBookingFullDay(event.getEntry());
                return;
            } else if (event.getEventType().equals(CalendarEvent.ENTRY_RECURRENCE_RULE_CHANGED)) {
//                    cbi.updateBookingRecurrence();
                return;
            }

//            if(fuckThis && iter == 1) {
//
//                event.getEntry().removeFromCalendar();
//
//                System.out.println("FUCK THIS");
//            }
           // AppValues.getInstance().calendarView.refreshData();
            System.out.println(event.getEventType());
            return;

        }

    };


    //what happens when user drags to change time of event
    public void onEntryDrag(CalendarView calendarView){
        //calendarView.drag
    }




}
