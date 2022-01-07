package UIControllers;

import Databases.EdgeDB;
import Databases.NodeDB;
import FileReaders.EdgeReader;
import FileWriters.CSVMaker;
import Graph.Edge;
import Graph.Node;
import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML ToggleButton btnShowNodes;
    @FXML ToggleButton btnShowEdges;
    @FXML JFXButton btnEmployees;
    @FXML JFXButton btnAddNode;
    @FXML JFXButton btnAddEdge;
    @FXML JFXButton btnDownloadAll;

    Image biohazardImage;

    boolean editingEdge;
    boolean kioskNode;

    HashMap<String, Node> nodeHashMap = new HashMap<>();

    public void setEdgeList(LinkedList<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    LinkedList<Edge> edgeList = new LinkedList<>();

    @FXML
    MainScreenController mainScreenController;

    public AdminController(){

    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        biohazardImage = new Image("/Images/biohazard_image.png");
        btnShowEdges.setTooltip(new Tooltip("Show Edges"));
        btnShowNodes.setTooltip(new Tooltip("Show Nodes"));
        btnAddEdge.setTooltip(new Tooltip("Add an Edge"));
        btnAddNode.setTooltip(new Tooltip("Add a Node"));
        btnEmployees.setTooltip(new Tooltip("View All Employees"));

        EdgeDB.createEdgeTable(); //this works here, but is bad practice having it here
        NodeDB.createHashMapDB();
        EdgeDB.populateHashMapDB();
        nodeHashMap = EdgeDB.getNodeHashMap();

        readOnClick();

    }

    public void readOnClick(){
        EdgeReader er = new EdgeReader();
        er.readFile("edgesv4.csv");
//        System.out.println("file has been read");
        edgeList = EdgeDB.getDBEdges();
//        System.out.println("read on click has been EDGES");

    }

    public void refresh(){
        //NodeDB.createHashMapDB();
        EdgeDB.populateHashMapDB();

        nodeHashMap = EdgeDB.getNodeHashMap();

        mainScreenController.imgAnchor.getChildren().clear();
        mainScreenController.changeFloorView(mainScreenController.floor);

//        System.out.println("finsihed refresh");
    }

    @FXML
    public void employeesOnClick() throws IOException {
        EmployeeController.setMainScreenParent(mainScreenController.welcomeController.getMainScreenParent());
        Parent parent = FXMLLoader.load(getClass().getResource("/Scenes/EmployeeScene.fxml"));
        Scene scene = mainScreenController.userNameBar.getScene();
        scene.setRoot(parent);

    }

    @FXML
    public void addNodeOnClick(){
        mainScreenController.nodeInfoController.errorLabel.setText("");
        mainScreenController.nodeInfoController.addHBox.setVisible(true);
        mainScreenController.nodeInfoController.editHBox.setVisible(false);
        mainScreenController.nodeInfoController.clearTF();
        mainScreenController.nodeInfoPane.setVisible(true);
        mainScreenController.edgeInfoPane.setVisible(false);
        mainScreenController.nodeInfoController.nodeIDTF.setDisable(false);
    }

    @FXML
    public void addEdgeOnClick(){
        editingEdge = true;
        mainScreenController.edgeInfoController.errorLabel.setText("");
        mainScreenController.edgeInfoController.addHBox.setVisible(true);
        mainScreenController.edgeInfoController.editHBox.setVisible(false);
        mainScreenController.edgeInfoController.clearTF();
        mainScreenController.edgeInfoPane.setVisible(true);
        mainScreenController.nodeInfoPane.setVisible(false);
    }

    @FXML
    public void showNodesOnClick(){

        if(!btnShowNodes.isSelected()){
            mainScreenController.imgAnchor.getChildren().clear();
            if(btnShowEdges.isSelected()) {showEdgesOnClick();}
        } else {
            mainScreenController.edgeInfoPane.setVisible(false);
            mainScreenController.nodeInfoPane.setVisible(false);
            String floor = Integer.toString(mainScreenController.floor);
            if (floor.equals("0")) {
                floor = "G";
            } else if (floor.equals("-1")) {
                floor = "L1";
            } else if (floor.equals("-2")) {
                floor = "L2";
            }

            for(Node bh: mainScreenController.sanitationController.bhNodeHM.values()) {
                if(bh.getFloor().equals(floor)){
                    mainScreenController.sanitationController.addBioHazIcon(bh);
                    ImageView biohazard = new ImageView(biohazardImage);
                    biohazard.setFitWidth(18);
                    biohazard.setPreserveRatio(true);
                    biohazard.setLayoutX((bh.getXCoord() / 5) - 9);
                    biohazard.setLayoutY((bh.getYCoord() / 5) - 9);
                    biohazard.setUserData(bh);
                    biohazard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
//                            System.out.println("you clicked the biohazard: " + bh.getNodeID());

                        }
                    });
                    mainScreenController.imgAnchor.getChildren().add(biohazard);

                }
            }

            for (Node n : nodeHashMap.values()) {

                if (n.getFloor().equals(floor)) {
                    Circle circleNode = new Circle();
                    circleNode.setUserData(n);
                    circleNode.setCenterX((n.getXCoord() / 5));
                    circleNode.setCenterY((n.getYCoord() / 5));

                    if(n.getNodeID().equals(mainScreenController.curLocationNode.getNodeID())){
//                        System.out.println("Kiosk node: " + n.getNodeID());
                        circleNode.setRadius(5);
                        circleNode.setFill(Color.RED);
                    } else if (n.getNodeType().equals("HALL") || n.getNodeType().equals("WALK")) {
                        circleNode.setRadius(2);
                        circleNode.setFill(Color.LIGHTBLUE);
                    } else {
                        circleNode.setRadius(4);
                        circleNode.setFill(Color.MIDNIGHTBLUE);
                    }
                    mainScreenController.imgAnchor.getChildren().add(circleNode);

                    //---------------------------------------------------------------------Event listeners for nodes
                    circleNode.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
//                            System.out.println("MOUSE IS PRESSED-------------");
//                            System.out.println("You clicked___" + n.getLongName());
                            kioskNode = false;
                            if(circleNode.getRadius() == 5){
//                                System.out.println("Kiosk Node");
                                kioskNode = true;
                            }
                            if (mainScreenController.edgeInfoPane.isVisible() && editingEdge) {
                                mainScreenController.edgeInfoController.setEdgeIDs(n.getNodeID());
                            } else {
                                displayNodeInfo(n);
                                mainScreenController.nodeInfoController.nodeIDTF.setDisable(true);
                            }
                            mainScreenController.gpane.setGestureEnabled(false);
//                            lastXCoord = t.getX()*5;
//                            lastYCoord = t.getY()*5;
                            if(kioskNode){
                                mainScreenController.nodeInfoController.errorLabel.setText("Cannot edit a current kiosk location");
                                mainScreenController.nodeInfoController.setDisableTF(true);
                                mainScreenController.nodeInfoController.btnEdit.setDisable(true);
                                mainScreenController.nodeInfoController.btnDelete.setDisable(true);
                                mainScreenController.nodeInfoController.btnKiosk.setDisable(true);
                            } else {
                                mainScreenController.nodeInfoController.errorLabel.setText("");
                                mainScreenController.nodeInfoController.setDisableTF(false);
                                mainScreenController.nodeInfoController.btnEdit.setDisable(false);
                                mainScreenController.nodeInfoController.btnDelete.setDisable(false);
                                mainScreenController.nodeInfoController.btnKiosk.setDisable(false);
                            }
                        }
                    });

                    circleNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            mainScreenController.gpane.setGestureEnabled(true);
                            if(!editingEdge && !kioskNode){
//                                System.out.println("MOUSE RELEASED-----------------");
                                mainScreenController.nodeInfoController.xCoordTF.setText(Long.toString(Math.round(t.getX()*5))); // *5 because i am setting the node coords in the tf when it needs to be correct
                                mainScreenController.nodeInfoController.yCoordTF.setText(Long.toString(Math.round(t.getY()*5)));
                                if(mainScreenController.nodeInfoController.floorTF.getText().equals("G")){
                                    mainScreenController.nodeInfoController.floorTF.setText("0");
                                } else if(mainScreenController.nodeInfoController.floorTF.getText().equals("L1")){
                                    mainScreenController.nodeInfoController.floorTF.setText("-1");
                                } else if(mainScreenController.nodeInfoController.floorTF.getText().equals("L2")){
                                    mainScreenController.nodeInfoController.floorTF.setText("-2");
                                }

                                mainScreenController.nodeInfoController.editOnClick();
                            }
                        }
                    });



                    circleNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            if(!editingEdge && !kioskNode){
//                                System.out.println("MOUSE IS BEING DRAGGED");
                                ((Circle)(t.getSource())).setCenterX(t.getX()); //this is to display the node while dragging it. that's why no *5
                                ((Circle)(t.getSource())).setCenterY(t.getY());
                            }
                        }
                    });
                }
            }
        }
    }





    public void displayNodeInfo(Node node){
        mainScreenController.nodeInfoController.errorLabel.setText("");
        mainScreenController.nodeInfoController.clearTF();
        mainScreenController.nodeInfoController.editHBox.setVisible(true);
        mainScreenController.nodeInfoController.addHBox.setVisible(false);
        mainScreenController.nodeInfoController.selectedNode = node;
        mainScreenController.nodeInfoController.displayNodeInfo(node); //my bad on the names the of the method haha
        mainScreenController.nodeInfoController.setDisableTF(false);
        mainScreenController.nodeInfoController.nodeIDTF.setDisable(true); //changing nodeID of an existing node breaks everything
        mainScreenController.nodeInfoController.btnDelete.setDisable(false);
        mainScreenController.nodeInfoController.btnEdit.setDisable(false);
        mainScreenController.nodeInfoController.btnAdd.setDisable(false);
        try{
            mainScreenController.nodeInfoPane.setVisible(true);
            mainScreenController.edgeInfoPane.setVisible(false);

        } catch (IllegalArgumentException e){

        }

    }

    @FXML
    public void editOnClick(){

    }

    @FXML
    public void deleteOnClick(){

    }

    @FXML
    public void showEdgesOnClick(){
//        editingEdge = false;

        if(!btnShowEdges.isSelected()){
            mainScreenController.imgAnchor.getChildren().clear();
            if(btnShowNodes.isSelected()){showNodesOnClick();} //redisplay nodes if they were already being displayed
        } else {
            mainScreenController.edgeInfoPane.setVisible(false);
            mainScreenController.nodeInfoPane.setVisible(false);
            edgeList = EdgeDB.getDBEdges();

            for (Edge e : edgeList) {
                boolean bhNodeChecker = true;
                String startNodeID = e.getStartNodeID();
                String endNodeID = e.getEndNodeID();
                Node startNode = nodeHashMap.get(startNodeID);
                Node endNode = nodeHashMap.get(endNodeID);
                String floor = Integer.toString(mainScreenController.floor);
                if (floor.equals("0")) {
                    floor = "G";
                } else if (floor.equals("-1")) {
                    floor = "L1";
                } else if (floor.equals("-2")) {
                    floor = "L2";
                }
                //if node hash map contains the node of the start id of the edge

                for(Node n: mainScreenController.sanitationController.bhNodeHM.values()) {
                    if(e.getEdgeID().contains(n.getNodeID())) {
                        bhNodeChecker = false; //will not allow edges connected to this bh node to be drawn
                    }
                }
//                System.out.println("----------------------------------------");
//                System.out.println(startNode.getNodeID());
//                System.out.println(e.getEdgeID());
//                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                if (bhNodeChecker && startNode.getFloor().equals(floor)) {
                    Line line = new Line(startNode.getXCoord() / 5, startNode.getYCoord() / 5, endNode.getXCoord() / 5, endNode.getYCoord() / 5);
                    line.setUserData(e);
                    line.setStrokeWidth(1.5);
                    line.setStroke(Color.GOLDENROD);

                    line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
//                            System.out.println("You clicked edge____" + e.getEdgeID());
                            displayEdgeInfo(e);

                        }
                    });
                    mainScreenController.imgAnchor.getChildren().add(line);
                }
            }
            if(btnShowNodes.isSelected()){showNodesOnClick();} //display nodes over edges always
        }

    }

    public void displayEdgeInfo(Edge edge){
        try{
            mainScreenController.edgeInfoController.errorLabel.setText("");
            mainScreenController.edgeInfoController.clearTF();
            mainScreenController.edgeInfoController.addHBox.setVisible(false);
            mainScreenController.edgeInfoController.editHBox.setVisible(true);
            mainScreenController.edgeInfoPane.setVisible(true);
            mainScreenController.nodeInfoPane.setVisible(false);
            mainScreenController.edgeInfoController.btnAdd.setDisable(false);
            mainScreenController.edgeInfoController.btnDelete.setDisable(false);
            mainScreenController.edgeInfoController.selectedEdge = edge;
            mainScreenController.edgeInfoController.displayEdgeInfo(edge);
        } catch (IllegalArgumentException e){

        }
    }

    @FXML
    public void downloadAllOnClick(){
        LinkedList<Node> nodeList = NodeDB.getDBNodes();
        CSVMaker.makeNodesFile(nodeList, "NewNodeList.csv");
        LinkedList<Edge> edgeList = EdgeDB.getDBEdges();
        CSVMaker.makeEdgesFile(edgeList, "NewEdgeList.csv");
    }


}
