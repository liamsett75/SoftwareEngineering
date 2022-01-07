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
import javafx.scene.control.Label;
import Values.AppValues;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static Graph.Node.getNodeFromLongName;
import static java.util.Calendar.*;

public class GiftShopController  implements Initializable {
    @FXML JFXTextField tfFirstName;
    @FXML JFXTextField tfLastName;
    @FXML JFXComboBox<String> cbItemPicker;
    @FXML JFXComboBox<String> cbItemCostPicker;
    @FXML JFXDatePicker datePicker;
    @FXML JFXTimePicker timePicker;
    @FXML JFXComboBox<String> cbLocation;
    @FXML JFXButton btnLaunchRequest;
    @FXML JFXTextArea taDescription;
    private static String firstName;
    private static String lastName;
    private static String itemChosen;
    private static String itemCost;

    private  Node location;

    @FXML
    private Label tfItem;
    @FXML
    private Label tfCost;


    private Calendar eventTime;

    private  String description;

    private static HashMap<String, Double> shopCatalog = new HashMap<>();
    private static ObservableList<String> tableItemList = FXCollections.observableArrayList();
    private static ObservableList<String> tableServicesList = FXCollections.observableArrayList();
    private static ObservableList<String> tableLocationList = FXCollections.observableArrayList();

    private static HashMap<String, Node> nodeHashMap = NodeDB.getNodeHashMap();

    @FXML
    MainScreenController mainScreenController;

    public GiftShopController(){

    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shopCatalog.put("Flowers", 70.00);
        shopCatalog.put("Balloons", 20.00);
        shopCatalog.put("Water Bottle", 12.95);
        shopCatalog.put("Fleece", 78.95); // Create the catalog of items

        tableItemList.addAll(shopCatalog.keySet()); // Populate the item list (things to go in the choice box) with the hashmap keys

        cbItemPicker.setItems(tableItemList); // Put the hashmap keys in the choice box

        tfFirstName.setDisable(false);
        tfLastName.setDisable(false);
        taDescription.setDisable(false);

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
        }
        cbItemPicker.setDisable(false);

    }

    @FXML public void tfLastNameOnClick(){
        if (tfLastName.getText()!= null) {
            lastName = tfLastName.getText();
        }
        cbItemPicker.setDisable(false);
    }

    @FXML
    public void setCbItemPickerOnClick(){
        itemChosen = cbItemPicker.getValue();
        tfItem.setText(itemChosen);
        tfCost.setText(Double.toString(shopCatalog.get(itemChosen)));
    }

    @FXML
    public void timePickerOnAction(){
        eventTime = new Calendar.Builder().setFields(YEAR, Calendar.getInstance().get(Calendar.YEAR), MONTH, Calendar.getInstance().get(Calendar.MONTH), DAY_OF_MONTH, Calendar.getInstance().get(DAY_OF_MONTH), HOUR, timePicker.getValue().getHour(), MINUTE, timePicker.getValue().getMinute()).build();
    }

    @FXML
    public void taDescriptionOnClick(){
        description = taDescription.getText();
        btnLaunchRequest.setDisable(false);
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

        description = taDescription.getText();

        details.add(itemChosen);
        details.add(shopCatalog.get(itemChosen).toString());

//        System.out.println("Code Type: " + details.get(0));
//        System.out.println("Hash Map: " + details.get(1));

        //making request
        RequestMaker rm = new RequestMaker();

        Request r = rm.makeSafeGifReq(allEmps, AppValues.getInstance().curEmp, "GIF", details, description, location, eventTime, null);

        if (r == null){
            btnLaunchRequest.setText("No available resources for this request");
        }
        else {
            btnLaunchRequest.setText("Request Made Successfully!");
        }
        btnLaunchRequest.setDisable(false);
    }
}

