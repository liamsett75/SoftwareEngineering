package UIControllers;

import Databases.EdgeDB;
import Databases.NodeDB;
import FileReaders.FileReader;
import Graph.Node;
import PathFinding.Filter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class NodeInfoController implements Initializable {

    @FXML JFXTextField nodeIDTF;
    @FXML JFXTextField xCoordTF;
    @FXML JFXTextField yCoordTF;
    @FXML JFXTextField floorTF;
    @FXML JFXTextField buildingTF;
    @FXML JFXTextField nodeTypeTF;
    @FXML JFXTextField longNameTF;
    @FXML JFXTextField shortNameTF;
    @FXML JFXButton btnEdit;
    @FXML JFXButton btnDelete;
    @FXML JFXButton btnKiosk;
    @FXML Label errorLabel;
    @FXML HBox editHBox;
    @FXML HBox addHBox;
    @FXML JFXButton btnAdd;

    String nodeID, building, nodeType, longName, shortName, floor;
    int element;
    double xCoord, yCoord;

    boolean goodTF, goodNumbers;

    Node selectedNode, delNode, addNode;

    @FXML
    MainScreenController mainScreenController;

    public NodeInfoController(){
    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addHBox.setVisible(false);
        editHBox.setVisible(true);
    }

    @FXML
    public void editOnClick(){
        if (getTextFieldEntries() && validNode()) {
            Node newNode = new Node(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName);
            element = 0;
            LinkedList<Node> fileReaderNodeList = NodeDB.getDBNodes();
            for (Node n : fileReaderNodeList) {
                if (n.getNodeID().equals(nodeID)) {
                    delNode = n;
                    break;
                }
                element++;
            }
            try {
                NodeDB.editNodeDB(delNode, newNode);
                NodeDB.createHashMapDB();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            errorLabel.setText("Node Edited!");
            mainScreenController.adminController.refresh();
            mainScreenController.adminController.showNodesOnClick();
            mainScreenController.nodeInfoPane.setVisible(true);
            btnDelete.setDisable(false);
            btnEdit.setDisable(false);
        } else {
            errorLabel.setText("Entered properties are not valid");
        }
    }

    @FXML
    public void deleteOnClick(){
        if (getTextFieldEntries() && validNode()) {
            element = 0;
            LinkedList<Node> fileReaderNodeList = NodeDB.getDBNodes();

            for (Node n : fileReaderNodeList) {
                if (n.getNodeID().equals(nodeID)) {
                    delNode = n;
                    break;
                }
                element++;
            }

            try {
//                System.out.println("length of list before delete " + NodeDB.getDBNodes().size());
                NodeDB.deleteNodeDB(delNode);
//                System.out.println("length of list after delete " + NodeDB.getDBNodes().size());
//                System.out.println("node of id " + delNode.getNodeID() + " has been deleted");
                EdgeDB.removeHashMapDBNeighbors();
                EdgeDB.deleteEdgeWithNodeID(delNode.getNodeID());
//                System.out.println("after remove hashmap db neightnors");

                Filter.setLinkedNodes(NodeDB.getDBNodes());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            errorLabel.setText("Node Deleted!");
            clearTF();
            mainScreenController.adminController.refresh();
            mainScreenController.adminController.showNodesOnClick();
            mainScreenController.nodeInfoPane.setVisible(true);
        } else {
            errorLabel.setText("Entered properties are not valid");
        }

    }

    @FXML
    public void addOnClick(){
        if(getTextFieldEntries() && validNode()){
            Node newNode = new Node(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName);
            if(!nodeIDExists(nodeID)){
                LinkedList<Node> nodeList = NodeDB.getDBNodes();
                nodeList.add(newNode);

                try{
                    NodeDB.addNodeDB(newNode);
                    errorLabel.setText("Node Added!");
                    clearTF();
                    mainScreenController.adminController.refresh();
                    mainScreenController.adminController.showNodesOnClick();
//                    mainScreenController.directionsController.updateComboBox();
                    mainScreenController.nodeInfoPane.setVisible(true);

                    Filter.setLinkedNodes(NodeDB.getDBNodes());

                }catch (SQLException e){
                    e.printStackTrace();
                }
            } else {
                errorLabel.setText("NodeID is already in use");
            }


        } else{
            errorLabel.setText("Non-valid Node Properties");
        }

    }


    public void setNodeXYZ(Point2D point, int floor){
        double posX = point.getX()*5;
        double posY = point.getY()*5;
//        mainScreenController.adminController.displayNodeInfo(addNode);
//        clearTF();
        floorTF.setText(Integer.toString(floor));
        xCoordTF.setText(Double.toString(posX));
        yCoordTF.setText(Double.toString(posY));

    }

    public void displayNodeInfo(Node node){
        nodeIDTF.setText(node.getNodeID());
        xCoordTF.setText(Double.toString(node.getXCoord()));
        yCoordTF.setText(Double.toString(node.getYCoord()));
        floorTF.setText(node.getFloor());
        buildingTF.setText(node.getBuilding());
        nodeTypeTF.setText(node.getNodeType());
        longNameTF.setText(node.getLongName());
        shortNameTF.setText(node.getShortName());
    }

    public void onlyDigits(TextField tf){
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public boolean getTextFieldEntries() {
        goodNumbers = true;
        try {
            nodeID = nodeIDTF.getText();
            xCoord = Double.parseDouble(xCoordTF.getText());
            yCoord = Double.parseDouble(yCoordTF.getText());
            floor = floorTF.getText();
            if (floor.equals("0")){
                floor = "G";
            } else if (floor.equals("-1")){
                floor = "L1";
            } else if (floor.equals("-2")) {
                floor = "L2";
            }
            building = buildingTF.getText();
            nodeType = nodeTypeTF.getText();
            longName = longNameTF.getText();
            shortName = shortNameTF.getText();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            goodNumbers = false;
        }
        return goodNumbers;
    }

    /**
     * Helper method to check if the user entered data in the text fields meets some sort of level of valid input or
     * do not crash the program.
     * @return
     */

    public boolean validNode() {
        goodTF = true;
        if(nodeIDTF.getText().length() == 0 || nodeIDTF.getText().length() > 10){
            goodTF = false;
        }
        if (((Double.parseDouble(xCoordTF.getText())) > 6500) || ((Double.parseDouble(xCoordTF.getText())) <= 0)) {
            goodTF = false;
        }
        if (((Double.parseDouble(yCoordTF.getText())) > 3400) || ((Double.parseDouble(yCoordTF.getText())) <= 0)) {
            goodTF = false;
        }
        if (((Integer.parseInt(floorTF.getText())) > 4) || ((Integer.parseInt(floorTF.getText())) < -2)) {
            goodTF = false;
        }
        if (buildingTF.getText().length() == 0 || buildingTF.getText().length() > 20) {
            goodTF = false;
        }
        if (nodeTypeTF.getText().length() == 0 || nodeTypeTF.getText().length() > 4) {
            goodTF = false;
        }
        if (longNameTF.getText().length() == 0 || longNameTF.getText().length() > 100) {
            goodTF = false;
        }
        if (shortNameTF.getText().length() == 0 || shortNameTF.getText().length() > 50) {
            goodTF = false;
        }
        return goodTF;
    }

    public boolean nodeIDExists(String nodeID){
        if(NodeDB.getNodeHashMap().containsKey(nodeID)){
            return true;
        } else {
            return false;
        }
    }

    public void setDisableTF(boolean value){
        xCoordTF.setDisable(value);
        yCoordTF.setDisable(value);
        floorTF.setDisable(value);
        buildingTF.setDisable(value);
        nodeTypeTF.setDisable(value);
        longNameTF.setDisable(value);
        shortNameTF.setDisable(value);
    }

    public void clearTF(){
        nodeIDTF.setText("");
        xCoordTF.setText("");
        yCoordTF.setText("");
        floorTF.setText("");
        buildingTF.setText("");
        nodeTypeTF.setText("");
        longNameTF.setText("");
        shortNameTF.setText("");
    }

    @FXML
    public void kioskOnClick(){
        String nodeID = nodeIDTF.getText();
//        System.out.println("New Kiosk ID: " + nodeID);
        errorLabel.setText("New Kiosk Location");
        mainScreenController.setNewCurrentLocationNode(nodeID);
        mainScreenController.adminController.showNodesOnClick();
    }

    //add the add button


}
