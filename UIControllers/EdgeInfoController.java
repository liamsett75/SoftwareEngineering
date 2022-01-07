package UIControllers;

import Databases.EdgeDB;
import Graph.Edge;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class EdgeInfoController implements Initializable {

    @FXML Label edgeIDLabel;
    @FXML Label startNodeIDLabel;
    @FXML Label endNodeIDLabel;
    @FXML JFXButton btnDelete;
    @FXML Label errorLabel;
    @FXML HBox editHBox;
    @FXML HBox addHBox;
    @FXML JFXButton btnAdd;

    Edge selectedEdge;

    boolean hasEdge = false;
    boolean switchEdge = true;
    //String nodeID, building, nodeType, longName, shortName, floor;
    int element;
    //double xCoord, yCoord;
    //Node curNode, delNode;
    String edgeID, startNode, endNode;
    Edge curEdge, delEdge;
    Boolean goodTF, goodNumbers;

    @FXML
    MainScreenController mainScreenController;

    public EdgeInfoController(){
    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addHBox.setVisible(false);
        editHBox.setVisible(true);
    }


    public void editOnClick() throws NumberFormatException{
        if (validEdge() && getTextFieldEntriesEdges()) {
            Edge newEdge = new Edge(edgeID, startNode, endNode);

            element = 0;
            LinkedList<Edge> fileReaderEdgeList = EdgeDB.getDBEdges();
            for (Edge ed : fileReaderEdgeList) {
                if (ed.getEdgeID().equals(edgeID)) {
                    delEdge = ed;
                    break;
                }
                element++;
            }

            try {
                EdgeDB.editEdgeDB(delEdge, newEdge);
                //EdgeDB.populateHashMapDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            errorLabel.setText("Edge Edited!");
            btnDelete.setDisable(false);

            mainScreenController.adminController.refresh();
            mainScreenController.adminController.showEdgesOnClick();
            mainScreenController.edgeInfoPane.setVisible(true);

        } else {
            errorLabel.setText("Entered properties are not valid");
        }
    }

    @FXML
    public void deleteOnClick(){
        if (validEdge() && getTextFieldEntriesEdges()) {
            element = 0;
            LinkedList<Edge> fileReaderEdgeList = EdgeDB.getDBEdges();
            for (Edge ed : fileReaderEdgeList) {
                if (ed.getEdgeID().equals(edgeID)) {
                    delEdge = ed;
                    break;
                }
                element++;
            }


            try {
                EdgeDB.deleteEdgeDB(delEdge);

                //EdgeDB.populateHashMapDB();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            errorLabel.setText("Edge Deleted!");
            btnDelete.setDisable(true);
            clearTF();
            mainScreenController.adminController.refresh();
            mainScreenController.adminController.showEdgesOnClick();
            mainScreenController.edgeInfoPane.setVisible(true);
        } else {
            errorLabel.setText("Entered properties are not valid");
        }
    }

    @FXML
    public void addOnClick(){
        if(getTextFieldEntriesEdges() && validEdge()){
            Edge newEdge = new Edge(edgeID, startNode, endNode);
            LinkedList<Edge> edgeList = EdgeDB.getDBEdges();
            edgeList.add(newEdge);

            try{
                EdgeDB.addEdgeDB(newEdge);
                errorLabel.setText("Edge Added!");
                clearTF();
                mainScreenController.adminController.refresh();
                mainScreenController.adminController.showEdgesOnClick();
                mainScreenController.adminController.showNodesOnClick();
                mainScreenController.edgeInfoPane.setVisible(true);
                mainScreenController.adminController.editingEdge = false;
            } catch (SQLException e){
                e.printStackTrace();
            }

        } else {
            errorLabel.setText("Non-valid Edge Format");
        }
    }

    public void setEdgeIDs(String edgeID){

        if(switchEdge){
            startNodeIDLabel.setText(edgeID);
            switchEdge = false;
        } else {
            endNodeIDLabel.setText(edgeID);
            switchEdge = true;
        }

        if(!startNodeIDLabel.equals("") && !endNodeIDLabel.equals("")){
            edgeIDLabel.setText(startNodeIDLabel.getText() + "_" + endNodeIDLabel.getText());
        }

    }


    public void displayEdgeInfo(Edge edge){
        edgeIDLabel.setText(edge.getEdgeID());
        startNodeIDLabel.setText(edge.getStartNodeID());
        endNodeIDLabel.setText(edge.getEndNodeID());
    }

    public boolean validEdge() {
        goodTF = true;
        if (edgeIDLabel.getText().length() == 0 || edgeIDLabel.getText().length() > 30) {
            goodTF = false;
        }
        if (startNodeIDLabel.getText().length() == 0 || startNodeIDLabel.getText().length() > 15) {
            goodTF = false;
        }
        if (endNodeIDLabel.getText().length() ==0 || endNodeIDLabel.getText().length() > 15) {
            goodTF = false;
        }

        return goodTF;
    }

    public boolean getTextFieldEntriesEdges() {
        goodNumbers = true;
        try {
            edgeID = edgeIDLabel.getText();
            startNode = startNodeIDLabel.getText();
            endNode = endNodeIDLabel.getText();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            goodNumbers = false;
        }
        return goodNumbers;
    }


    public void clearTF(){
        edgeIDLabel.setText("");
        startNodeIDLabel.setText("");
        endNodeIDLabel.setText("");
    }
}
