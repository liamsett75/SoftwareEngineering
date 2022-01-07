package UIControllers;

import Databases.BookingDB;
import Databases.EmployeeDB;
import Databases.NodeDB;
import Databases.RoomDB;
import Employee.Employee;
import FileReaders.NodeReader;
import FuzzySearch.AutoCompleteComboBoxListener;
import Graph.Node;
import PathFinding.AStar;
import PathFinding.Context;
import PathFinding.Strategy;
import RequestFacade.IScheduler;
import RequestFacade.Request;
import RoomBooking.Booking;
import RoomBooking.BookingMaker;
import RoomBooking.CalendarBookingIntegrator;
import RoomBooking.Room;
import Calendar.CalendarBehaviour;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import Values.AppValues;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Calendar.*;


//import static UIControllers.SanitationController.curEmployeeIDD;

public class RoomsController implements Initializable {
    @FXML
    JFXDatePicker checkInDate;
    @FXML
    JFXDatePicker checkOutDate;
    @FXML
    JFXTimePicker checkInTime;
    @FXML
    JFXTimePicker checkOutTime;
    @FXML
    ComboBox<String> cbRoom;
    @FXML
    JFXButton btnReserve;
    @FXML
    Label label;
    @FXML
    JFXTextField roomDescription;
    @FXML
    JFXButton btnSeeAvailability;
    @FXML
    JFXButton btnCalendar;
    @FXML
    VBox detailsBox;

    @FXML
    AnchorPane apCalendar;
    @FXML
    BorderPane bpCalendar;
    @FXML
    AnchorPane apRoomBackground;


    static Polygon CLASS1;
    static Polygon CLASS2;
    static Polygon CLASS3;
    static Polygon CLASS4;
    static Polygon CLASS5;
    static Polygon CLASS6;
    static Polygon CLASS7;
    static Polygon CLASS8;
    static Polygon CONF;
    static Polygon AUDI;
    static Polygon WZ1;
    static Polygon WZ2;
    static Polygon WZ3;
    static Polygon WZ4;
    static Polygon WZ5;

    HashMap<String, Node> allNodesHM = getAllNodes();
    private static LinkedList<Node> nodeList;
    //HashMap<Room,Node> allRooms = new HashMap<>();
    LinkedList<Room> allRoomsList = new LinkedList<>();

    BookingMaker bm = AppValues.getInstance().bm;
    Room r = new Room(null);

    String description = null;
    private static ObservableList<String> bookableRoomNames = FXCollections.observableArrayList();

    java.util.Calendar startDateTime;
    java.util.Calendar endDateTime;

    boolean viewingCal = false;

    boolean startDateSel = false;
    boolean endDateSel = false;
    boolean startTimeSel = false;
    boolean endTimeSel = false;


    //-----------------------------------------------------------------here petra
    CalendarBookingIntegrator cbi;
    LinkedList<Request> allRequests = Request.getAllRequests();
    LinkedList<Employee> allEmps;
    Employee curEmp;

    HashMap<String, Polygon> roomHM = new HashMap<>();
    HashMap<String, Polygon> wzHM = new HashMap<>();


    @FXML
    MainScreenController mainScreenController;

    public RoomsController() {

    }

    public void setMainScreenController(MainScreenController mc) {
        this.mainScreenController = mc;
    }

    /*Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            displayWorkZones();
            System.out.println("this is called every 5 seconds");
        }
    }));*/


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        cbRoom.setDisable(true);
        btnReserve.setDisable(true);
        btnSeeAvailability.setDisable(true);
        nodeList = AppValues.getInstance().allNodesList;
        allNodesHM = NodeDB.getNodeHashMap();
        allRoomsList = RoomDB.getDBRooms();

        setPolygons();
        roomHM.put("ZCLAS00401", CLASS1);
        roomHM.put("ZCLAS00402", CLASS2);
        roomHM.put("ZCLAS00403", CLASS3);
        roomHM.put("ZCLAS00404", CLASS4);
        roomHM.put("ZCLAS00405", CLASS5);
        roomHM.put("ZCLAS00406", CLASS6);
        roomHM.put("ZCLAS00407", CLASS7);
        roomHM.put("ZCLAS00408", CLASS8);
        roomHM.put("ZCONF00401", CONF);
        roomHM.put("ZAUDI00401", AUDI);

        wzHM.put("ZWKZN00401", WZ1);
        wzHM.put("ZWKZN00402", WZ2);
        wzHM.put("ZWKZN00403", WZ3);
        wzHM.put("ZWKZN00404", WZ4);
        wzHM.put("ZWKZN00405", WZ5);


        //------------------------------------------------------------------------here Petra
        calendarPaneZeroWidth();
        cbi = AppValues.getInstance().cbi;
        //making initial Calendar things
        CalendarView calendarView = AppValues.getInstance().calendarView;
        DialogBoxController dbc = new DialogBoxController();

        //        SwingUtilities.invokeLater(() -> initSwingComponents());


        //-----------------------------------------------------------------------------

        checkInDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                startDateSel = true;
                checkOutDate.setValue(checkInDate.getValue());
                enableBtnAvailability();
            }
        });

        checkOutDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                endDateSel = true;
                enableBtnAvailability();
            }
        });

        checkInTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                startTimeSel = true;
                enableBtnAvailability();
            }
        });

        checkOutTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                endTimeSel = true;
                enableBtnAvailability();
            }
        });

        cbRoom.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                label.setText("Click 'Book' to Complete Request");
                btnReserve.setDisable(false);
                enableBtnAvailability();
            }
        });

        // fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        //fiveSecondsWonder.play();

        //timeline.play();

    }


    void enableBtnAvailability() {
        //update rooms
        if (startDateSel && endDateSel && startTimeSel && endTimeSel) {
            label.setText("Check Room Availability");
            btnSeeAvailability.setDisable(false);
            btnReserve.setText("Book");
        } else { //error handling if the book button somehow gets stuck on path find (it shouldn't)
            btnReserve.setText("Book");
            btnReserve.setDisable(true);
        }
    }

    @FXML
    public void availabilityOnClick() {

        LocalDate bookDate = checkInDate.getValue();
        LocalTime startTime = checkInTime.getValue();
        LocalTime endTime = checkOutTime.getValue();

        //making retrieved dates and times into Calendar objects to match backend
        startDateTime = new java.util.Calendar.Builder().setFields(YEAR, bookDate.getYear(), MONTH, bookDate.getMonthValue() - 1, DAY_OF_MONTH, bookDate.getDayOfMonth(), HOUR_OF_DAY, startTime.getHour(), MINUTE, startTime.getMinute()).build();
        endDateTime = new java.util.Calendar.Builder().setFields(YEAR, bookDate.getYear(), MONTH, bookDate.getMonthValue() - 1, DAY_OF_MONTH, bookDate.getDayOfMonth(), HOUR_OF_DAY, endTime.getHour(), MINUTE, endTime.getMinute()).build();

        updateRooms();
        populateChoiceBox();
        displayRoomNodes();

        if (!IScheduler.isValidDate(startDateTime, endDateTime)) {
            cbRoom.setDisable(true);
            label.setText("Invalid Times Entered");
            btnReserve.setDisable(true);
        } else {
            cbRoom.setDisable(false);
            label.setText("Select or Click Desired Room");
            btnReserve.setDisable(true); //gets changed by the listener on cbRoom
        }


//        btnReserve.setDisable(false);
    }

    void populateChoiceBox() {
        List<String> nameList = new ArrayList<>();
        for (Room r : allRoomsList) {
            if (!Room.isBooked(r, startDateTime, endDateTime)) {
                nameList.add(r.getName());
            }
        }

        Collections.sort(nameList);
        cbRoom.getItems().clear();
        cbRoom.getItems().addAll(nameList);
        AutoCompleteComboBoxListener bx = new AutoCompleteComboBoxListener(cbRoom);
    }


    @FXML
    void reserveOnClick() {
        //AppValues.getInstance().curLocation = cbRoom.getSelectionModel().getSelectedItem();
        Booking isRequested = null;
        allEmps = EmployeeDB.getDBEmployees();
        Room room;


        if (btnReserve.getText().equals("Book")) {

            String location = cbRoom.getSelectionModel().getSelectedItem();
           // Entry e = AppValues.getInstance().curEntry;

            LocalDate ciDate = checkInDate.getValue();
            LocalDate coDate = checkOutDate.getValue();
            LocalTime startTime = checkInTime.getValue();
            LocalTime endTime = checkOutTime.getValue();

//            String s = cbRoom.getSelectionModel().getSelectedItem();
//            Booking isRequested = null;
            allEmps = EmployeeDB.getDBEmployees();

            startDateTime = new java.util.Calendar.Builder().setFields(YEAR, ciDate.getYear(), MONTH, ciDate.getMonthValue() - 1, DAY_OF_MONTH, ciDate.getDayOfMonth(), HOUR_OF_DAY, startTime.getHour(), MINUTE, startTime.getMinute(), SECOND, 0).build();
            endDateTime = new java.util.Calendar.Builder().setFields(YEAR, coDate.getYear(), MONTH, coDate.getMonthValue() - 1, DAY_OF_MONTH, coDate.getDayOfMonth(), HOUR_OF_DAY, endTime.getHour(), MINUTE, endTime.getMinute(), SECOND, 0).build();
            room = RoomDB.getRoomDBFromName(location);

            LocalDateTime endtInt = LocalDateTime.ofInstant(endDateTime.toInstant(), endDateTime.getTimeZone().toZoneId()).withSecond(0).withNano(0);
            LocalDateTime startInt = LocalDateTime.ofInstant(startDateTime.toInstant(), startDateTime.getTimeZone().toZoneId()).withSecond(0).withNano(0);

            Interval finalInt = new Interval(startInt, endtInt);
            System.out.println("interval is:");
            System.out.println("start: " + startInt.toString());
            System.out.println("end: " + endtInt.toString());
            System.out.println();

            //end of interval things

            //change description of booking to contain the entry id as well
            //if booking ends up being null it doesnt matter, but if its not then make entry with that id then increment counter

           // isRequested = bm.makeSafeBooking(AppValues.getInstance().curEmp, room, "# ".concat(Integer.toString(AppValues.getInstance().entryId)).concat(" #").concat(description), startDateTime, endDateTime, false);
            isRequested = bm.makeSafeBooking(AppValues.getInstance().curEmp, room, "# ENT"+ AppValues.getInstance().entryId+" #"+description, startDateTime, endDateTime, false);

            if (isRequested != null) {
                label.setText("Room Booking Complete");
                updateRooms();
                displayRoomNodes();

                btnReserve.setDisable(true);
                cbRoom.setDisable(true);
                btnReserve.setText("Path to Room");


                //Calendar things

//                e.setLocation(location);
//                e.setTitle("Bookings for " + location);
//                e.setInterval(finalInt);
//                e.setId("ENT" + AppValues.getInstance().entryId++);
//                e.setCalendar(AppValues.getInstance().defaultBookings);

                //e.isShowing(e.getStartDate(), e.getEndDate(), e.getZoneId());
                cbi.makeEntryFromBooking(AppValues.getInstance().calendarView, AppValues.getInstance().defaultBookings, isRequested);
//            AppValues.getInstance().calendarBehaviour.newEntryBehaviour(AppValues.getInstance().calendarView,AppValues.getInstance().defaultBookings,curEmp);

                System.out.println("printing current list of bookings:");
                for (Booking b : BookingDB.getAllBookingDB()) {
                    System.out.println("id: " + b.getBookingID() + " room: " + b.getRoom().getName() + " start: " + b.getScheduledTimeStart().getTime().toString() + " end: " + b.getScheduledTimeEnd().getTime().toString());
                }
            } else {
                label.setText("Could Not Complete Request.");
            }

//        System.out.println("isRequested is: " + isRequested);
//        System.out.println();
        } else {
//            System.out.println("path find to room");
            room = RoomDB.getRoomDBFromName(cbRoom.getSelectionModel().getSelectedItem());

            pathFindToRoom(room);

        }
    }

    public void pathFindToRoom(Room roomNode) {

        Node toNode = NodeDB.getNodeDB(roomNode.getId());
        mainScreenController.directionsController.fromNode = mainScreenController.curLocationNode;
        mainScreenController.directionsController.toNode = toNode;
        ArrayList<Strategy> selectionState = mainScreenController.directionsController.algoritmsChosen; // saves the current selection
        mainScreenController.directionsController.algoritmsChosen.clear();
        mainScreenController.directionsController.algoritmsChosen.add(new AStar());
        mainScreenController.directionsController.pathFind();
        for (Context pathResult : mainScreenController.directionsController.pathResults.keySet()) {
            mainScreenController.directionsController.drawRoute(pathResult, mainScreenController.directionsController.pathResults.get(pathResult));
        }
        mainScreenController.directionsController.relocateMap();
        mainScreenController.directionsController.algoritmsChosen = selectionState;

        btnReserve.setDisable(true);
        btnReserve.setText("Book");
        label.setText("Path to " + toNode.getShortName());
        backOnClick();
    }

    @FXML
    public void backOnClick() {
        mainScreenController.displayDirectionsRoute(true);
        mainScreenController.zeroInfoWidth();
        mainScreenController.revertToMapView();
        mainScreenController.toolBar.setOpacity(1);
        mainScreenController.toolBar.setVisible(true);
        mainScreenController.floorSlider.setDisable(false);
        mainScreenController.floorLabel.setDisable(false);
        mainScreenController.changeFloorView(4);
        mainScreenController.floorSlider.setValue(4);
        double x = mainScreenController.curLocationNode.getXCoord() / 5;
        double y = mainScreenController.curLocationNode.getYCoord() / 5;
        mainScreenController.gpane.centreOn(new Point2D(x, y));
        mainScreenController.gpane.zoomTo(2, new Point2D(x, y));
        mainScreenController.changeFloorView(mainScreenController.currentFloor);
        mainScreenController.floorSlider.setValue(mainScreenController.currentFloor);
        if (mainScreenController.directionsController.pathFindingTextual) {
            mainScreenController.textualDirectionsPane.setVisible(true);
        }
        resetView();
        mainScreenController.timeline.stop();
    }

    public void resetView() {
        detailsBox.setDisable(false);
        checkInDate.setValue(null);
        checkOutDate.setValue(null);
        checkInTime.setValue(null);
        checkOutTime.setValue(null);
        startTimeSel = false;
        endTimeSel = false;
        startDateSel = false;
        endDateSel = false;
        btnSeeAvailability.setDisable(true);
        roomDescription.setText("");
        cbRoom.setValue("");
        label.setText("");
        cbRoom.setDisable(true);
        btnReserve.setDisable(true);
        btnReserve.setText("Book");
    }


    HashMap<String, Node> getAllNodes() {
        NodeReader nr = new NodeReader();
        nr.readFile("nodesv4.csv");
        return nr.getNodeHashMap();
    }


    @FXML
    public void updateChoiceBox() {

    }

//    EventHandler<CalendarEvent> handler = evt -> foo(evt);


    @FXML
    void updateRooms() { //how you update list of rooms

        for (Room r : allRoomsList) {
            if (!Room.isBooked(r, startDateTime, endDateTime)) {
//                System.out.println(r.getName());
                bookableRoomNames.add(r.getName());
            }
        }
    }

    @FXML
    boolean checkDateFilledIn() {
        checkInTime.setDisable(false);
        checkOutTime.setDisable(false);
        return checkInDate.getValue() != null;
    }

    @FXML
    boolean checkTimeIn() {
        checkInDate.setDisable(false);
        checkOutTime.setDisable(false);
        return checkInTime.getValue() != null;
    }

    @FXML
    boolean checkTimeOut() {
        checkInDate.setDisable(false);
        checkInTime.setDisable(false);
        return checkOutTime.getValue() != null;
    }

    @FXML
    String getDescription() {
        description = roomDescription.getText();
        return description;
    }

    @FXML
    public void calendarOnClick() {

        if (viewingCal) {
            calendarPaneZeroWidth();
            mainScreenController.bookingPaneWidth();
            viewingCal = false;
            resetView();
        } else {
            viewingCal = true;
            mainScreenController.bookPaneWidthCalendar();
            calendarPaneWidth();
            detailsBox.setDisable(true);
            btnSeeAvailability.setDisable(true);
            btnReserve.setDisable(true);

            //------------------------------------------------------------------------------------------here Petra
            //------------------------------------ lol thnx Brent
            CalendarView calendarView = AppValues.getInstance().calendarView;
            bpCalendar.setCenter(calendarView);

            //event handler for this event: create new event when Calendar clicked, that also calls make booking from event (calendarView.setDefaultCalendarProvider(param -> new DefaultCalendarProvider());
            //event handler for switching calendars
            //customize popover for when user double clicks on a Calendar entry (use popover content node factory)
            //add Calendar source factory to set a source for the current Calendar

            //customize day entry view by subclassing DayEntryViewSkin and overriding the createContent() method
            allEmps = EmployeeDB.getDBEmployees();
            curEmp = AppValues.getInstance().curEmp;

            //-------------------- CALENDAR STUFF ----------------------------------------------
            CalendarBehaviour calendarBehaviour = AppValues.getInstance().calendarBehaviour;
        }


        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        AppValues.getInstance().calendarView.setToday(LocalDate.now());
                        AppValues.getInstance().calendarView.setTime(LocalTime.now());
                    });
                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        System.out.println("Catching the mystical null pointer exception");
                    }
                }
            }
        };

        updateTimeThread.start();
        //---------------------------- CALENDAR STUFF ENDS HERE --------------------------------------

    }


    public void calendarPaneZeroWidth() {
        apCalendar.setMinWidth(0);
        apCalendar.setMaxWidth(0);
        apRoomBackground.setPrefWidth(300);
        apRoomBackground.setMinWidth(300);
        apRoomBackground.setMaxWidth(300);
        apCalendar.setVisible(false);
    }

    public void calendarPaneWidth() {
        apCalendar.setMaxWidth(1520);
        apCalendar.setPrefWidth(1520);
        apRoomBackground.setPrefWidth(1920);
        apRoomBackground.setMaxWidth(1920);
        apCalendar.setVisible(true);
    }

    public void displayRoomNodes() {

        mainScreenController.imgAnchor.getChildren().clear();
        System.out.println(allRoomsList);
        for (Room r : allRoomsList) {

            if (r.getFloor().equals("4")) {

                Polygon curPoly = roomHM.get(r.getId());
                curPoly.setUserData(r);

                if (!IScheduler.isValidDate(startDateTime, endDateTime)) {
                    curPoly.setFill(Color.GRAY);

                    curPoly.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            label.setText("Invalid Times Entered");
                        }
                    });
                } else if (Room.isBooked(r, startDateTime, endDateTime)) {
                    curPoly.setFill(Color.RED);
                    curPoly.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            label.setText("This Room Is Already Booked");
                            btnReserve.setDisable(true);
                        }
                    });
                } else {
                    curPoly.setFill(Color.GREEN);
                    curPoly.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {

//                            System.out.println("You clicked bookable room... " + r.getName());
                            label.setText("");
                            cbRoom.getSelectionModel().select(r.getName());
                        }
                    });
                }
                mainScreenController.imgAnchor.getChildren().add(curPoly);

            }
        }
        displayRoomLabels();
    }

    public void displayRoomLabels() {

        for (Room r : allRoomsList) {
            Label roomLabel = new Label(r.getName());
            roomLabel.setLayoutX((r.getxCoord() / 5) - 10);
            roomLabel.setLayoutY((r.getyCoord() / 5) - 4);
            roomLabel.setStyle("-fx-font-size: 4; -fx-font-weight: bold");

            mainScreenController.imgAnchor.getChildren().add(roomLabel);

        }
    }

    public void displayWorkZoneLabels() {

        for (String str : wzHM.keySet()) {
            Node tempNode = allNodesHM.get(str);
            Label wzLabel = new Label(tempNode.getLongName());
            if (str.equalsIgnoreCase("ZWKZN00401")) {
                wzLabel.setLayoutX((tempNode.getXCoord() / 5) - 30);
                wzLabel.setLayoutY((tempNode.getYCoord() / 5) - 10);
            } else if (str.equalsIgnoreCase("ZWKZN00402")) {
                wzLabel.setLayoutX((tempNode.getXCoord() / 5) - 15);
                wzLabel.setLayoutY((tempNode.getYCoord() / 5) + 5);
            } else if (str.equalsIgnoreCase("ZWKZN00404")) {
                wzLabel.setLayoutX((tempNode.getXCoord() / 5) - 17);
                wzLabel.setLayoutY((tempNode.getYCoord() / 5) - 22);
            } else if (str.equalsIgnoreCase("ZWKZN00405")) {
                wzLabel.setLayoutX((tempNode.getXCoord() / 5) - 20);
                wzLabel.setLayoutY((tempNode.getYCoord() / 5) - 5);
            } else {
                wzLabel.setLayoutX((tempNode.getXCoord() / 5) - 15);
                wzLabel.setLayoutY((tempNode.getYCoord() / 5) - 8);
            }
            wzLabel.setStyle("-fx-font-size: 7; -fx-font-weight: bold");


            mainScreenController.imgAnchor.getChildren().add(wzLabel);
        }

    }

    public void displayWorkZones() {
        mainScreenController.imgAnchor.getChildren().clear();
        mainScreenController.imgAnchor.getChildren().add(WZ1);
        mainScreenController.imgAnchor.getChildren().add(WZ2);
        mainScreenController.imgAnchor.getChildren().add(WZ3);
        mainScreenController.imgAnchor.getChildren().add(WZ4);
        mainScreenController.imgAnchor.getChildren().add(WZ5);
        WZ1.setFill(setWorkZoneColor());
        WZ2.setFill(setWorkZoneColor());
        WZ3.setFill(setWorkZoneColor());
        WZ4.setFill(setWorkZoneColor());
        WZ5.setFill(setWorkZoneColor());
    }

    public Color setWorkZoneColor() {
        int randomInt = ThreadLocalRandom.current().nextInt(0, 4);
        Color returnColor = Color.WHITE;

        switch (randomInt) {
            case 0:
                returnColor = Color.GREEN;
                break;

            case 1:
                returnColor = Color.YELLOW;
                break;

            case 2:
                returnColor = Color.ORANGE;
                break;

            case 3:
                returnColor = Color.RED;
                break;
        }

        return returnColor;
    }

    public void setPolygons() {
        //------------------polygons-------------------
        double class1[] = {1118.3856209150324, 345.6666666666667, 1140.6078431372546, 335.1111111111111,
                1147.5294117647059, 349.6013071895425, 1138.3071895424837, 354.1568627450981,
                1137.3071895424837, 352.2679738562092, 1128.5294117647059, 356.1568627450981,
                1124.3071895424837, 347.6013071895425, 1120.0849673202615, 349.2679738562092,
                1118.3856209150324, 345.6666666666667};
        CLASS1 = new Polygon(class1);
        CLASS1.setOpacity(.5);


        double class2[] = {1127.1372549019607, 388.95424836601313, 1118.2222222222226, 397.843137254902,
                1102.0000000000005, 381.95424836601313, 1110.9150326797385, 373.17647058823536,
                1127.1372549019607, 388.95424836601313};
        CLASS2 = new Polygon(class2);
        CLASS2.setOpacity(.5);


        double class3[] = {1144.2222222222226, 405.95424836601313, 1140.1111111111115, 410.62091503267976,
                1138.1111111111115, 411.62091503267976, 1135.2222222222226, 411.95424836601313,
                1132.4444444444448, 411.62091503267976, 1129.8888888888894, 410.7320261437909,
                1127.5555555555559, 409.39869281045753, 1125.7777777777783, 407.5098039215687,
                1126.666666666667, 406.0653594771242, 1118.4501672051886, 397.9278481932478,
                1127.4444444444448, 389.28758169934645, 1144.2222222222226, 405.95424836601313};
        CLASS3 = new Polygon(class3);
        CLASS3.setOpacity(.5);


        double class4[] = {1162.8627450980393, 432.2156862745099, 1172.8627450980393, 427.7712418300655,
                1164.3071895424837, 409.54901960784326, 1157.1960784313726, 412.66013071895435,
                1155.9738562091504, 410.54901960784326, 1152.0849673202615, 412.4379084967321,
                1153.0849673202615, 414.66013071895435, 1147.751633986928, 417.1045751633988,
                1162.8627450980393, 432.2156862745099};
        CLASS4 = new Polygon(class4);
        CLASS4.setOpacity(.5);


        double class5[] = {1143.751633986928, 383.6143790849675, 1159.8627450980393, 375.8366013071897,
                1168.1960784313726, 393.50326797385634, 1156.0849673202615, 399.6143790849675,
                1156.5294117647059, 401.169934640523, 1153.0849673202615, 402.72549019607857,
                1143.751633986928, 383.6143790849675};
        CLASS5 = new Polygon(class5);
        CLASS5.setOpacity(.5);


        double class6[] = {1173.1699346405233, 427.4183006535949, 1189.725490196079, 419.529411764706,
                1180.83660130719, 401.08496732026157, 1177.0588235294122, 402.75163398692825,
                1176.0588235294122, 400.86274509803934, 1173.947712418301, 401.86274509803934,
                1174.83660130719, 403.4183006535949, 1163.83660130719, 408.4183006535949,
                1173.1699346405233, 427.4183006535949};
        CLASS6 = new Polygon(class6);
        CLASS6.setOpacity(.5);


        double class7[] = {1160.018499334812, 375.51886663581274, 1172.2407215570342, 369.74108885803497,
                1180.5740548903677, 387.6299777469238, 1173.018499334812, 391.51886663581274,
                1173.6851660014788, 393.18553330247937, 1170.2407215570342, 395.0744221913683,
                1169.3518326681453, 393.2966444135905, 1168.2407215570342, 393.2966444135905,
                1160.018499334812, 375.51886663581274};
        CLASS7 = new Polygon(class7);
        CLASS7.setOpacity(.5);


        double class8[] = {1189.8169934640525, 419.562091503268, 1207.372549019608, 411.33986928104576,
                1197.5947712418301, 390.45098039215685, 1193.483660130719, 392.33986928104576,
                1194.2614379084969, 394.33986928104576, 1180.9281045751636, 400.562091503268,
                1189.8169934640525, 419.562091503268};
        CLASS8 = new Polygon(class8);
        CLASS8.setOpacity(.5);


        double conf[] = {1118.2756559093368, 345.4489620468869, 1116.437908496732, 341.37908496732024,
                1120.437908496732, 339.37908496732024, 1117.549019607843, 332.82352941176464,
                1130.8823529411764, 326.37908496732024, 1130.1045751633985, 324.71241830065355,
                1134.7712418300653, 322.4901960784313, 1140.497878131559, 334.67118426910906,
                1118.2756559093368, 345.4489620468869};
        CONF = new Polygon(conf);

        CONF.setOpacity(.5);

        double audi[] = {1101.7712418300653, 381.37908496732024, 1089.7712418300653, 369.71241830065355,
                1088.549019607843, 370.4901960784313, 1086.8823529411764, 369.04575163398687,
                1085.6601307189542, 366.9346405228758, 1084.549019607843, 364.156862745098,
                1083.8823529411764, 361.71241830065355, 1083.7712418300653, 359.04575163398687,
                1084.2156862745096, 356.2679738562091, 1084.9934640522874, 353.71241830065355,
                1086.1045751633985, 351.156862745098, 1087.8823529411764, 348.9346405228758,
                1089.549019607843, 347.37908496732024, 1090.9934640522874, 348.82352941176464,
                1105.326797385621, 342.2679738562091, 1108.9934640522874, 350.04575163398687,
                1108.1045751633985, 367.60130718954247, 1104.9934640522874, 371.04575163398687,
                1108.437908496732, 374.60130718954247, 1101.7712418300653, 381.37908496732024};
        AUDI = new Polygon(audi);
        AUDI.setOpacity(.5);


        double wz2[] = {1207.4981477100057, 411.00372940019435, 1247.6737138546293, 392.07933144790053,
                1237.9012788464777, 371.44863531958026, 1230.145378046357, 369.7423371435538,
                1222.854831294244, 354.6958895913202, 1203.465079293943, 364.3132065834695,
                1208.42885580602, 374.5509956396284, 1197.7257127018538, 379.6698901677079,
                1199.8973649258876, 384.1683126317777, 1198.0359487338587, 390.8383873198813,
                1207.4981477100057, 411.00372940019435};
        WZ2 = new Polygon(wz2);
        WZ2.setStroke(Color.BLUE);
        WZ2.setStrokeWidth(1);
        WZ2.setFill(Color.LIGHTGRAY);
        WZ2.setOpacity(.5);

        double wz1[] = {1203.9071895424847, 308.4562091503268, 1191.7960784313736, 313.90065359477126,
                1195.0183006535958, 321.7895424836601, 1170.4627450980402, 332.5673202614379,
                1179.240522875818, 352.90065359477126, 1172.0183006535958, 355.90065359477126,
                1175.0183006535958, 362.7895424836601, 1179.9071895424847, 366.1228758169934,
                1183.7960784313736, 374.1228758169934, 1188.0183006535958, 371.5673202614379, 1193.0222222222224, 381.6,
                1208.42885580602, 374.5509956396284, 1203.465079293943, 364.3132065834695,
                1222.854831294244, 354.6958895913202, 1224.7960784313736, 358.5673202614379,
                1235.6849673202623, 353.23398692810457, 1230.4627450980402, 342.01176470588234,
                1228.6849673202623, 337.1228758169934, 1216.240522875818, 311.34509803921566,
                1207.7960784313736, 315.1228758169934, 1203.9071895424847, 308.4562091503268};
        WZ1 = new Polygon(wz1);
        WZ1.setStroke(Color.BLUE);
        WZ1.setStrokeWidth(1);
        WZ1.setFill(Color.LIGHTGRAY);
        WZ1.setOpacity(.5);

        double wz3[] = {1247.6737138546293, 392.07933144790053, 1237.9012788464777, 371.44863531958026,
                1230.145378046357, 369.7423371435538, 1224.7960784313736, 358.5673202614379,
                1235.6849673202623, 353.23398692810457, 1234.1521259824494, 349.99201519261004,
                1241.5592154950127, 346.1690012506419, 1237.855670738731, 338.52297336670557,
                1268.3203130887898, 325.02295538413046, 1290.302643255107, 371.73540698755403,
                1247.6737138546293, 392.07933144790053};
        WZ3 = new Polygon(wz3);
        WZ3.setStroke(Color.BLUE);
        WZ3.setStrokeWidth(1);
        WZ3.setFill(Color.LIGHTGRAY);
        WZ3.setOpacity(.5);

        double wz4[] = {1249.9555555555555, 286.5111111111111, 1268.3203130887898, 325.02295538413046,
                1230.4627450980402, 342.01176470588234, 1228.6849673202623, 337.1228758169934,
                1216.240522875818, 311.34509803921566, 1223.7333333333333, 308.17777777777775,
                1220.288888888889, 300.4, 1249.9555555555555, 286.5111111111111};
        WZ4 = new Polygon(wz4);
        WZ4.setStroke(Color.BLUE);
        WZ4.setStrokeWidth(1);
        WZ4.setFill(Color.LIGHTGRAY);
        WZ4.setOpacity(.5);

        double wz5[] = {1172.1999999999998, 273.17777777777775, 1191.7960784313736, 313.90065359477126,
                1195.0183006535958, 321.7895424836601, 1170.4627450980402, 332.5673202614379,
                1172.6444444444444, 338.17777777777775, 1167.9777777777776, 340.4,
                1165.7555555555555, 335.5111111111111, 1145.7555555555555, 344.73333333333335,
                1135.1999999999998, 322.17777777777775, 1129.9777777777776, 319.17777777777775,
                1119.8666666666666, 298.06666666666666, 1172.1999999999998, 273.17777777777775};
        WZ5 = new Polygon(wz5);
        WZ5.setStroke(Color.BLUE);
        WZ5.setStrokeWidth(1);
        WZ5.setFill(Color.LIGHTGRAY);
        WZ5.setOpacity(.5);
    }

    /*
    class Random extends TimerTask
    {
        public void run()
        {
            System.out.println("Hello world");
        }

    }

    Timer timer = new Timer();
    timer.schedule(new Random(), 0,6000);

*/

}
//            icAgenda.newAppointmentCallbackProperty().set( (localDateTimeRange) -> {
////                System.out.println("making new appointment");
//                Agenda.Appointment a =  new Agenda.AppointmentImplLocal()
//                       // .withLocation(a.getLocation())
//                        .withStartLocalDateTime(localDateTimeRange.getStartLocalDateTime())
//                        .withEndLocalDateTime(localDateTimeRange.getEndLocalDateTime());
////                System.out.println("a start time is: " + a.getStartLocalDateTime().toString());
////                System.out.println("a end time is: " + a.getEndLocalDateTime().toString());
//
//                cbi.addBookingFromAppointment(a, MainScreenController.getCurEmployee(), a.getLocation(), bm);
//
//                return a;
//
//            });