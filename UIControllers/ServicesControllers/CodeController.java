package UIControllers.ServicesControllers;

import Databases.EmployeeDB;
import Databases.NodeDB;
import Employee.Employee;
import Graph.Node;
import RequestFacade.CodeRequest;
import RequestFacade.Request;
import RequestFacade.RequestMaker;
import UIControllers.MainScreenController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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

public class CodeController implements Initializable {
    @FXML JFXComboBox<String> cbCodeTypePicker;
    @FXML JFXComboBox<String> cbLocation;
    @FXML JFXButton btnLaunchRequest;
    @FXML JFXTextArea taDescription;
    @FXML Label lbCodeDescription;

    private String description;
    private static String codeType;
    private static Node location;

    private static HashMap<String, String> hm;
    private static ObservableList<String> tableCodeList = FXCollections.observableArrayList();
    private static ObservableList<String> tableLocationList = FXCollections.observableArrayList();

    private static HashMap<String, Node> nodeHashMap = NodeDB.getNodeHashMap();

    @FXML
    MainScreenController mainScreenController;

    public CodeController(){

    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        hm = CodeRequest.getActivitiesByCode();
        tableCodeList.addAll(hm.keySet());
        cbCodeTypePicker.setItems(tableCodeList);
        //enabling the first control box
        taDescription.setDisable(false);

        //disabling all the other boxes
        cbLocation.setDisable(true);
        btnLaunchRequest.setDisable(true);
        cbCodeTypePicker.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    LinkedList<String> nodes = new LinkedList<>();

                    Collection<Node> values = nodeHashMap.values();
                    for(Node n : values){
                        nodes.add(n.getLongName());
                    }

                    Collections.sort(nodes);

                    codeType = newValue;

                    lbCodeDescription.setText(hm.get(codeType));

                    tableLocationList.addAll(nodes);
                    cbLocation.setItems(tableLocationList);
                    cbLocation.setDisable(false);
                }
        );
    }

    @FXML
    public void cbLocationsOnClick(){
        location = getNodeFromLongName(nodeHashMap,cbLocation.getValue());
        btnLaunchRequest.setDisable(false);
    }

    @FXML
    public void taDescriptionOnClick(){
        description = taDescription.getText();
    }

    @FXML
    public void launchRequestOnClick() throws IOException, MessagingException {
        //making instances of all required objects to put in constructor
        LinkedList<Employee> allEmps = EmployeeDB.getDBEmployees();
        LinkedList<String> details = new LinkedList<>();


        details.add(codeType);
        details.add(hm.get(codeType));

//        System.out.println("Code Type: " + details.get(0));
//        System.out.println("Hash Map: " + details.get(1));


        //making request
        RequestMaker rm = new RequestMaker();

        Request r = rm.makeSafeCodeReq(allEmps, AppValues.getInstance().curEmp, "COD", details, description, location);

        btnLaunchRequest.setDisable(true);
        if (r == null){
            btnLaunchRequest.setText("No available resources for this request");
        }
        else {
            btnLaunchRequest.setText("Request Made Successfully!");
        }
        btnLaunchRequest.setDisable(false);
    }
}
