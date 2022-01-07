package UIControllers.ServicesControllers;

import Databases.EmployeeDB;
import Databases.NodeDB;
import Employee.Employee;
import Graph.Node;
import RequestFacade.Request;
import RequestFacade.RequestMaker;
import UIControllers.MainScreenController;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Values.AppValues;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static java.util.Calendar.*;

public class InternalTransportationController implements Initializable {

    @FXML JFXTextField tfFirstName;
    @FXML JFXTextField tfLastName;
    @FXML JFXComboBox<String> cbTransportation;
    @FXML JFXComboBox<String> cbLocation;
    @FXML JFXDatePicker datePicker;
    @FXML JFXTimePicker startTimePicker;
    @FXML JFXTimePicker endTimePicker;
    @FXML JFXButton btnReqTransportation;

    String firstName;
    String lastName;
    String transportation;
    Node location;

    int year;
    int month;
    int day;
    Calendar startTime;
    Calendar endTime;

    LinkedList<Employee> allEmps;
    LinkedList<Node> allNodes;
    HashMap<String, Node> hmNodes;
    ObservableList<String> transportationOptions = FXCollections.observableArrayList();
    ObservableList<String> locationNames = FXCollections.observableArrayList();

    @FXML
    MainScreenController mainScreenController;

    public InternalTransportationController() {

    }

    public void setMainScreenController(MainScreenController mc) { this.mainScreenController = mc; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transportationOptions.add("Wheelchair");
        transportationOptions.add("Shuttle");
        transportationOptions.add("Bed with wheels");
        cbTransportation.setItems(transportationOptions);

        allEmps = EmployeeDB.getDBEmployees();
        allNodes = NodeDB.getDBNodes();
        hmNodes = NodeDB.getNodeHashMap();

        for (Node n: allNodes){
            locationNames.add(n.getLongName());
        }
        cbLocation.setItems(locationNames);

    }

    @FXML
    public void tfFirstNameOnAction(){
        firstName = tfFirstName.getText();
    }

    @FXML
    public void tfLastNameOnAction(){
        lastName = tfLastName.getText();
    }

    @FXML
    public void cbTransportationOnAction(){
        transportation = cbTransportation.getValue();

    }

    @FXML
    public void cbLocationOnAction(){
        location = Node.getNodeFromLongName(hmNodes, cbLocation.getValue());
    }

    @FXML
    public void datePickerOnAction(){
        year = datePicker.getValue().getYear();
        month = datePicker.getValue().getMonthValue();
        day = datePicker.getValue().getDayOfMonth();
    }

    @FXML
    public void startTimePickerOnAction(){
        startTime = new Calendar.Builder().setFields(YEAR, year, MONTH, month, DAY_OF_MONTH, day, HOUR, startTimePicker.getValue().getHour(), MINUTE, startTimePicker.getValue().getHour()).build();
    }

    @FXML
    public void endTimePickerOnAction(){
        endTime = new Calendar.Builder().setFields(YEAR, year, MONTH, month, DAY_OF_MONTH, day, HOUR, endTimePicker.getValue().getHour(), MINUTE, endTimePicker.getValue().getHour()).build();
    }

    @FXML
    public void btnReqTransportationOnAction() throws IOException, MessagingException {
        RequestMaker rm = new RequestMaker();
        LinkedList<String> specs = new LinkedList<>();
        specs.add(transportation);
        if (startTime!= null){
//            System.out.println("start time exists");
        } else {
//            System.out.println("start time is null");
        }

        if (endTime!= null){
//            System.out.println("end time exists");
        } else {
//            System.out.println("end time is null");
        }

        Request r = rm.makeSafeIntTranReq(allEmps, AppValues.getInstance().curEmp, "TINT", specs, "n/a", mainScreenController.getCurNode(), startTime, endTime);
        btnReqTransportation.setDisable(true);
        if (r==null){
            btnReqTransportation.setText("Request could not be made.");
        } else {
            btnReqTransportation.setText("Request successfully made!");
        }
        btnReqTransportation.setDisable(false);
    }
}
