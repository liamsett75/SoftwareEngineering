package RoomBooking;

import com.calendarfx.model.Entry;

public class CustomEntry extends Entry {

    /*public static void setNull(Entry entry) {
        entry.setInterval((Interval) null);
        entry.setCalendar(null);
        entry.setLocation(null);
        entry.setTitle(null);
        entry.setRecurrenceRule(null);
        entry.setId(null);
        entry.setZoneId(null);
        entry.setUserObject(null);
        entry.setMinimumDuration(null);
    }*/

    public static void setStyle(Entry e) {
        e.getStyleClass().add("calendarStyles.css");

//        ("calendarStyles.css");
    }
}
