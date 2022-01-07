package UIControllers;

import Databases.EdgeDB;
import Databases.NodeDB;
import Employee.Employee;
import Graph.Node;
import PathFinding.Context;
import PathFinding.Filter;
import PathFinding.TextualDirections;
import UIControllers.ServicesControllers.*;
import bishopfishapi.Emergency;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import floral.api.FloralApi;
import foodRequest.FoodRequest;
import foodRequest.ServiceException;
import imaging.ImagingRequest;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import Values.AppValues;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

//import imaging.ImagingRequest;

public class MainScreenController implements Initializable {

    Image img04, img03, img02, img01, img00, img10, img20, imgYAH;
    @FXML ImageView imgView;
    @FXML GesturePane gpane;
    @FXML Slider floorSlider;
    @FXML Label floorLabel;
    @FXML AnchorPane imgAnchor;
    @FXML VBox aPaneInfo;
    @FXML JFXButton btnDirections;
    @FXML JFXButton btnServices;
    @FXML JFXButton btnBooking;
    @FXML JFXButton btnAdmin;
    @FXML JFXNodesList directionsNodeList;
    @FXML JFXNodesList servicesNodeList;
    @FXML HBox directionsBox;
    @FXML HBox directionsSettingsBox;
    @FXML HBox informationBox;
    @FXML MenuButton userNameBar;
    @FXML HBox bookingBox;
    @FXML VBox toolBar;
    @FXML AnchorPane nodeInfoPane;
    @FXML AnchorPane edgeInfoPane;
    @FXML AnchorPane textualDirectionsPane;
    @FXML public HBox servicesHBox;
    @FXML Button btnInfo;
    @FXML AnchorPane infoPane;
//    @FXML JFXButton btnMI;
    @FXML HBox floorSeqHBox;
    @FXML HBox iconsHBox;

    @FXML JFXButton btnMedicalImagingAPI;

    @FXML ImageView hereIcon;
    @FXML ImageView down2;
    @FXML ImageView down3;
    @FXML ImageView downG;
    @FXML ImageView downL1;
   // @FXML ImageView


    @FXML private static Parent welcomeParent;

    //---------SubControllers---------------------------------------------
    @FXML WelcomeController welcomeController;
    @FXML DirectionsController directionsController;
    @FXML DirectionsSettingsController directionsSettingsController;
    @FXML RoomsController roomsController;
    @FXML ProfileController profileController;
    @FXML AdminController adminController;
    @FXML NodeInfoController nodeInfoController;
    @FXML EdgeInfoController edgeInfoController;
    @FXML InformationController informationController;
    @FXML TextualDirectionsController textualDirectionsController;
    //-----------All Service Controllers-----------------------------------
    @FXML AudioVisualController audioVisualController;
    @FXML CodeController codeController;
    @FXML ExternalTransportationController externalTransportationController;
    @FXML FloristController floristController;
    @FXML GiftShopController giftShopController;
    @FXML InternalTransportationController internalTransportationController;
    @FXML TechController techController;
    @FXML PatientInfoController patientInfoController;
    @FXML PrescriptionController prescriptionController;
    @FXML ReligiousRequestController religiousRequestController;
    @FXML SanitationController sanitationController;
    @FXML LanguageInterpreterController languageInterpreterController;
    @FXML MedicalImagingRequestController medicalImagingRequestController;
    //-------------------------------------------------------------------------
    @FXML public Parent sanitation;
    @FXML public Parent religiousRequest;

    public AdminController getAdminController()
    {
        return adminController;
    }
    int elapsedTime =0;
    double posX, posY;
    int floor;
    int preFloor;

    static boolean isAdmin = false;
    static boolean tapToGo = false;

    public static Employee curEmployee;

    Node curLocationNode = EdgeDB.getNodeHashMap().get("DEXIT00102");
    static Point2D currentLocation;
    static Point2D currentLocationIcon;
    int currentFloor;
    String currentBuilding = "BTM";

    boolean showingAdminMenu =false;
    boolean revertBookingMenu = false;

    Node currentNode;

    public MainScreenController(){

    }

    public void setWelcomeController(WelcomeController wc){
        this.welcomeController = wc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

//        System.out.println("Main Initialize");
        long time = System.nanoTime();
        imgYAH = new Image(getClass().getResourceAsStream("/Images/you_are_here.png"));
        img04 = new Image(getClass().getResourceAsStream("/Images/04_fourthfloor_Lmap.png"));
        img03 = new Image(getClass().getResourceAsStream("/Images/03_thirdfloor_Lmap.png"));
        img02 = new Image(getClass().getResourceAsStream("/Images/02_secondfloor_Lmap.png"));
        img01 = new Image(getClass().getResourceAsStream("/Images/01_firstfloor_Lmap.png"));
        img00 = new Image(getClass().getResourceAsStream("/Images/00_groundfloor_Lmap.png"));
        img10 = new Image(getClass().getResourceAsStream("/Images/10_lowerlevel1_Lmap.png"));
        img20 = new Image(getClass().getResourceAsStream("/Images/20_lowerlevel2_Lmap.png"));
//        checkInDate.setValue(LocalDate.now());

        setNewCurrentLocationNode("DEXIT00102"); //default starting location

        imgView.setPreserveRatio(true);
        gpane.setContent(imgView);
        imgView.setFitWidth(1340);
        gpane.setFitWidth(true);
        floor = currentFloor;
        // do not call the floor slider to change here!
        gpane.centreOn(new Point2D(500, 0));
        gpane.zoomTo(2, new Point2D(500, 0));

        aPaneInfo.setVisible(false); //it is not visible in sceneBuilder (on purpose)
        aPaneInfo.setMinWidth(0);
        aPaneInfo.setMaxWidth(0);
        bookingBox.setVisible(false);
        bookingBox.setMinWidth(0);
        bookingBox.setMaxWidth(0);
        nodeInfoPane.setVisible(false);
        edgeInfoPane.setVisible(false);
        textualDirectionsPane.setVisible(false);
        floorSeqHBox.setVisible(false);

        directionsSettingsController.tbTextualDirections.setSelected(false);
        directionsSettingsController.tbSpeechDirections.setSelected(false);

        directionsBox.setVisible(false);
        directionsSettingsBox.setVisible(false);
        servicesHBox.setVisible(false);
        sanitation.setVisible(false);
        religiousRequest.setVisible(false);
        informationBox.setVisible(false);

        directionsController.setMainScreenController(this);
        directionsSettingsController.setMainScreenController(this);
        roomsController.setMainScreenController(this);
        nodeInfoController.setMainScreenController(this);
        adminController.setMainScreenController(this);
//        profileController.setMainScreenController(this);
        edgeInfoController.setMainScreenController(this);
        informationController.setMainScreenController(this);
        textualDirectionsController.setMainScreenController(this);

        //-------ServiceControllers--------------------------------------
//        audioVisualController.setMainScreenController(this);
//        codeController.setMainScreenController(this);
//        externalTransportationController.setMainScreenController(this);
//        floristController.setMainScreenController(this);
//        giftShopController.setMainScreenController(this);
//        internalTransportationController.setMainScreenController(this);
//        techController.setMainScreenController(this);
//        patientInfoController.setMainScreenController(this);
//        prescriptionController.setMainScreenController(this);
        religiousRequestController.setMainScreenController(this);
        sanitationController.setMainScreenController(this);
      //  medicalImagingRequestController.setMainScreenController(this);
        //---------------------------------------------------------------------

        directionsController.setMyImgAnchor(imgAnchor);

        hereIcon.setVisible(true);
        hereIcon.setOpacity(.6);
        //gpane.zoomTo(4, currentLocation);
        hereIcon.setFitWidth(86/gpane.getCurrentScale());
        hereIcon.setFitHeight(140/gpane.getCurrentScale());
        // show you are here Icon on he map
        changeFloorView(floor);
        floorSlider.setValue(floor);
        directionsController.pathLabelShow = false;

        //directionsController.addYAHIcon(hereIcon, currentFloor);

        setToolTips();

        currentNode = new Node ("curNode", currentLocationIcon.getX(), currentLocationIcon.getY(),Integer.toString(currentFloor),currentBuilding,"HALL","Current Node", "Current Node");



        floorSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                floor = (int)Math.round(floorSlider.getValue());
                changeFloorView(floor);
            }
        });

        gpane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                hereIcon.setFitWidth(86/gpane.getCurrentScale());
                hereIcon.setFitHeight(140/gpane.getCurrentScale());
            }
        });

        //------------------------- Calendar things ------------------------------



    }

    //gets current node
    public Node getCurNode(){
        return this.currentNode;
    }

    @FXML
    public void directionsBtnOnClick(){
        zeroInfoWidth();
        revertToMapView();
        collapseNodeListServices();
        displayDirectionsRoute(true);
        if(showingAdminMenu){
            showingAdminMenu = false;
        }
        //                directionsController.updateComboBox();
//        directionsController.resetGroups();
        adminController.refresh(); //update nodes
        Filter.setLinkedNodes(NodeDB.getDBNodes());
    }

    // is this used???
//    @FXML
//    public void directionsClickAway(){
//        directionsBox.setVisible(false);
//    }
//
//    @FXML
//    public void directionsSettingsClickAway(){ directionsSettingsBox.setVisible(false);}
//
//    @FXML
//    public void informationClickAway(){ informationBox.setVisible(false);}

    @FXML
    public void servicesBtnOnClick() {
        collapseNodeListDirections();
        zeroInfoWidth();
        revertToMapView();
        if(showingAdminMenu){
            showingAdminMenu = false;
        }

        //servicesBox.setVisible(true);

    }

//    @FXML
//    public void servicesClickAway(){
//        servicesHBox.setVisible(false);
//    }


    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(15), e -> {
                System.out.println("--------------------------------------------------we got here");
                this.roomsController.displayWorkZones();
                this.roomsController.displayWorkZoneLabels();
                //displayWorkZones();
                // code to execute here...
            })
    );

    @FXML
    public void bookingBtnOnClick(){
        displayDirectionsRoute(false);
        floorSeqHBox.setVisible(false);
        zeroInfoWidth();
        bookingPaneWidth();
        toolBar.setOpacity(0);
        toolBar.setVisible(false);
        roomsController.label.setText("");
        roomsController.calendarPaneZeroWidth();
        roomsController.viewingCal = false;
        if(showingAdminMenu){
            showingAdminMenu = false;
        }
        floorSlider.setDisable(true);
        floorLabel.setDisable(true);
        changeFloorView(4);
        gpane.centreOn(new Point2D(1400, 350));
        gpane.zoomTo(5, new Point2D(1400, 350));


        // makes some path finding elements invisible
        textualDirectionsPane.setVisible(false);
        directionsController.pathLabel.setVisible(false);
        textualDirectionsController.directionsCircle.setVisible(false);


        roomsController.displayWorkZones();
        roomsController.displayWorkZoneLabels();

        timeline.setCycleCount(100);
        timeline.play();

    }

    @FXML
    public void adminBtnOnClick(){
        if(!showingAdminMenu){
            imgAnchor.getChildren().clear();
            displayDirectionsRoute(false);
            floorSeqHBox.setVisible(false);
            revertToMapView();
            textualDirectionsPane.setVisible(false);
            directionsController.pathLabel.setVisible(false);
            textualDirectionsController.directionsCircle.setVisible(false);
            adminPaneWidth();
            showingAdminMenu = true;
            adminController.refresh(); //refresh nodes
        } else {
            displayDirectionsRoute(true);
            zeroInfoWidth();
            revertToMapView();
            if(directionsController.pathFindingTextual){
                textualDirectionsPane.setVisible(true);
            }
            directionsController.pathLabel.setVisible(true);
            textualDirectionsController.directionsCircle.setVisible(true);
            showingAdminMenu = false;
            nodeInfoPane.setVisible(false);
            edgeInfoPane.setVisible(false);
            adminController.editingEdge = false;
        }
    }

    @FXML
    public void mapClick(MouseEvent event) {
        posX = (int)(event.getX());
        posY = (int)(event.getY());
//        System.out.println(posX + ", " + posY + ", ");
        Point2D point = new Point2D(posX,posY);
        if(tapToGo) {
            directionsController.goToOnClick();
            tapToGo = false;
        }
        // routes are shown, for ViewRouteController


        nodeInfoController.setNodeXYZ(point, floor);

    }

    @FXML
    public void myProfileOnClick(ActionEvent event) throws IOException{

        ProfileController.setMainScreenParent(welcomeController.getMainScreenParent());
        Parent parent = FXMLLoader.load(getClass().getResource("/Scenes/ProfileScene.fxml"));
        Scene scene = userNameBar.getScene();
        scene.setRoot(parent);
//        ProfileController.setMainScreenParent(welcomeController.getMainScreenParent());
//        FXMLLoader profileLoader = new FXMLLoader();
//        System.out.println(getClass().getResource("/Scenes/ProfileScene.fxml"));
//        profileLoader.setLocation(getClass().getResource("/Scenes/ProfileScene.fxml"));
//        System.out.println(profileLoader.getLocation());
//        profileLoader.setController(new ProfileController());
//        Parent parent = profileLoader.load();
//        profileController = profileLoader.getController();
//        profileController.setMainScreenController(this);
//        Scene scene = userNameBar.getScene();
//        scene.setRoot(parent);
    }


    @FXML
    public void logoutOnClick(ActionEvent event) {
        textualDirectionsPane.setVisible(false);
        directionsSettingsController.tbSpeechDirections.setSelected(false);
        directionsSettingsController.tbTextualDirections.setSelected(false);
        directionsSettingsController.rbEnglish.setSelected(true);
        directionsSettingsController.rbNoCond.setSelected(true);
        directionsController.pathFindingTextual = false;
        textualDirectionsController.directionsCircle.setVisible(false);
        directionsController.pathFindingSpeech = false;
        directionsController.pathFindingConditions = 1; //no conditions
        directionsController.textualLanguage = 1; //english
        zeroInfoWidth();
        revertToMapView();
        toolBar.setOpacity(1);
        toolBar.setVisible(true);
        revertToMapView();
        showingAdminMenu = false;
        adminController.editingEdge = false;
        nodeInfoPane.setVisible(false);
        edgeInfoPane.setVisible(false);
        roomsController.resetView();

        //roomsController.viewingCal = false; //--------------------comment line back in after merging with pk_iCalendar
        displayDirectionsRoute(true);
        floorSeqHBox.setVisible(false);
        System.out.println("Hai");
        gpane.centreOn(new Point2D(500, 0));
        gpane.zoomTo(2, new Point2D(500, 0));

        directionsController.pathLabelShow = false;

        welcomeController.scanLabel.setText("SWIPE ID TO BEGIN");
        Scene scene = userNameBar.getScene();
        scene.setRoot(welcomeParent);

        collapseNodeListServices();
        collapseNodeListDirections();
        welcomeController.altLoginScreen.setVisible(false);
        welcomeController.bigButton.setDisable(false);
        welcomeController.login.clear();
        welcomeController.username.clear();
        welcomeController.password.clear();
        welcomeController.login.requestFocus();
        reset();
    }


    public void displayDirectionsRoute(boolean show){
        // shows all the groups for all the directions

        for(Context context: directionsController.drawnPaths.keySet()){
            for(int i = 0; i < 7; i++){
                directionsController.drawnPaths.get(context).get(i).setVisible(show);
//                System.out.println("for context " + context + " got group" + directionsController.drawnPaths.get(context).get(i));
            }
//            System.out.println(context + " displayed");
        }
//        System.out.println("displayed all contexts");
    }


    public void revertToMapView(){
        floorSlider.setDisable(false);
        floorLabel.setDisable(false);
        changeFloorView(floor);
    }

    void zeroInfoWidth(){
        aPaneInfo.setMaxWidth(0);
        aPaneInfo.setMinWidth(0);
        aPaneInfo.setVisible(false);
        bookingBox.setVisible(false);
        bookingBox.setMaxWidth(0);
        bookingBox.setMinWidth(0);
        nodeInfoPane.setVisible(false);
        edgeInfoPane.setVisible(false);
        adminController.btnShowNodes.setSelected(false);
        adminController.btnShowEdges.setSelected(false);
    }

    public void bookingPaneWidth(){
        bookingBox.setMinWidth(300);
        bookingBox.setMaxWidth(300);
        bookingBox.setVisible(true);
        collapseNodeListDirections();
        collapseNodeListServices();
    }

    public void bookPaneWidthCalendar(){
//        bookingBox.setMinWidth(1920);
        bookingBox.setPrefWidth(1920);
        bookingBox.setMaxWidth(1920);
    }

    public void adminPaneWidth(){
        aPaneInfo.setPrefWidth(130);
        aPaneInfo.setMaxWidth(130);
        aPaneInfo.setVisible(true);
        collapseNodeListDirections();
        collapseNodeListServices();
    }

    public void changeFloorView(int floor){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        //preFloor = floor;

        switch (floor) {
            case 4: //reserve room floor
//                gpane.reset();
                imgView.setImage(img04);
                gpane.setContent(imgView);
                imgAnchor.getChildren().clear();
                if(!showingAdminMenu && directionsController.pathLabelShow){
                    managePath(0,4, random);
                    imgAnchor.getChildren().add(directionsController.pathLabel);
                }
                directionsController.addYAHIcon(hereIcon, 4);
                if(adminController.btnShowEdges.isSelected()) {adminController.showEdgesOnClick();}
                if(adminController.btnShowNodes.isSelected()){adminController.showNodesOnClick();}
                if(adminController.editingEdge) {edgeInfoPane.setVisible(true);}
                break;
            case 3:
                imgView.setImage(img03);
                gpane.setContent(imgView);
                imgAnchor.getChildren().clear();
                if(!showingAdminMenu && directionsController.pathLabelShow) {
                    managePath(1, 3, random);
                    imgAnchor.getChildren().add(directionsController.pathLabel);
                }
                directionsController.addYAHIcon(hereIcon, 3);
                if(adminController.btnShowEdges.isSelected()) {adminController.showEdgesOnClick();}
                if(adminController.btnShowNodes.isSelected()){adminController.showNodesOnClick();}
                if(adminController.editingEdge) {edgeInfoPane.setVisible(true);}
                break;
            case 2:
                imgView.setImage(img02);
                gpane.setContent(imgView);
                imgAnchor.getChildren().clear();
                if(!showingAdminMenu && directionsController.pathLabelShow) {
                    managePath(2, 2, random);
                    imgAnchor.getChildren().add(directionsController.pathLabel);
                }
                directionsController.addYAHIcon(hereIcon, 2);
                if(adminController.btnShowEdges.isSelected()) {adminController.showEdgesOnClick();}
                if(adminController.btnShowNodes.isSelected()){adminController.showNodesOnClick();}
                if(adminController.editingEdge) {edgeInfoPane.setVisible(true);}
                break;
            case 1:
                imgView.setImage(img01);
                gpane.setContent(imgView);
                imgAnchor.getChildren().clear();
                if(!showingAdminMenu && directionsController.pathLabelShow){
                    managePath(3,1, random);
                    imgAnchor.getChildren().add(directionsController.pathLabel);
                }
                directionsController.addYAHIcon(hereIcon, 1);
                if(adminController.btnShowEdges.isSelected()) {adminController.showEdgesOnClick();}
                if(adminController.btnShowNodes.isSelected()){adminController.showNodesOnClick();}
                if(adminController.editingEdge) {edgeInfoPane.setVisible(true);}
                break;
            case 0:
                imgView.setImage(img00);
                gpane.setContent(imgView);
                imgAnchor.getChildren().clear();
                if(!showingAdminMenu && directionsController.pathLabelShow){
                    managePath(4,0, random);
                    imgAnchor.getChildren().add(directionsController.pathLabel);
                }
                directionsController.addYAHIcon(hereIcon, 0);
                if(adminController.btnShowEdges.isSelected()) {adminController.showEdgesOnClick();}
                if(adminController.btnShowNodes.isSelected()){adminController.showNodesOnClick();}
                if(adminController.editingEdge) {edgeInfoPane.setVisible(true);}
                break;
            case -1:
                imgView.setImage(img10);
                gpane.setContent(imgView);
                imgAnchor.getChildren().clear();
                if(!showingAdminMenu && directionsController.pathLabelShow){
                    managePath(5,-1, random);
                    imgAnchor.getChildren().add(directionsController.pathLabel);
                }
                directionsController.addYAHIcon(hereIcon, -1);
                if(adminController.btnShowEdges.isSelected()) {adminController.showEdgesOnClick();}
                if(adminController.btnShowNodes.isSelected()){adminController.showNodesOnClick();}
                if(adminController.editingEdge) {edgeInfoPane.setVisible(true);}
                break;
            case -2:
                imgView.setImage(img20);
                gpane.setContent(imgView);
                imgAnchor.getChildren().clear();
                if(!showingAdminMenu && directionsController.pathLabelShow){
                    managePath(6,-2, random);
                    imgAnchor.getChildren().add(directionsController.pathLabel);
                }
                directionsController.addYAHIcon(hereIcon, -2);
                if(adminController.btnShowEdges.isSelected()) {adminController.showEdgesOnClick();}
                if(adminController.btnShowNodes.isSelected()){adminController.showNodesOnClick();}
                if(adminController.editingEdge) {edgeInfoPane.setVisible(true);}
                break;
        }
        preFloor = floor;
    }

    /**
     * draws the path and moves the label
     * @param groupID
     * @param floor
     * @param random
     */
    private void managePath(int groupID, int floor, Random random){
        // text directions per floor
        if(directionsController.pathFindingTextual){
            // check if getDirectionsChange Coords is null
            if (TextualDirections.getDirectionsChangeCoords() != null) {
                // check if circle should be displayed
                // circle has that floor
                for(double[] stepCoords: TextualDirections.getDirectionsChangeCoords()){
                    if(stepCoords[2] == floor){
                        // check if floor memory exists for that circle

                        if(textualDirectionsController.floorMemory.containsKey(floor)){
                            // place the circle to the floor memory
                            textualDirectionsController.directionsCircle.setCenterX(textualDirectionsController.floorMemory.get(floor).getX());
                            textualDirectionsController.directionsCircle.setCenterY(textualDirectionsController.floorMemory.get(floor).getY());
                            textualDirectionsController.directionsCircle.setVisible(true);
                            imgAnchor.getChildren().add(textualDirectionsController.directionsCircle);
                        }
                        else{
                            textualDirectionsController.directionsCircle.setVisible(false);
                        }
                        break;
                    }
                }
            }
        }


        for (Context context : directionsController.drawnPaths.keySet()) {
            imgAnchor.getChildren().add(directionsController.drawnPaths.get(context).get(groupID)); // adds the correct contexts
            // should only run if the floor changes
            if(true/*floor != preFloor*/) {
                if (directionsController.routeContext == null) {
                    // label could not be assigned yet
                    directionsController.pathLabel.setVisible(false);
                } else if (context.getStrategy().equals(directionsController.routeContext.getStrategy())) {
                    // randomly selects a node, if that node is a line calls the label on that line
                    // unless a coordinate has already been selected
                    if(!directionsController.floorMemory.containsKey(floor)) {
                        // easy function, just needs context and group index
                        int nodeCount = directionsController.drawnPaths.get(context).get(groupID).getChildren().size();
                        if (nodeCount != 0) { // must be positive to be a bounds, can select any node
                            int randomInt;
                            javafx.scene.Node nodeSeleted;
                            do{
                                randomInt = random.nextInt(nodeCount);
                                nodeSeleted = directionsController.drawnPaths.get(context).get(groupID).getChildren().get(randomInt);
                            } while(!(nodeSeleted instanceof Line));
                            Point2D point2D = new Point2D(((Line) nodeSeleted).getStartX(), ((Line) nodeSeleted).getStartY());
                            directionsController.floorMemory.put(floor, point2D);
                            directionsController.pathLabel.setLayoutX(point2D.getX());
                            directionsController.pathLabel.setLayoutY(point2D.getY());
//                            System.out.println("set memory to" + point2D);
                            directionsController.pathLabel.setVisible(true);
                        } else {
                            directionsController.pathLabel.setVisible(false); // should not be visible if there are no nodes
                        }
                    }
                    else{
                        // already selected, get from hashmap
                        Point2D pointRecall = directionsController.floorMemory.get(floor);
                        directionsController.pathLabel.setLayoutX(pointRecall.getX());
                        directionsController.pathLabel.setLayoutY(pointRecall.getY());
                        directionsController.pathLabel.setVisible(true);
                    }
                }
            }
        }
    }

    void goToCurrentLocation(){
        changeFloorView(currentFloor);
        floorSlider.setValue(currentFloor);
//        hereIcon.setVisible(true);
        gpane.zoomTo(4, currentLocation);
        gpane.centreOn(currentLocation);
//        hereIcon.setFitWidth(86/gpane.getCurrentScale());
//        hereIcon.setFitHeight(140/gpane.getCurrentScale());
    }

    public void showUserName() {
        userNameBar.setText(curEmployee.getFirstName() + " " + curEmployee.getLastName());
    }

    public void setCurEmployee(Employee curEmployee) {
        this.curEmployee = curEmployee;
        AppValues.getInstance().curEmp = curEmployee;
    }

    public static Employee getCurEmployee() {
        return curEmployee;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        if(isAdmin){
            btnAdmin.setVisible(true);
        } else {
            btnAdmin.setVisible(false);
        }
    }

    public static void setWelcomeParent(Parent welcomeParent) {
        MainScreenController.welcomeParent = welcomeParent;
    }

    void reset() {
        directionsController.group4.getChildren().clear();
        directionsController.group3.getChildren().clear();
        directionsController.group2.getChildren().clear();
        directionsController.group1.getChildren().clear();
        directionsController.group0.getChildren().clear();
        directionsController.group11.getChildren().clear();
        directionsController.group22.getChildren().clear();

        directionsController.drawnPaths.clear();
        directionsController.pathLabel.setVisible(false);
        textualDirectionsController.directionsCircle.setVisible(false);

        directionsSettingsController.ckbAStar.setSelected(true);
        directionsSettingsController.ckbBestFirst.setSelected(false);
        directionsSettingsController.ckbBreadthFirst.setSelected(false);
        directionsSettingsController.ckbDepthFirst.setSelected(false);
        directionsSettingsController.ckbDjikstra.setSelected(false);
        directionsSettingsController.ckbGrandTour.setSelected(false);

        directionsController.algoritmsChosen.clear();
        directionsController.drawnPaths.clear();
        nodeInfoPane.setVisible(false);
        edgeInfoPane.setVisible(false);
        zeroInfoWidth();
        changeFloorView(currentFloor);
        floorSlider.setValue(currentFloor);
    }


    public void setNewCurrentLocationNode(String nodeID){
        curLocationNode = EdgeDB.getNodeHashMap().get(nodeID);
//        curLocationNode = directionsController.nodeHashMap.get(nodeID);
        if(curLocationNode.getFloor().equals("4")){
            currentFloor = 4;
        } else if(curLocationNode.getFloor().equals("3")){
            currentFloor = 3;
        } else if(curLocationNode.getFloor().equals("2")){
            currentFloor = 2;
        } else if(curLocationNode.getFloor().equals("1")){
            currentFloor = 1;
        } else if(curLocationNode.getFloor().equals("G")){
            currentFloor = 0;
        } else if(curLocationNode.getFloor().equals("L1")){
            currentFloor = -1;
        } else if(curLocationNode.getFloor().equals("L2")){
            currentFloor = -2;
        }
        floor = currentFloor;
        currentLocation = new Point2D(curLocationNode.getXCoord(), curLocationNode.getYCoord());
        currentLocationIcon = new Point2D(curLocationNode.getXCoord()/5, curLocationNode.getYCoord()/5);
        hereIcon.setLayoutX((currentLocationIcon.getX()));
        hereIcon.setLayoutY((currentLocationIcon.getY()));
    }


    //-----------all quick direction buttons---------------------
    @FXML
    public void btnExitOnClick() {
        directionsController.exitOnClick();
        collapseNodeListDirections();
    }

    @FXML
    public void btnBathroomOnClick() {
        directionsController.bathroomOnClick();
        collapseNodeListDirections();
    }

    @FXML
    public void btnFoodOnClick() {
        directionsController.foodOnClick();
        collapseNodeListDirections();
    }

    @FXML
    public void btnInfoOnClick() {
        directionsController.infoDeskOnClick();
        collapseNodeListDirections();
    }

    @FXML
    public void btnGiftShopOnClick() {
        directionsController.giftShopOnClick();
        collapseNodeListDirections();
    }

    @FXML
    public void btnParkingOnClick() {
        directionsController.parkingOnClick();
        collapseNodeListDirections();
    }

    @FXML
    public void btnUberOnClick() {
        directionsController.uberOnClick();
        collapseNodeListDirections();
    }

    @FXML
    public void btnTapToGoOnClick() {
        //toast to tell user to click on map
        //set a boolean to true to tells onMapClick to run astar when clicked next
        //
        tapToGo = true;
        collapseNodeListDirections();
    }

    @FXML
    public void btnAdvancedOnClick() {
        directionsBox.setVisible(true);
        directionsController.btnGoA.setDisable(true);
        collapseNodeListDirections();
        directionsController.updateComboBox();
    }

    @FXML
    public void settingsOnClick(){
        directionsSettingsBox.setVisible(true);
        collapseNodeListDirections();
    }

    public void collapseNodeListDirections(){
        directionsNodeList.animateList(false);
    }
    public void collapseNodeListServices(){
        servicesNodeList.animateList(false);
    }

    //-------------------------------services buttons---------------------------------------------


    @FXML
    public void btnSanitationServices() {
        servicesHBox.setVisible(true);
        sanitation.setVisible(true);
        displayDirectionsRoute(false);
        floorSeqHBox.setVisible(false);
        //servicesTabPane.getSelectionModel().select(sanitationTab);
        collapseNodeListServices();
    }

    @FXML
    public void btnFlorist(){
        collapseNodeListServices();
        FloralApi floralApi = new FloralApi();

        try {
            floralApi.run(425, 150, "/MainStyles.css", "ACONF00102");
        } catch (Exception e){
//            System.out.println("Floral API failed");
            e.printStackTrace();
        }

    }

    @FXML
    public void religiousOnClick(){
        servicesHBox.setVisible(true);
        religiousRequest.setVisible(true);
        collapseNodeListServices();
        displayDirectionsRoute(false);
        floorSeqHBox.setVisible(false);
    }

    @FXML
    public void medicalImagingOnClick(){
        collapseNodeListServices();
        ImagingRequest imgRqst = new ImagingRequest();
        imgRqst.run(0,0,1080,1920,null,"ELABS00101", "ELABS00101");
    }

    @FXML
    public void codeServiceOnClick(){
        collapseNodeListServices();
        Emergency.setRecipient("");
        Emergency.setSender("");
        Emergency.setSenderPassword("");
        Emergency e = new Emergency();

        try {
            e.run(560, 250, 800, 600, "/MainStyles.css", "destNode", "originNode");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void foodDeliveryOnClick() throws ServiceException {
        collapseNodeListServices();
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.run(0, 0, 1920, 1080, null, "ACONF00102", "ADEPT00301");
    }

    @FXML
    public void btnInfoShow(){
        informationBox.setVisible(true);
    }

    //--------------------------------tool tips-----------------------------------
    public void setToolTips(){
        btnServices.setTooltip(new Tooltip("Service Requests"));
        btnAdmin.setTooltip(new Tooltip("Admin Features"));
        btnBooking.setTooltip(new Tooltip("Room Booking"));
        btnDirections.setTooltip(new Tooltip("Directions"));
    }

    public void  mementoOnClick(){
        Parent parent = AppValues.getInstance().mainScene;
        Scene scene  = AppValues.getInstance().currScene;
        scene.setRoot(parent);
    }
    void setUpTimer(){

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                //System.out.println("thread is running...");
                double xMouse = MouseInfo.getPointerInfo().getLocation().getX();
                double yMouse = MouseInfo.getPointerInfo().getLocation().getY();

                double xMouseNew;
                double yMouseNew;


                while (elapsedTime < 30*1000) {
                    xMouseNew = MouseInfo.getPointerInfo().getLocation().getX();
                    yMouseNew = MouseInfo.getPointerInfo().getLocation().getY();

                    if (xMouse != xMouseNew || yMouse != yMouseNew) {
                        elapsedTime = 0;

                        xMouse = MouseInfo.getPointerInfo().getLocation().getX();
                        yMouse = MouseInfo.getPointerInfo().getLocation().getY();
                        continue;
                    } else {
                        if(elapsedTime == 30){

                            mementoOnClick();

                            elapsedTime = 0;
                        }
                    }

                    elapsedTime ++;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(elapsedTime + " sec");
                }
            }
        };

        Timer timer = new Timer("MyTimer");//create a new Timer
        timer.scheduleAtFixedRate(timerTask, 10, 100);

    }
}
