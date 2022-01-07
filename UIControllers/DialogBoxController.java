package UIControllers;

import Databases.BookingDB;
import Databases.NodeDB;
import Databases.RoomDB;
import FuzzySearch.AutoCompleteComboBoxListener;
import Graph.Node;
import RequestFacade.IScheduler;
import RoomBooking.Booking;
import RoomBooking.BookingMaker;
import RoomBooking.CalendarBookingIntegrator;
import RoomBooking.Room;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Values.AppValues;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.util.Calendar.*;

public class DialogBoxController implements Initializable {
    @FXML
    JFXDatePicker startDatePicker;
    @FXML
    JFXDatePicker endDatePicker;
    @FXML
    JFXTimePicker startTimePicker;
    @FXML
    JFXTimePicker endTimePicker;
    @FXML
    JFXComboBox cbSelectRoom;
    @FXML
    JFXButton btnBook;
    @FXML
    JFXButton btnCancel;
    LinkedList<Node> nodeList;
    LinkedList<Room> allRoomsList;
    HashMap<String, Node> allNodesHM;
    CalendarBookingIntegrator cbi;
    CalendarView calendarView;
    boolean startDateSel = false;
    boolean endDateSel = false;
    boolean startTimeSel = false;
    boolean endTimeSel = false;
    java.util.Calendar startDateTime;
    java.util.Calendar endDateTime;
    BookingMaker bm;
    String s;
    Room room;
    String description = null;
    LocalDateTime startInt;
    LocalDateTime endtInt;
    Interval finalInt;

    private static ObservableList<String> bookableRoomNames = FXCollections.observableArrayList();


    public void initialize(URL Location, ResourceBundle resources) {
        bm = AppValues.getInstance().bm;
        btnBook.setDisable(true);
        nodeList = AppValues.getInstance().allNodesList;
        allNodesHM = NodeDB.getNodeHashMap();
        allRoomsList = RoomDB.getDBRooms();
        cbi = AppValues.getInstance().cbi;
        calendarView = AppValues.getInstance().calendarView;
        startInt = AppValues.getInstance().startInt;
        endtInt = AppValues.getInstance().endtInt;
        finalInt = new Interval();
        //cbSelectRoom.toFront();


//        initFX();
        //SwingUtilities.invokeLater(() -> initSwingComponents());

        startDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                System.out.println("start date changed");
                startDateSel = true;
                endDatePicker.setValue(startDatePicker.getValue());
                enableBtnBook();
                enableCbSelectRoom();
            }
        });

        endDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                System.out.println("end date changed");
                endDateSel = true;
                enableBtnBook();
                enableCbSelectRoom();
            }
        });

        startTimePicker.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                System.out.println("start time changed");
                startTimeSel = true;
                enableBtnBook();
                enableCbSelectRoom();

            }
        });

        endTimePicker.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                System.out.println("end time changed");
                endTimeSel = true;
                enableBtnBook();
                enableCbSelectRoom();
            }
        });

        cbSelectRoom.valueProperty().addListener(new ChangeListener<String>() {
            @Override

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                AppValues.getInstance().curInterval = new Interval(AppValues.getInstance().startInt, AppValues.getInstance().endtInt);
                //s = cbSelectRoom.getSelectionModel().getSelectedItem().toString().trim();
                System.out.println("room has been selected");
                btnBook.setDisable(false);
            }
        });


//        btnReserve.setDisable(false);


        //newEntryBehaviour(AppValues.getInstance().calendarView, AppValues.getInstance().defaultBookings, AppValues.getInstance().curEmp);
    }

//    void enableCbSelectRoom() {
//        //update rooms
//        if (startDateSel && endDateSel && startTimeSel && endTimeSel) {
//            updateRooms();
//            populateChoiceBox();
//        }
//    }
//
//
//    void enableBtnBook() {
//        //update rooms
//        if (startDateSel && endDateSel && startTimeSel && endTimeSel) {
//            btnBook.setDisable(false);
//        }
//    }

    @FXML
    public void availabilityOnClick() {
        System.out.println("avail on click called");

        LocalDate bookDate = startDatePicker.getValue();
        LocalTime startTime = startTimePicker.getValue();
        LocalTime endTime = endTimePicker.getValue();

        //making retrieved dates and times into Calendar objects to match backend
        startDateTime = new java.util.Calendar.Builder().setFields(YEAR, bookDate.getYear(), MONTH, bookDate.getMonthValue() - 1, DAY_OF_MONTH, bookDate.getDayOfMonth(), HOUR_OF_DAY, startTime.getHour(), MINUTE, startTime.getMinute(), SECOND, 0).build();
        endDateTime = new java.util.Calendar.Builder().setFields(YEAR, bookDate.getYear(), MONTH, bookDate.getMonthValue() - 1, DAY_OF_MONTH, bookDate.getDayOfMonth(), HOUR_OF_DAY, endTime.getHour(), MINUTE, endTime.getMinute(), SECOND, 0).build();

        endtInt = LocalDateTime.ofInstant(endDateTime.toInstant(), endDateTime.getTimeZone().toZoneId()).withSecond(0).withNano(0);
        startInt = LocalDateTime.ofInstant(startDateTime.toInstant(), startDateTime.getTimeZone().toZoneId()).withSecond(0).withNano(0);
        //AppValues.getInstance().curInterval = new Interval(startInt, endtInt);
        finalInt = new Interval(startInt, endtInt);
        System.out.println("interval is:");
        System.out.println("start: " + startInt.toString());
        System.out.println("end: " + endtInt.toString());
        System.out.println();
        updateRooms();
        populateChoiceBox();

        if (!IScheduler.isValidDate(startDateTime, endDateTime)) {
            cbSelectRoom.setDisable(true);
            btnBook.setDisable(true);
        } else {
            cbSelectRoom.setDisable(false);
//            label.setText("Select Room then click Book");
            btnBook.setDisable(true); //gets changed by the listener on cbRoom
        }
    }


    void populateChoiceBox() {
        List<String> nameList = new ArrayList<>();
        for (Room r : allRoomsList) {
            if (!Room.isBooked(r, startDateTime, endDateTime)) {
                //adding times to appvalues
                nameList.add(r.getName());
            }
        }

        Collections.sort(nameList);
        cbSelectRoom.getItems().clear();
        cbSelectRoom.getItems().addAll(nameList);
        AutoCompleteComboBoxListener bx = new AutoCompleteComboBoxListener(cbSelectRoom);
    }


    @FXML
    Entry bookOnClick() {
        //------------HERE PETRA------------------
        //ADD PROPER DESCRIPTIONS TO BOOKINGS, ADD PROPER ID TO ENTRIES,
        // ADD A SINGLETON METHOD TO ITERATE THROUGH NUMBERS FOR GENERATING ENTRY IDS
        //-------------HERE PETRA-----------------

        System.out.println("book on click called");

        System.out.println("time things are happening");

        LocalDate bookDate = startDatePicker.getValue();
        LocalTime startTime = startTimePicker.getValue();
        LocalTime endTime = endTimePicker.getValue();

        startDateTime = new java.util.Calendar.Builder().setFields(YEAR, bookDate.getYear(), MONTH, bookDate.getMonthValue() - 1, DAY_OF_MONTH, bookDate.getDayOfMonth(), HOUR_OF_DAY, startTime.getHour(), MINUTE, startTime.getMinute(), SECOND, 0).build();
        endDateTime = new java.util.Calendar.Builder().setFields(YEAR, bookDate.getYear(), MONTH, bookDate.getMonthValue() - 1, DAY_OF_MONTH, bookDate.getDayOfMonth(), HOUR_OF_DAY, endTime.getHour(), MINUTE, endTime.getMinute(), SECOND, 0).build();

        endtInt = LocalDateTime.ofInstant(endDateTime.toInstant(), endDateTime.getTimeZone().toZoneId()).withSecond(0).withNano(0);
        startInt = LocalDateTime.ofInstant(startDateTime.toInstant(), startDateTime.getTimeZone().toZoneId()).withSecond(0).withNano(0);
        //AppValues.getInstance().curInterval = new Interval(startInt, endtInt);
        finalInt = new Interval(startInt, endtInt);
        System.out.println("interval is:");
        System.out.println("start: " + startInt.toString());
        System.out.println("end: " + endtInt.toString());
        System.out.println();
        updateRooms();

        System.out.println("time things stopped happening");

        s = cbSelectRoom.getSelectionModel().getSelectedItem().toString().trim();
        room = RoomDB.getRoomDBFromName(s);

        //newEntryBehaviour(AppValues.getInstance().calendarView, AppValues.getInstance().defaultBookings, AppValues.getInstance().curEmp, true);
        updateRooms();
        btnBook.setDisable(true);
        cbSelectRoom.setDisable(true);

        Entry e = AppValues.getInstance().curEntry;

        System.out.println("ok pressed");
        e.setLocation(s);
        e.setTitle("Booking for " + s); //same here
        e.setInterval(finalInt);
        e.setId("ENT"+AppValues.getInstance().entryId);


        //checking if time is valid and no other bookings overlap
        java.util.Calendar timeStart = AppValues.getInstance().cbi.calendarDatesFromEntry(e).getFirst();
        java.util.Calendar timeEnd = AppValues.getInstance().cbi.calendarDatesFromEntry(e).getLast();
        //if (Booking.isValidBooking(RoomDB.getRoomDBFromName(e.getLocation()), timeStart, timeEnd) == true) {

            Booking tempB = AppValues.getInstance().bm.makeSafeBooking(AppValues.getInstance().curEmp, RoomDB.getRoomDBFromName(e.getLocation()), "# "+e.getId()+" # Auto-Generated Booking for "+e.getLocation(), timeStart, timeEnd, e.isFullDay());
            if (tempB == null) {
                System.out.println("make safe booking null");
                e = null;
            } else {
                System.out.println("vooking made");
                //AppValues.getInstance().entryId++;
            }
            System.out.println("make booking from entry not null");
            System.out.println("iterating list of bookings");
            for (Booking b : BookingDB.getAllBookingDB()) {
                System.out.println("this booking is: " + b.getBookingID() + " " + b.getRoom().getName() + " description " + b.getDescription());
            }

        //} else {
          //  System.out.println("booking couldnt be made");
         //   e = null;
       // }

        System.out.println("printing current list of bookings:");
        for (Booking b : BookingDB.getAllBookingDB()) {
            System.out.println("id: " + b.getBookingID() + " room: " + b.getRoom().getName() + " start: " + b.getScheduledTimeStart().getTime().toString() + " end: " + b.getScheduledTimeEnd().getTime().toString());
        }
        cbSelectRoom.setDisable(false);
        btnBook.setDisable(false);
        if (e!=null){
           // AppValues.getInstance().curEntry = e;
            e = cbi.makeEntryFromBooking(calendarView,AppValues.getInstance().defaultBookings,tempB);
            AppValues.getInstance().allEntries.add(e);
            //e.setCalendar(AppValues.getInstance().defaultBookings);
          //  e.isShowing(e.getStartDate(),e.getEndDate(), e.getZoneId());
        }
        //e.setCalendar(AppValues.getInstance().defaultBookings);//newEntryBehaviour(AppValues.getInstance().calendarView, AppValues.getInstance().defaultBookings,AppValues.getInstance().curEmp);
        //return e;
        return e;
    }

    void enableCbSelectRoom() {
        //update rooms
        if (startDateSel && endDateSel && startTimeSel && endTimeSel) {
            updateRooms();
            populateChoiceBox();
        }
    }


    void enableBtnBook() {
        //update rooms
        if (startDateSel && endDateSel && startTimeSel && endTimeSel) {
            btnBook.setDisable(false);
        }

    }

    @FXML
    void btnCancelOnClick() {

    }

    @FXML
    void updateRooms() { //how you update list of rooms

        for (Room r : allRoomsList) {
            if (!Room.isBooked(r, startDateTime, endDateTime)) {
//                System.out.println(r.getName());
                bookableRoomNames.add(r.getName());
            }
        }
    }

    public void resetView() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        startTimePicker.setValue(null);
        endTimePicker.setValue(null);
        startTimeSel = false;
        endTimeSel = false;
        startDateSel = false;
        endDateSel = false;
        cbSelectRoom.setValue("");

//        label.setText("");
        cbSelectRoom.setDisable(true);
        btnBook.setDisable(true);
    }

}