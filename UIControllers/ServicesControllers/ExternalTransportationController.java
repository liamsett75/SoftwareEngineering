package UIControllers.ServicesControllers;

import Databases.EmployeeDB;
import Employee.Employee;
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
import java.util.LinkedList;
import java.util.ResourceBundle;

import static java.util.Calendar.*;

public class ExternalTransportationController implements Initializable {

    @FXML JFXTextField tfFirstName;
    @FXML JFXTextField tfLastName;
    @FXML JFXComboBox<String> cbTransportationMode;
    @FXML JFXTextField tfLocation;
    @FXML JFXDatePicker datePicker;
    @FXML JFXTimePicker startTimePicker;
    @FXML JFXTimePicker endTimePicker;
    @FXML JFXButton btnReqService;

    String firstName;
    String lastName;
    String transportation;
    String location;

    int year;
    int month;
    int day;
    Calendar startTime;
    Calendar endTime;

    LinkedList<Employee> allEmps;
    ObservableList<String> transportationOptions = FXCollections.observableArrayList();

    @FXML
    MainScreenController mainScreenController;

    public ExternalTransportationController() {

    }

    public void setMainScreenController(MainScreenController mc) { this.mainScreenController = mc; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transportationOptions.add("Ambulance");
        transportationOptions.add("Shuttle Bus");
        transportationOptions.add("Private Car");
        transportationOptions.add("Helicopter");
        cbTransportationMode.setItems(transportationOptions);

        allEmps = EmployeeDB.getDBEmployees();
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
    public void cbTransportationModeOnAction(){
        transportation = cbTransportationMode.getValue();
    }

    @FXML
    public void tfLocationOnAction(){
        location = tfLocation.getText();
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
    public void btnReqServiceOnAction() throws IOException, MessagingException {
        RequestMaker rm = new RequestMaker();
        LinkedList<String> specs = new LinkedList<>();
        specs.add(transportation);
        specs.add(location);
        Request r = rm.makeSafeExtTranReq(allEmps, AppValues.getInstance().curEmp, "TEXT", specs, "n/a", mainScreenController.getCurNode(), startTime, endTime);
        btnReqService.setDisable(true);
        if (r==null){
            btnReqService.setText("Request could not be made.");
        } else {
            btnReqService.setText("Request successfully made.");
        }
        btnReqService.setDisable(false);
    }
}
