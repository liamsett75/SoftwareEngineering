package UIControllers;

import Databases.EmployeeDB;
import Databases.NodeDB;
import Employee.Employee;
import Graph.Node;
import RequestFacade.Request;
import RequestFacade.RequestMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import Values.AppValues;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static Graph.Node.getNodeFromLongName;
import static java.util.Calendar.*;

public class FloristController  implements Initializable {
    @FXML
    JFXTextField tfFirstName;
    @FXML
    JFXTextField tfLastName;
    @FXML
    JFXComboBox<String> flowerType;
    @FXML
    JFXComboBox<String> flowerColor;
    @FXML
    JFXComboBox<String> numberOf;
    @FXML
    JFXComboBox<String> cbLocation;
    @FXML
    JFXTimePicker timePicker;
    @FXML
    JFXButton btnLaunchRequest;
    private static String firstName;
    private static String lastName;
    private static String itemChosen;
    private static Node location;

    @FXML
    private Label tfItem;
    @FXML
    private Label tfCost;

    private String number;
    private String color;
    private String type;

    private Calendar eventTime;

    private static String description;

    private static ObservableList<String> tableFlowerList = FXCollections.observableArrayList();
    private static ObservableList<String> tableColorList = FXCollections.observableArrayList();
    private static ObservableList<String> tableOfNumbers = FXCollections.observableArrayList();
    private static ObservableList<String> tableLocationList = FXCollections.observableArrayList();

    private static HashMap<String, Node> nodeHashMap = NodeDB.getNodeHashMap();

    @FXML
    MainScreenController mainScreenController;

    public FloristController() {

    }

    public void setMainScreenController(MainScreenController mc) {
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("Flower Delivery");

        tableFlowerList.add("Daisy");
        tableFlowerList.add("Rose");
        tableFlowerList.add("Tulip");
        tableFlowerList.add("Orchid");

        tableColorList.add("Red");
        tableColorList.add("Pink");
        tableColorList.add("Yellow");
        tableColorList.add("White");
        tableColorList.add("Blue");

        tableOfNumbers.add("1");
        tableOfNumbers.add("2");
        tableOfNumbers.add("6");
        tableOfNumbers.add("12");
        tableOfNumbers.add("24");

        flowerType.setItems(tableFlowerList);
        flowerColor.setItems(tableColorList);
        numberOf.setItems(tableOfNumbers);

        tfFirstName.setDisable(false);
        tfLastName.setDisable(false);

        cbLocation.setDisable(true);
        btnLaunchRequest.setDisable(true);

        LinkedList<String> nodes = new LinkedList<>();

        Collection<Node> values = nodeHashMap.values();
        for(Node n : values){
            nodes.add(n.getLongName());
        }

        Collections.sort(nodes);

        tableLocationList.addAll(nodes);

        cbLocation.setItems(tableLocationList);
        cbLocation.setDisable(false);
    }

    @FXML
    public void tfFirstNameOnClick(){
        if (tfFirstName.getText() != null) {
            firstName = tfFirstName.getText();
//            System.out.println("first name is: " + firstName);
        }
        flowerType.setDisable(false);

    }

    @FXML public void tfLastNameOnClick(){
        if (tfLastName.getText()!= null) {
            lastName = tfLastName.getText();
//            System.out.println("last name is: " + lastName);
        }
        flowerType.setDisable(false);
    }

    @FXML
    public void setFlowerTypeOnClick(){
        itemChosen = flowerType.valueProperty().getValue();
//        System.out.println("Item Chosen: " + itemChosen);
        //tfItem.setText(itemChosen);

        type = itemChosen;
    }
    @FXML
    public void setFlowerColorOnClick(){
        itemChosen = flowerColor.valueProperty().getValue();
//        System.out.println("Item Chosen: " + itemChosen);
        //tfItem.setText(itemChosen);

        color = itemChosen;
    }
    @FXML
    public void setNumberOfOnClick(){
        itemChosen = numberOf.valueProperty().getValue();
//        System.out.println("Item Chosen: " + itemChosen);

        number = itemChosen;
        //tfItem.setText(itemChosen);
    }

    @FXML
    public void timePickerOnAction(){
        eventTime = new Calendar.Builder().setFields(YEAR, Calendar.getInstance().get(Calendar.YEAR), MONTH, Calendar.getInstance().get(Calendar.MONTH), DAY_OF_MONTH, Calendar.getInstance().get(DAY_OF_MONTH), HOUR, timePicker.getValue().getHour(), MINUTE, timePicker.getValue().getMinute()).build();
    }

    @FXML
    public void cbLocationsOnClick(){
        location = getNodeFromLongName(nodeHashMap,cbLocation.getValue());
        btnLaunchRequest.setDisable(false);
    }

    @FXML
    public void launchRequestOnClick() throws IOException, MessagingException {
        //making instances of all required objects to put in constructor
        LinkedList<Employee> allEmps = EmployeeDB.getDBEmployees();
        LinkedList<String> details = new LinkedList<>();

        details.add(type);
        details.add(color);
        details.add(number);

//        System.out.println("Details: " + details);

        //making request
        RequestMaker rm = new RequestMaker();

        Request r = rm.makeSafeFlowerReq(allEmps, AppValues.getInstance().curEmp, "FLO", details, "n/a", location, eventTime, null);

        if (r == null){
            btnLaunchRequest.setText("No available resources for this request");
        }
        else {
            btnLaunchRequest.setText("Request Made Successfully!");
        }
        btnLaunchRequest.setDisable(false);
    }

    @FXML
    public void backOnClick(){
        mainScreenController.servicesHBox.setVisible(false);
        mainScreenController.imgView. requestFocus();
    }

}