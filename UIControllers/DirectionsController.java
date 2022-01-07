package UIControllers;

import Databases.EdgeDB;
import Databases.NodeDB;
import FuzzySearch.AutoCompleteComboBoxListener;
import Graph.Node;
import PathFinding.*;
import Roomba.SendRoombaCommands;
import TTS.TextToSpeech;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

import static Graph.Node.getNodeFromLongName;

public class DirectionsController implements Initializable {

    @FXML Button btnGoA;
    @FXML JFXComboBox<String> comboBoxTo;
    @FXML JFXComboBox<String> comboBoxFrom;
    @FXML JFXComboBox<String> comboBoxToFloor;
    @FXML JFXComboBox<String> comboBoxFromFloor;


    HashMap<String, Node> nodeHashMap = new HashMap<>();

    String toNodeName, fromNodeName;
    private String toNodeID, fromNodeID;
    Node toNode, fromNode;

    Group group = new Group();
    Group group4 = new Group();
    Group group3 = new Group();
    Group group2 = new Group();
    Group group1 = new Group();
    Group group0 = new Group();
    Group group11 = new Group();
    Group group22 = new Group();

    Label pathLabel = new Label();
    boolean pathLabelShow = false; // check if the label should  be shown
    boolean getFloorIconCalled = false;

    int routeFloor;
    HashMap<Integer, Point2D> floorMemory;
    int startingFloor = 0;
    Context routeContext;
    Context fastestRoute;
    double[][] setCoords;

    private AnchorPane myImgAnchor;

    private double[][] allRouteXYZ = new double[1][3]; // dynamically sized

    //int pathFindingChoice = 1;
    ArrayList<Strategy> algoritmsChosen = new ArrayList<>();
    ArrayList<Context> contextsFromAlgorithmsChosen = new ArrayList<>();
    HashMap<Context, double[][]> pathResults = new HashMap<>();
    HashMap<Context, ArrayList<Group>> drawnPaths = new HashMap<Context, ArrayList<Group>>();


    double[][] routeXYZ;
    int pathFindingChoice = 1;
    int pathFindingConditions = 1;
    int textualLanguage = 1;
    //private int routeFloor;
    private int hotKeyStatus;
    private List<String> nodeList;
    boolean pathFindingSpeech = false;
    boolean pathFindingTextual = false;

    Image downL2, downL1, downG, down1, down2, down3;
    Image up4, up3, up2, up1, upG, upL1;
    Image floor4, floor3, floor2, floor1, floorG, floorL1, floorL2;

    boolean toSelected = false;
    boolean fromSelected = false;

    @FXML
    MainScreenController mainScreenController;

    public DirectionsController() {

    }

    public void setMainScreenController(MainScreenController mc) {
        this.mainScreenController = mc;
    }

    public HashMap<String, Node> getNodeHashMap() {
        return this.nodeHashMap;
    }

    public void setNodeHashMap(HashMap<String, Node> nodeHashMap) {
        this.nodeHashMap = nodeHashMap;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("Init Directions Controller");
        //below is needed because it is the first initialization of the hashMap
        NodeDB.createHashMapDB();
        EdgeDB.populateHashMapDB();
        nodeHashMap = EdgeDB.getNodeHashMap();
        populateChoiceBox();
        populateFloorChoiceBox();



        LinkedList<Node> values = new LinkedList<>(nodeHashMap.values());

        Filter.setLinkedNodes(values);

        comboBoxToFloor.getSelectionModel().selectLast();
        comboBoxFromFloor.getSelectionModel().selectLast();

        loadAllImages();
        btnGoA.setDisable(true);

        comboBoxFrom.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fromSelected = true;
                disableGoBtn();
            }
        });

        comboBoxTo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                toSelected = true;
                disableGoBtn();
            }
        });

    }

    public void disableGoBtn(){
        if(fromSelected && toSelected){
            btnGoA.setDisable(false);
        } else {
            btnGoA.setDisable(true);
        }
    }

    @FXML
    public void updateComboBox() {
        NodeDB.createHashMapDB();
        EdgeDB.populateHashMapDB();
        nodeHashMap = EdgeDB.getNodeHashMap();
        populateChoiceBox();

    }

    @FXML
    public void backOnClick(){
        mainScreenController.directionsBox.setVisible(false);
        btnGoA.setDisable(true);
        comboBoxFrom.setValue("");
        comboBoxTo.setValue("");
        comboBoxFromFloor.setValue("");
        comboBoxToFloor.setValue("");
        toSelected = false;
        fromSelected = false;
    }

    void populateFloorChoiceBox() {
        List<String> floors = new LinkedList<>();
        floors.add("4");
        floors.add("3");
        floors.add("2");
        floors.add("1");
        floors.add("G");
        floors.add("L1");
        floors.add("L2");
        floors.add("All");

        comboBoxFromFloor.getItems().addAll(floors);
        comboBoxToFloor.getItems().addAll(floors);
    }

    void populateFromChoiceBox() {
        comboBoxFromFloor.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

                    Filter.setFloorFilter(newValue);
                    nodeList = Filter.toLongName(Filter.getFloorFilter());
                    Collections.sort(nodeList);
                    comboBoxFrom.getItems().clear();
                    comboBoxFrom.getItems().addAll(nodeList);
                }
        );
    }

    void populateToChoiceBox() {
        comboBoxToFloor.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

                    Filter.setFloorFilter(newValue);
                    nodeList = Filter.toLongName(Filter.getFloorFilter());
                    Collections.sort(nodeList);

                    comboBoxTo.getItems().clear();
                    comboBoxTo.getItems().addAll(nodeList);
                }
        );
    }

    void populateChoiceBox() {
        populateFromChoiceBox();
        populateToChoiceBox();
        AutoCompleteComboBoxListener a = new AutoCompleteComboBoxListener(comboBoxTo);
        AutoCompleteComboBoxListener b = new AutoCompleteComboBoxListener(comboBoxFrom);

    }


    public void exitOnClick() {
        hotKeyStatus = 1;
        goOnClick();
    }

    public void bathroomOnClick() {
        hotKeyStatus = 2;
        goOnClick();
    }

    public void foodOnClick() {
        hotKeyStatus = 3;
        goOnClick();
    }

    public void infoDeskOnClick() {
        hotKeyStatus = 4;
        goOnClick();
    }

    public void giftShopOnClick() {
        hotKeyStatus = 5;
        goOnClick();
    }

    public void parkingOnClick() {
        hotKeyStatus = 6;
        goOnClick();
    }

    public void uberOnClick() {
        hotKeyStatus = 7;
        goOnClick();
    }

    public double getXCoord() {
        return mainScreenController.currentLocation.getX();
    }

    public double getYCoord() {
        return mainScreenController.currentLocation.getY();
    }

    // public int getZCoord() { return (int)mainScreenController.currentLocation2.getZ(); }

    public double getPosX() {
        return mainScreenController.posX * 5;
    }

    public double getPosY() {
        return mainScreenController.posY * 5;
    }

    @FXML
    public void goOnClick() {
        hotKeyPathFind();
        //mainScreenController.gpane.reset();
        for (Context pathResult : pathResults.keySet()) {
            drawRoute(pathResult, pathResults.get(pathResult));
            addRouteDetails();
        }
        //drawRoute(routeXYZ);
    }

    @FXML
    public void goOnClickA() {
        advancedPathFind();
        //mainScreenController.gpane.reset();
        for (Context pathResult : pathResults.keySet()) {
            drawRoute(pathResult, pathResults.get(pathResult));
            addRouteDetails();
        }
        //drawRoute(routeXYZ);
       // mainScreenController.directionsBox.setVisible(false);
        mainScreenController.gpane.reset();
        //drawRoute(routeXYZ);
        mainScreenController.directionsBox.setVisible(false);
        btnGoA.setDisable(true);
        comboBoxFrom.setValue("");
        comboBoxTo.setValue("");
        comboBoxToFloor.setValue("");
        comboBoxFromFloor.setValue("");
        toSelected = false;
        fromSelected = false;
    }

    public void setMyImgAnchor(AnchorPane myImgAnchor) {
        this.myImgAnchor = myImgAnchor;
    }


    /**
     * advancedPathFind()
     * Runs path finding from the advanced selection boxes (selection of Nodes)
     */
    void advancedPathFind() {
        fromNode = getNodeFromLongName(nodeHashMap, comboBoxFrom.getValue());
        toNode = getNodeFromLongName(nodeHashMap, comboBoxTo.getValue());
        pathFind();
    }

    /**
     * hotKeyPathFind()
     * Runs path finding from hot keys
     */
    void hotKeyPathFind() {
        fromNode = mainScreenController.curLocationNode;
        toNode = PathFindingPrep.findEndNode(nodeHashMap, getXCoord(), getYCoord(), mainScreenController.floor, hotKeyStatus);
        pathFind();
    }

    /**
     * goToOnClick()
     * Runs path finding from tap to go, origin is Kiosk location
     */
    public void goToOnClick() {
        fromNode = mainScreenController.curLocationNode;
        toNode = PathFindingPrep.clickEndNode(nodeHashMap, getPosX(), getPosY(), mainScreenController.floor);
        pathFind();
        for (Context pathResult : pathResults.keySet()) {
            drawRoute(pathResult, pathResults.get(pathResult));
        }

        addRouteDetails();

        //drawRoute(routeXYZ);

    }

    private void addRouteDetails() {
        //mainScreenController.imgAnchor.getChildren().add(pathLabel);
        pathLabel.setVisible(false);
        pathLabel.setStyle("-fx-padding: 7; -fx-opacity: 0.75; -fx-text-fill: midnightblue; -fx-background-color: white; -fx-background-radius: 30; -fx-font-size: 7; -fx-font-weight: bold");

        // each path
        for (Context context : drawnPaths.keySet()) {
            // each group in a given path
            int groupid = 0;
            for (Group group : drawnPaths.get(context)) {
                for (javafx.scene.Node node : group.getChildren()) {
                    if (node instanceof Line) {
                        Line line = (Line) node;
                        // add aware line functionality
                        final int groupID = groupid;
                        line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                // textual directions reset
                                mainScreenController.textualDirectionsController.directionsCircle.setVisible(false);
                                mainScreenController.textualDirectionsController.mapThingacr.getPanes().removeAll(mainScreenController.textualDirectionsController.getSteps());
                                // sets all the line opacity in that group
                                group.toFront();
                                for(Group theseGroups: drawnPaths.get(context)) {
                                    for (javafx.scene.Node node : theseGroups.getChildren()) {
                                        if (node instanceof Line) {
                                            Line otherLinesInGroup = (Line) node;
                                            otherLinesInGroup.setOpacity(1);
                                        }
                                        if (node instanceof ImageView) {
                                            ImageView otherImageViewsInGroup = (ImageView) node;
                                            otherImageViewsInGroup.setOpacity(0.75);
                                        }
                                    }
                                }
                                routeContext = context; // sets for the directions options and the floor slider
                                routeFloor = 4 - groupID;
                                startingFloor = (int) pathResults.get(context)[0][2];

                                // sets up the label
                                // some of this could be removed with css styles
//                                System.out.println("you clicked on: " + context + " floor" + routeFloor);
                                Point2D point2D = new Point2D(event.getX(), event.getY());
                                pathLabel.setLayoutX(point2D.getX());
                                pathLabel.setLayoutY(point2D.getY());
                                floorMemory.remove(routeFloor);
                                floorMemory.put(routeFloor, point2D);

                                // has to be set dynamically
                                pathLabel.setBorder(new Border(new BorderStroke(line.getStroke(), BorderStrokeStyle.SOLID, new CornerRadii(25), new BorderWidths(2))));

                                double totalTime = context.getTotalTime();
//                                System.out.println("total time is " + totalTime);
                                int minutes = (int)( totalTime / 60.0);
                                int seconds = (int)( totalTime - (minutes * 60));

                                String secondsString = String.valueOf(seconds);
                                if(secondsString.length()==1){ // adds and extra 0 if needed
                                    secondsString = ("0" + secondsString);
                                }

                                double totalDist = context.getTotalDist();

                                // if there are no other routs then last option should no show
                                drawnPaths.keySet().size();


                                pathLabel.setText(context.toString() + " Route\n"
                                        + "Time " + (int) minutes + ":" + secondsString + "\n"
                                        + "Distance " + (int) Math.ceil(totalDist) + " feet\n" +
                                        "click to set route");
                                pathLabelShow = true;

                                // adds the 'button'
                                pathLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        routeContext = context; // sets for the directions options and the floor slider
                                        routeFloor = 4 - groupID;
                                        startingFloor = (int) pathResults.get(context)[0][2];
                                        // textual directions reset
                                        mainScreenController.textualDirectionsController.directionsCircle.setVisible(false);
                                        mainScreenController.textualDirectionsController.mapThingacr.getPanes().removeAll(mainScreenController.textualDirectionsController.getSteps());
//                                        System.out.println(" you clicked on the label for " + context);
                                        // set all other groups to less occupancy
                                        for (Context otherContext : drawnPaths.keySet()) {
                                            // filter out the other algorithms
                                            if (!otherContext.getStrategy().equals(context.getStrategy())) {
                                                // set all the other group's occupancies
                                                for (Group otherGroup : drawnPaths.get(otherContext)) {
                                                    for (javafx.scene.Node otherNode : otherGroup.getChildren()) {
                                                        if (otherNode instanceof Line) {
                                                            Line otherLine = (Line) otherNode;
                                                            otherLine.setOpacity(0.25);
                                                        }
                                                        if (otherNode instanceof ImageView) {
                                                            ImageView otherImageViewsInGroup = (ImageView) otherNode;
                                                            otherImageViewsInGroup.setOpacity(0);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        setCoords = pathResults.get(routeContext); // sets the coords for the options
                                        // directions options
                                        doDirectionsOptions();
                                    }
                                });
                                pathLabel.setVisible(true);
                                pathLabelShow = true;
                                pathLabel.toFront();
                            }
                        });
                    }
                }
                groupid++;
            }
        }
    }

    void pathFind() {
        pathLabelShow = true;
        // reset of other systems
        mainScreenController.textualDirectionsController.mapThingacr.getPanes().removeAll();

        routeContext = null; // reset label context
        floorMemory = new HashMap<>();
        // A star is the default if they did not open the settings and try to use tap to go or nothing is checked
        if (algoritmsChosen.size() == 0) {
            algoritmsChosen.add(new AStar());
        }

        // gets an arraylist of the pathfinding algorithms to be used
        // creates an arraylist of contexts
        contextsFromAlgorithmsChosen.clear();
        for (Strategy strategy : algoritmsChosen) {
            contextsFromAlgorithmsChosen.add(new Context(strategy, nodeHashMap, fromNode.getNodeID(), toNode.getNodeID(), pathFindingConditions));
        }

        pathResults.clear();
        allRouteXYZ = new double[1][3];
        int perAllRoutes = 0;
        // runs each context and places the result in a hashmap
        for (Context context : contextsFromAlgorithmsChosen) {
            double[][] path = context.runOne();
            pathResults.put(context, path);
        }

        // finds the fastest route
        fastestRoute = null;
        fastestRoute = contextsFromAlgorithmsChosen.get(0);
        for(Context context: contextsFromAlgorithmsChosen){
            if(Context.calcTotalTime(pathResults.get(context)) < Context.calcTotalTime(pathResults.get(fastestRoute))){
                fastestRoute = context;
                routeContext = context;
            }
        }
//        System.out.println("fastest route is " + fastestRoute);

        // calculates the total size
        int totalSize = 0;
        for (Context context : pathResults.keySet()) {
            totalSize += pathResults.get(context).length;
        }
        allRouteXYZ = new double[totalSize][3];
        // places all the coordinates in the new array
        int currentPlace = 0;
        for (Context context : pathResults.keySet()) {
            for (int i = 0; i < pathResults.get(context).length; i++) {
                allRouteXYZ[currentPlace][0] = pathResults.get(context)[i][0];
                allRouteXYZ[currentPlace][1] = pathResults.get(context)[i][1];
                allRouteXYZ[currentPlace][2] = pathResults.get(context)[i][2];
                currentPlace++;
            }
        }
        drawnPaths.clear();
        relocateMap();
        double[] iconOrder = getFloorIconOrder();
        for(double d : iconOrder){
            System.out.println("icon order: " + d);
        }
        populateFloorSeq(iconOrder);
        //--------This was done for robot path finding-----//
        //activateRoomberto();
        //--------Robot Path finding ends here-------------//
    }

    private void activateRoomberto() {
        // -------------ACTIVATE ROOMBERTO------------------------//
        double[][] coordsOut = new double[allRouteXYZ.length * 2][2];
        SendRoombaCommands myRoomba = new SendRoombaCommands();
        coordsOut = myRoomba.makeInstructions(allRouteXYZ);
        //System.out.println("Coords + " + coordsOut);
        String roombaDirection = myRoomba.coordsToString(coordsOut);
//        System.out.println(roombaDirection);
        myRoomba.connect(roombaDirection);
    }

    /**
     * coods are set when the label is clicked
     */
    private void doDirectionsOptions() {
        TextualDirections.setCoords(setCoords);
        // textual directions
        if (pathFindingTextual) {
            mainScreenController.textualDirectionsPane.setVisible(true);  //-----------------this should go here?
            if (mainScreenController.directionsController.textualLanguage == 1) {
                mainScreenController.textualDirectionsController.madeIcons = 0;
                Translate.url = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=";
                Translate.url += "en";
                String printDirections = "";
                String directions = TextualDirections.getDirection();
                char[] testDirections = directions.toCharArray();
                for (int g = 0; g < testDirections.length; g++) {
                    if (testDirections[g] == ('.')) {
                        printDirections += ".\n";
                    } else {
                        printDirections += testDirections[g];
                    }
                }
                mainScreenController.textualDirectionsController.displayTextualDirections(printDirections);
            } else {
                mainScreenController.textualDirectionsController.madeIcons = 1;
                Translate.url = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=";
                String printDirections = "";
                switch (mainScreenController.directionsController.textualLanguage) {
                    case 1:
                        Translate.url += "en"; // English
                        break;
                    case 2:
                        Translate.url += "es"; // Spanish
                        break;
                    case 3:
                        Translate.url += "fr"; // French
                        break;
                    case 4:
                        Translate.url += "it"; // Italian
                        break;
                    case 5:
                        Translate.url += "ru"; // Russian
                        break;
                    default:
                        Translate.url += "en";

                }
                String directions = TextualDirections.getDirection();
                printDirections = "";
                char[] testDirections = directions.toCharArray();
                for (int g = 0; g < testDirections.length; g++) {
                    if (testDirections[g] == ('.')) {
                        printDirections += ".\n";
                    } else {
                        printDirections += testDirections[g];
                    }
                }
                mainScreenController.textualDirectionsController.displayTextualDirections(printDirections); // This sets the language
            }

        }

        // speach
        if (pathFindingSpeech) {
            Thread thread = new Thread("New Thread") {
                public void run() {
                    TextToSpeech.doSpeak(TextualDirections.getDirection());
                }
            };
            thread.start();
        }
    }

    public void relocateMap() {
        Point2D location = new Point2D(6, 9);
        double[] borders = new double[4];
        borders = getRouteBorders();
        location = new Point2D(((borders[2] + borders[3]) / 2) / 5, ((borders[0] + borders[1]) / 2) / 5);
        double xWidth = (borders[3] - borders[2]);
        double yWidth = (borders[1] - borders[0]);
        double borderWidth = 0;
        if(xWidth > yWidth) {
            borderWidth = xWidth;
        }
        else {
            borderWidth = yWidth;
        }
        double zoomFactor = 4000 / borderWidth;
        if(zoomFactor < 1.8){
            zoomFactor = 1.8;
        }
        if(zoomFactor > 5){
            zoomFactor = 5;
        }

//        System.out.println("Focus info: " + zoomFactor + " " + xWidth + " " + yWidth);
        mainScreenController.gpane.centreOn(location);
        mainScreenController.gpane.zoomTo(zoomFactor, location);
//        System.out.println("Map relocated");
    }


    // would have to work per context
    // uses all coordinates
    public double[] getRouteBorders() {
//        System.out.println("Grabbing coords");
        // node to map / 5
        // map to node * 5
        double topBorder = 86000;
        double bottomBorder = 0;
        double leftBorder = 73000; //??
        double rightBorder = 0;
        for(int i = 0; i < allRouteXYZ.length; i++) {
            //System.out.println(allRouteXYZ[i][0] + " " + allRouteXYZ[i][1]);
            if(allRouteXYZ[i][1] < topBorder) {
                topBorder = allRouteXYZ[i][1];
            }
            if(allRouteXYZ[i][1] > bottomBorder) {
                bottomBorder = allRouteXYZ[i][1];
            }
            if(allRouteXYZ[i][0] < leftBorder) {
                leftBorder = allRouteXYZ[i][0];
            }
            if(allRouteXYZ[i][0] > rightBorder) {
                rightBorder = allRouteXYZ[i][0];
            }
        }
        double[] borders = new double[4];
        borders[0] = topBorder;
        borders[1] = bottomBorder;
        borders[2] = leftBorder;
        borders[3] = rightBorder;
//        System.out.println("Borders: " + topBorder + " " + bottomBorder + " " + leftBorder + " " + rightBorder);
        return borders;
    }

    //-----------------------------------------------------------------Drawing on Scene-----------------------------------------------------------//

    //
    public void drawRoute(Context context, double[][] route){
        Image upArrow = null;
        Image dwArrow = null;
        myImgAnchor.getChildren().clear();
        routeFloor = (int)route[0][2];
        int count = 0;
        int startFloor = 0;
        group4 = new Group();
        group3 = new Group();
        group2 = new Group();
        group1 = new Group();
        group0 = new Group();
        group11 = new Group();
        group22 = new Group();

        for(int i = 0; i < route.length-1; i++){
            startFloor = (int) route[0][2];

            if(route[i][2] == 4){
                Line line = addLine(route[i][0],route[i][1],route[i+1][0],route[i+1][1], context);
                group4.getChildren().add(line);
            } else if(route[i][2] == 3){
                Line line = addLine(route[i][0],route[i][1],route[i+1][0],route[i+1][1], context);
                group3.getChildren().add(line);
            } else if(route[i][2] == 2){
                Line line = addLine(route[i][0],route[i][1],route[i+1][0],route[i+1][1], context);
                group2.getChildren().add(line);
            } else if(route[i][2] == 1){
                Line line = addLine(route[i][0],route[i][1],route[i+1][0],route[i+1][1], context);
                group1.getChildren().add(line);
            } else if(route[i][2] == 0){
                Line line = addLine(route[i][0],route[i][1],route[i+1][0],route[i+1][1], context);
                group0.getChildren().add(line);
            } else if(route[i][2] == -1){
                Line line = addLine(route[i][0],route[i][1],route[i+1][0],route[i+1][1], context);
                group11.getChildren().add(line);
            } else if(route[i][2] == -2){
                Line line = addLine(route[i][0],route[i][1],route[i+1][0],route[i+1][1], context);
                group22.getChildren().add(line);
            }
            count++;
        }

        drawStartEndUpDown(route);
        if(mainScreenController.sanitationController.isBiohazard){
            for(Node n : mainScreenController.sanitationController.bhNodeHM.values())
            mainScreenController.sanitationController.addBioHazIcon(n);
        }

        // localizes the variable IDS to avoid duplicate children in the anchorpane
        Group group4L = group4;
        Group group3L = group3;
        Group group2L = group2;
        Group group1L = group1;
        Group group0L = group0;
        Group group11L = group11;
        Group group22L = group22;

        // store the current groups, would have to add methods to swap groups to show a different path
        // not sure how to show multiple though
        ArrayList<Group> storedGroups = new ArrayList<>();

        storedGroups.add(0,group4L);
        storedGroups.add(1,group3L);
        storedGroups.add(2,group2L);
        storedGroups.add(3,group1L);
        storedGroups.add(4,group0L);
        storedGroups.add(5,group11L);
        storedGroups.add(6,group22L);
        // then to the hashmap
        drawnPaths.put(context, storedGroups);
        // changing groups does change what is displayed
        // since there can only be one result per algorithm, an array grouping structure isn't too bad an idea

//        System.out.println("Pathfinding created " + count + " lines...wow!");
        mainScreenController.changeFloorView(startFloor);
        mainScreenController.floorSlider.setValue(startFloor);
    }

    Line addLine(double xs, double ys, double xf, double yf, Context context){
        Line line = new Line(xs/5,ys/5,xf/5,yf/5);
        // color depends on the algorithm
        if(context.getStrategy().equals(new AStar())){
            line.setStroke(Color.RED);
        }
        if(context.getStrategy().equals(new BestFirst())){
            line.setStroke(Color.FORESTGREEN);
        }
        if(context.getStrategy().equals(new BreadthFirst())){
            line.setStroke(Color.BLUE);
        }
        if(context.getStrategy().equals(new DepthFirst())){
            line.setStroke(Color.ORANGE);
        }
        if(context.getStrategy().equals(new Dijkstra())){
            line.setStroke(Color.HOTPINK);
        }
        if(context.getStrategy().equals(new GrandTour())){
            line.setStroke(Color.GOLD);
        }
        line.setOpacity(0.25); //
        if(fastestRoute.getStrategy().equals(context.getStrategy())){ // the route is the fastest
            line.setOpacity(1);
            line.toFront();
        }
        line.setStrokeWidth(2);
        line.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        //  line.setStrokeWidth(2);

        final double maxOffset =
                line.getStrokeDashArray().stream()
                        .reduce(
                                0d,
                                (a, b) -> a + b
                        );
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(line.strokeDashOffsetProperty(),0, Interpolator.LINEAR)
                ),
                new KeyFrame(
                        Duration.seconds(5),
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                maxOffset,
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setRate(-3);
        timeline.play();

        // could add the events to lines here, but can't make those events aware


        return line;
    }

    public void addStartEndLabels(int floor, boolean start){
        if(start){
            String startNodeName = fromNode.getLongName();
            Label startLabel = new Label(startNodeName);
            startLabel.setStyle("-fx-font-weight: bold; -fx-opacity: 0.8");
            startLabel.setLayoutX((fromNode.getXCoord()/5) + 10);
            startLabel.setLayoutY((fromNode.getYCoord()/5) - 10);

            addLabelToGroup(floor, startLabel);
        } else {
            String endNodeName = toNode.getLongName();
            Label endLabel = new Label(endNodeName);
            endLabel.setStyle("-fx-font-weight: bold; -fx-opacity: 0.8");
            endLabel.setLayoutX((toNode.getXCoord()/5) + 10);
            endLabel.setLayoutY((toNode.getYCoord()/5) - 10);

            addLabelToGroup(floor, endLabel);
        }

    }


    public void drawStartEndUpDown(double[][] route){
        for(int i = 0; i < route.length-1; i++) {
            if (i == 0 && route.length > 2) {
                int startFloor = (int) route[i][2];
                Circle startCircle = addStartEndCircle(route[i][0], route[i][1], true);
                addCircleToGroup(startFloor, startCircle);
                addStartEndLabels(startFloor, true);
            }
            if (i == route.length - 2) {
                int endFloor = (int) route[i + 1][2];
                Circle endCircle = addStartEndCircle(route[i + 1][0], route[i + 1][1], false);
                addCircleToGroup(endFloor, endCircle);
                if(route.length > 2){
                    addStartEndLabels(endFloor, false);
                }
            }

            if (route[i][2] < route[i + 1][2]) {
                int upFloor = (int) route[i][2];
                int downFloor = (int) route[i + 1][2];
                drawAllUpDownImages(true, route[i][0], route[i][1], upFloor, downFloor);

            }
            if (route[i][2] > route[i + 1][2]) {
                int downFloor = (int) route[i][2];
                int upFloor = (int) route[i + 1][2];
                drawAllUpDownImages(false, route[i][0], route[i][1], upFloor, downFloor);
            }
        }
    }

    public void drawAllUpDownImages(boolean up, double x, double y, int upFloor, int downFloor){
        if(up){
            drawUpArrowImageView(upFloor, downFloor, x, y);
            drawDownArrowImageView(downFloor, upFloor, x, y);
        } else {
            drawDownArrowImageView(downFloor, upFloor, x, y);
            drawUpArrowImageView(upFloor, downFloor, x, y);
        }
    }

    public Circle addStartEndCircle(double x, double y, boolean start) {
        if (start) {
            Circle startCircle = new Circle(8);
            startCircle.setCenterX(x / 5);
            startCircle.setCenterY(y / 5);
            startCircle.setFill(Color.GREEN);
            return startCircle;
        } else {
            Circle endCircle = new Circle(8);
            endCircle.setCenterX(x / 5);
            endCircle.setCenterY(y / 5);
            endCircle.setFill(Color.RED);
            return endCircle;
        }
    }

    public void addCircleToGroup(int floor, Circle circle){
        if(floor == 4){
            group4.getChildren().add(circle);
        } else if(floor == 3){
            group3.getChildren().add(circle);
        } else if(floor == 2){
            group2.getChildren().add(circle);
        } else if(floor == 1){
            group1.getChildren().add(circle);
        } else if(floor == 0){
            group0.getChildren().add(circle);
        } else if(floor == -1){
            group11.getChildren().add(circle);
        } else if(floor == -2){
            group22.getChildren().add(circle);

        }
    }


    public void addImageViewToGroup(int floor, ImageView imageView){
        if(floor == 4){
            group4.getChildren().add(imageView);
        } else if(floor == 3){
            group3.getChildren().add(imageView);
        } else if(floor == 2){
            group2.getChildren().add(imageView);
        } else if(floor == 1){
            group1.getChildren().add(imageView);
        } else if(floor == 0){
            group0.getChildren().add(imageView);
        } else if(floor == -1){
            group11.getChildren().add(imageView);
        } else if(floor == -2){
            group22.getChildren().add(imageView);
        }
    }

    public void addLabelToGroup(int floor, Label label){
        if(floor == 4){
            group4.getChildren().add(label);
        } else if(floor == 3){
            group3.getChildren().add(label);
        } else if(floor == 2){
            group2.getChildren().add(label);
        } else if(floor == 1){
            group1.getChildren().add(label);
        } else if(floor == 0){
            group0.getChildren().add(label);
        } else if(floor == -1){
            group11.getChildren().add(label);
        } else if(floor == -2){
            group22.getChildren().add(label);
        }
    }

    public void drawUpArrowImageView(int startFloor, int endFloor, double x, double y){

        ImageView imageView = new ImageView();


        switch(endFloor){
            case -1:
                imageView = new ImageView(upL1);
                break;
            case 0:
                imageView = new ImageView(upG);
                break;
            case 1:
                imageView = new ImageView(up1);
                break;
            case 2:
                imageView = new ImageView(up2);
                break;
            case 3:
                imageView = new ImageView(up3);
                break;
            case 4:
                imageView = new ImageView(up4);
                break;
        }
        imageView.setOpacity(0.9);

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(16);
        imageView.setX((x / 5) - 8);
        imageView.setY((y / 5) - 8);

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainScreenController.changeFloorView(endFloor);
                mainScreenController.floorSlider.setValue(endFloor);
            }
        });

        addImageViewToGroup(startFloor, imageView);
    }

    public void drawDownArrowImageView(int startFloor, int endFloor, double x, double y){

        ImageView imageView = new ImageView();


        switch(endFloor){
            case -2:
                imageView = new ImageView(downL2);
                break;
            case -1:
                imageView = new ImageView(downL1);
                break;
            case 0:
                imageView = new ImageView(downG);
                break;
            case 1:
                imageView = new ImageView(down1);
                break;
            case 2:
                imageView = new ImageView(down2);
                break;
            case 3:
                imageView = new ImageView(down3);
                break;
        }
        imageView.setOpacity(0.9);

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(16);
        imageView.setX((x / 5) - 8);
        imageView.setY((y / 5) - 8);

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainScreenController.changeFloorView(endFloor);
                mainScreenController.floorSlider.setValue(endFloor);
            }
        });

        addImageViewToGroup(startFloor, imageView);
    }

    void addYAHIcon(ImageView img, int floor){
        PathFindingTemplate.buildFloorMap();
        // places based on floor
        if(PathFindingTemplate.getFloorMap().get(mainScreenController.curLocationNode.getFloor()) == floor){
            mainScreenController.imgAnchor.getChildren().add(img);
        }
    }

    public void resetGroups(){
        group4.getChildren().clear();
        group3.getChildren().clear();
        group2.getChildren().clear();
        group1.getChildren().clear();
        group0.getChildren().clear();
        group11.getChildren().clear();
        group22.getChildren().clear();
    }

    public void loadAllImages(){
        up4 = new Image("/Images/Up_Arrow_4.png");
        up3 = new Image("/Images/Up_Arrow_3.png");
        up2 = new Image("/Images/Up_Arrow_2.png");
        up1 = new Image("/Images/Up_Arrow_1.png");
        upG = new Image("/Images/Up_Arrow_G.png");
        upL1 = new Image("/Images/Up_Arrow_L1.png");

        down3 = new Image("/Images/Down_Arrow_3.png");
        down2 = new Image("/Images/Down_Arrow_2.png");
        down1 = new Image("/Images/Down_Arrow_1.png");
        downG = new Image("/Images/Down_Arrow_G.png");
        downL1 = new Image("/Images/Down_Arrow_L1.png");
        downL2 = new Image("/Images/Down_Arrow_L2.png");

        floor4 = new Image("/Images/floor_4.png");
        floor3 = new Image("/Images/floor_3.png");
        floor2 = new Image("/Images/floor_2.png");
        floor1 = new Image("/Images/floor_1.png");
        floorG = new Image("/Images/floor_G.png");
        floorL1 = new Image("/Images/floor_L1.png");
        floorL2 = new Image("/Images/floor_L2.png");

    }

        public double[] getFloorIconOrder() {
            System.out.println("Making icons");
            double[][] routeCoords = allRouteXYZ;
            double floorIcons[] = new double[routeCoords.length];
            double startCoords[] = new double[3];
            int count = 0;
            floorIcons[count] = routeCoords[count][2];
            count++;
            double oldFloor = routeCoords[count][2];
            startCoords[0] = routeCoords[0][0];
            startCoords[1] = routeCoords[0][1];
            startCoords[2] = routeCoords[0][2];
            for (int i = 1; i < routeCoords.length; i++) {
                if(routeCoords[i][0] == startCoords[0] && routeCoords[i][1] == startCoords[1] && routeCoords[i][2] == startCoords[2]) {
                    double multiFloor[] = new double[1];
                    multiFloor[0] = 20;
                    return multiFloor;
                }

                if (routeCoords[i][2] != oldFloor) {
                    floorIcons[count] = routeCoords[i][2];
                    oldFloor = routeCoords[i][2];
                    count++;
                }
            }
            double finalFloorIcons[] = new double[count];
            for (int k = 0; k < count; k++) {
                finalFloorIcons[k] = floorIcons[k];
                System.out.println(finalFloorIcons[k] + " Final Floor");
            }
            getFloorIconCalled = true;
            System.out.println("Made true");
            return finalFloorIcons;
        }

        public void populateFloorSeq(double[] floorOder){
            mainScreenController.iconsHBox.getChildren().clear();
            for(double d : floorOder){
                mainScreenController.floorSeqHBox.setVisible(true);
                ImageView floorIcon = new ImageView();
                if(d == 4){
                    floorIcon.setImage(floor4);
                    floorIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainScreenController.changeFloorView(4);
                            mainScreenController.floorSlider.setValue(4);
                        }
                    });
                } else if(d == 3){
                    floorIcon.setImage(floor3);
                    floorIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainScreenController.changeFloorView(3);
                            mainScreenController.floorSlider.setValue(3);
                        }
                    });
                } else if(d == 2){
                    floorIcon.setImage(floor2);
                    floorIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainScreenController.changeFloorView(2);
                            mainScreenController.floorSlider.setValue(2);
                        }
                    });
                } else if(d == 1){
                    floorIcon.setImage(floor1);
                    floorIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainScreenController.changeFloorView(1);
                            mainScreenController.floorSlider.setValue(1);
                        }
                    });
                } else if(d == 0){
                    floorIcon.setImage(floorG);
                    floorIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainScreenController.changeFloorView(0);
                            mainScreenController.floorSlider.setValue(0);
                        }
                    });
                } else if(d == -1){
                    floorIcon.setImage(floorL1);
                    floorIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainScreenController.changeFloorView(-1);
                            mainScreenController.floorSlider.setValue(-1);
                        }
                    });
                } else if(d == -2){
                    floorIcon.setImage(floorL2);
                    floorIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            mainScreenController.changeFloorView(-2);
                            mainScreenController.floorSlider.setValue(-2);
                        }
                    });

                } else if(d == 20){
                    System.out.println("Multiple routes selected: dont show");
                    mainScreenController.floorSeqHBox.setVisible(false);
                }
                floorIcon.setFitWidth(80);
                floorIcon.setPreserveRatio(true);
                mainScreenController.iconsHBox.getChildren().add(floorIcon);

            }
        }

    }