package UIControllers.ServicesControllers;

import Databases.EmployeeDB;
import Employee.Employee;
import RequestFacade.Request;
import RequestFacade.RequestMaker;
import UIControllers.MainScreenController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import Values.AppValues;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static java.util.Calendar.*;

public class AudioVisualController implements Initializable {
    @FXML public JFXTextField tfDeviceName;
    @FXML public JFXTextField tfDeviceBrand;
    @FXML public JFXTextField tfDeviceType;
    @FXML public JFXTimePicker startTimePicker;
    @FXML public JFXTimePicker endTimePicker;
    @FXML public JFXButton btnLaunchRequest;
    @FXML public JFXDatePicker datePicker;
    @FXML public Label lbl;
    // specifications
    LinkedList<String> deviceSpecifications = new LinkedList<>();

    // dates
    private int year;
    private int month;
    private int day;
    // start time
    private int startHour;
    private int startMinute;
    // end time
    private int endHour;
    private int endMinute;

    private static LinkedList<Employee> allEmps = new LinkedList<Employee>();

    @FXML
    MainScreenController mainScreenController;

    public AudioVisualController(){}

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLaunchRequest.setDisable(false);
        lbl.setVisible(false);
    }

    @FXML
    public void enable() {
        if(tfDeviceName.getText().isEmpty()==false&&
        tfDeviceType.getText().isEmpty()==false&&
        tfDeviceBrand.getText().isEmpty()==false&&
        startTimePicker.getValue()!=null&&
        endTimePicker.getValue()!=null&&
        datePicker.getValue()!=null){
            btnLaunchRequest.setDisable(false);
        }
    }

    /**
     * Just reads in everything
     * @param actionEvent
     */
    @FXML
    public void launchRequestOnClick(ActionEvent actionEvent) throws GeneralSecurityException, IOException, MessagingException {


        tfDeviceBrand.getText();
        deviceSpecifications.add(tfDeviceName.getText());
        deviceSpecifications.add(tfDeviceType.getText());
        deviceSpecifications.add(tfDeviceBrand.getText());

        year = datePicker.getValue().getYear();
        month = datePicker.getValue().getMonthValue();
        day = datePicker.getValue().getDayOfMonth();

        // start Date
        startHour = startTimePicker.getValue().getHour();
        startMinute = startTimePicker.getValue().getMinute();
        Calendar startDate = new Calendar.Builder().setFields(YEAR, year,
                MONTH, month , DAY_OF_MONTH, day, HOUR, startHour,
                MINUTE, startMinute).build();
        // end Date
        endHour = endTimePicker.getValue().getHour();
        endMinute = endTimePicker.getValue().getMinute();
        Calendar endDate = new Calendar.Builder().setFields(YEAR, year,
                MONTH, month, DAY_OF_MONTH, day, HOUR, endHour,
                MINUTE, endMinute).build();

        allEmps = EmployeeDB.getDBEmployees();
        RequestMaker rm = new RequestMaker();
        Request request = rm.makeSafeAVReq(allEmps, AppValues.getInstance().curEmp, "AV", deviceSpecifications, "n/a", mainScreenController.getCurNode(), startDate, endDate);

/*
        System.out.println(request);
        System.out.println(year);
        System.out.println(month);
        System.out.println(day);
        System.out.println(startHour);
        System.out.println(endHour);
        System.out.println(startMinute);
        System.out.println(endMinute);
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(allEmps);
        System.out.println(deviceSpecifications);
        System.out.println(AppValues.getInstance().curEmp);
*/
        if(request!=null){
            lbl.setVisible(true);
            tfDeviceBrand.clear();
            tfDeviceType.clear();
            tfDeviceName.clear();
            startTimePicker.getEditor().clear();
            endTimePicker.getEditor().clear();
            datePicker.getEditor().clear();
        }
        else {
            lbl.setVisible(true);
            lbl.setText("No Employees ;(");
        }
    }

    @FXML
    public void startTimePickerOnAction(ActionEvent actionEvent) {

    }

    @FXML
    public void datePickerOnClick(ActionEvent actionEvent) {
    }

    @FXML
    public void endTimePickerOnAction(ActionEvent actionEvent) {
    }
}
