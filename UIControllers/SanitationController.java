package UIControllers;

import Databases.EmployeeDB;
import Databases.NodeDB;
import Employee.Employee;
import Graph.Node;
import PathFinding.Filter;
import RequestFacade.Request;
import RequestFacade.RequestMaker;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.*;

import static Graph.Node.getNodeFromLongName;

public class SanitationController implements Initializable {

    @FXML
    ComboBox<String> cbSpec;
    @FXML
    ComboBox<String> cbLocation;
    @FXML
    Button btnRequest;
    @FXML
    CheckBox biohazardCB;
    @FXML
    JFXButton btnClose;

    private static String description;
    private static String sanTypeChosen;

    boolean locationSel = false;
    boolean specificationSel = false;

    private static Node location;
    boolean isBiohazard;

    private static ObservableList<String> tableSanitationType = FXCollections.observableArrayList(); // serviceSubCategory
    private static ObservableList<String> tableLocationList = FXCollections.observableArrayList();
    private static HashMap<String, Node> nodeHashMap = NodeDB.getNodeHashMap();
    public static HashMap<String, Node> bhNodeHM = new HashMap<>();

    public static void setBhNodeHM(HashMap<String, Node> bhNodeHM) {
        SanitationController.bhNodeHM = bhNodeHM;
    }

    public static HashMap<String, Node> getBhNodeHM() {
        return bhNodeHM;
    }
//    public LinkedList<Node> bhNodes = new LinkedList<>();

    @FXML
    MainScreenController mainScreenController;

    Image biohazardImage;


    public SanitationController() {}

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        biohazardImage = new Image("/Images/biohazard_image.png");

        tableSanitationType.add("Bodily Fluids");
        tableSanitationType.add("Water / Liquid");
        tableSanitationType.add("Debris and Broken Items");
        tableSanitationType.add("General Cleanup");
        cbSpec.setItems(tableSanitationType);
        cbSpec.setDisable(false);

        btnRequest.setText("Request Service");

        LinkedList<String> nodes = new LinkedList<>();

        Collection<Node> values = nodeHashMap.values();
        for(Node n : values){
            nodes.add(n.getLongName());
        }

        Collections.sort(nodes);

        tableLocationList.addAll(nodes);

        cbLocation.setItems(tableLocationList);
        cbLocation.setDisable(false);
        btnRequest.setDisable(true);

        cbLocation.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                locationSel = true;
                enableRequestBtn();
            }
        });

        cbSpec.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                specificationSel = true;
                enableRequestBtn();
            }
        });
    }

    public void enableRequestBtn(){
        if(locationSel && specificationSel){
            btnRequest.setDisable(false);
        }
    }

    @FXML
    public void setCbSpecOnClick() {
        sanTypeChosen = cbSpec.getValue();
    }

    @FXML
    public void cbLocationsOnClick(){
        location = getNodeFromLongName(nodeHashMap,cbLocation.getValue());
    }


    @FXML
    public void cbBiohazardOnClick()
    {
        if (biohazardCB.isSelected())
            isBiohazard = true;
        else
            isBiohazard = false;
    }

    @FXML
    public void launchRequestOnClick() throws GeneralSecurityException, IOException, MessagingException {
        //making instances of all required objects to put in constructor
        LinkedList<Employee> allEmps = EmployeeDB.getDBEmployees();
        LinkedList<String> details = new LinkedList<>();

//        System.out.println("VALUE OF BH IN SAN CONTROLLER IS " + isBiohazard);

        details.add(sanTypeChosen);

        String nodeLongName = cbLocation.getValue();

        location = Node.getNodeFromLongName(nodeHashMap, nodeLongName);

//        System.out.println("location node is of id " + location.getNodeID());

        //making request
        RequestMaker rm = new RequestMaker();
        Request r = rm.makeSafeSanReq(allEmps, MainScreenController.getCurEmployee(), "SAN", isBiohazard, details, description, location, null, null);

        //if(bh == null)
          //  System.out.println("LOCATION OF BH IS NULL");

        if(isBiohazard)
        {
            Node bh = location;
//            System.out.println("bh node is " + location.getLongName());

            try {
//                System.out.println("gets to try");
                NodeDB.deleteNodeDB(bh);
//                System.out.println("deletes node");
//                addBioHazIcon(bh); //normal users shouldn't be able to see if there is a biohazard in the hospital because it might scare them
//                System.out.println("adds bio icon");
//                bhNodes.add(bh);
                bhNodeHM.put(r.getReqID(), bh);
//                System.out.println("adds to nodes");
//                System.out.println("filters");
            }catch (SQLException e)
                {
                    e.printStackTrace();
                }

            mainScreenController.adminController.refresh();
            LinkedList<Node> values = new LinkedList<>(NodeDB.getDBNodes());
            Filter.setLinkedNodes(values);
            mainScreenController.directionsController.updateComboBox();
//            System.out.println("map refreshed");

        }

        btnRequest.setText("Request Made Successfully!");
        btnRequest.setDisable(false);
        isBiohazard = false;
    }

    public void addImageViewToGroup(int floor, ImageView imageView){
        if(floor == 4){
            mainScreenController.directionsController.group4.getChildren().add(imageView);
        } else if(floor == 3){
            mainScreenController.directionsController.group3.getChildren().add(imageView);
        } else if(floor == 2){
            mainScreenController.directionsController.group2.getChildren().add(imageView);
        } else if(floor == 1){
            mainScreenController.directionsController.group1.getChildren().add(imageView);
        } else if(floor == 0){
            mainScreenController.directionsController.group0.getChildren().add(imageView);
        } else if(floor == -1){
            mainScreenController.directionsController.group11.getChildren().add(imageView);
        } else if(floor == -2){
            mainScreenController.directionsController.group22.getChildren().add(imageView);
        }
    }

    public void addBioHazIcon(Node node){

        int floor = 0;
        ImageView biohazard = new ImageView(biohazardImage);
        biohazard.setFitWidth(10);
        biohazard.setPreserveRatio(true);
        biohazard.setLayoutX((node.getXCoord() / 5) - 5);
        biohazard.setLayoutY((node.getYCoord() / 5) - 5);

        if(node.getFloor().equals("4")){
            floor = 4;
        } else if(node.getFloor().equals("3")){
            floor = 3;
        } else if(node.getFloor().equals("2")){
            floor = 2;
        } else if(node.getFloor().equals("1")){
            floor = 1;
        } else if(node.getFloor().equals("G")){
            floor = 0;
        } else if(node.getFloor().equals("L1")){
            floor = -1;
        } else if(node.getFloor().equals("L2")){
            floor = -2;
        }

//        addImageViewToGroup(floor, biohazard);

        biohazard.setUserData(node);

        biohazard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//                System.out.println("clicked " + biohazard.getUserData());
            }
        });
    }


    @FXML
    public void backOnClick(){
        mainScreenController.servicesHBox.setVisible(false);
        mainScreenController.sanitation.setVisible(false);
        mainScreenController.displayDirectionsRoute(true);
        reset();
    }

    public void reset(){
        cbLocation.setValue("");
        cbSpec.setValue("");
        biohazardCB.setSelected(false);
        btnRequest.setText("Request Service");
        locationSel = false;
        specificationSel = false;
        btnRequest.setDisable(true);
    }
}
