package UIControllers;

import PathFinding.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class DirectionsSettingsController implements Initializable {

    @FXML public JFXCheckBox ckbAStar;
    @FXML public JFXCheckBox ckbBestFirst;
    @FXML public JFXCheckBox ckbBreadthFirst;
    @FXML public JFXCheckBox ckbDepthFirst;
    @FXML public JFXCheckBox ckbDjikstra;
    @FXML public JFXCheckBox ckbGrandTour;
    @FXML public JFXCheckBox ckbRandom;
    @FXML
    JFXToggleButton tbTextualDirections;
    @FXML
    JFXToggleButton tbSpeechDirections;
    @FXML
    ToggleGroup dType;
    @FXML
    JFXRadioButton rbNoCond;
    @FXML
    JFXRadioButton rbAvoidStairs;
    @FXML
    JFXRadioButton rbAvoidElev;
    @FXML
    ToggleGroup cType;
    @FXML
    JFXButton btnSet;
    @FXML
    JFXRadioButton rbEnglish;

    @FXML
    MainScreenController mainScreenController;

    public DirectionsSettingsController(){

    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbTextualDirections.setSelected(false);
        tbSpeechDirections.setSelected(false);
        ckbAStar.setSelected(true);

    }

    @FXML
    public void textualDirectionsOnClick(){
        mainScreenController.directionsController.pathFindingTextual = tbTextualDirections.isSelected();
        mainScreenController.textualDirectionsPane.setVisible(false);
    }

    @FXML
    public void speechDirectionsOnClick(){
        mainScreenController.directionsController.pathFindingSpeech = tbSpeechDirections.isSelected();
    }

    @FXML
    public void setOnClick(){
        mainScreenController.directionsSettingsBox.setVisible(false);
    }

    // Path finding algorithm choosi
    @FXML
    public void aStarOnClick(){
        if(ckbAStar.isSelected()){
            mainScreenController.directionsController.algoritmsChosen.remove(new AStar()); // remove default ASTAR
            mainScreenController.directionsController.algoritmsChosen.add(new AStar());
        }
        else{
            mainScreenController.directionsController.algoritmsChosen.remove(new AStar());
        }
    }

    @FXML
    public void bestFirstOnClick() {
        if(ckbBestFirst.isSelected()){
            mainScreenController.directionsController.algoritmsChosen.add(new BestFirst());
        }
        else {
            mainScreenController.directionsController.algoritmsChosen.remove(new BestFirst());
        }
    }

    @FXML
    public void breadthFirstOnClick() {
        if(ckbBreadthFirst.isSelected()){
            mainScreenController.directionsController.algoritmsChosen.add(new BreadthFirst());
        }
        else {
            mainScreenController.directionsController.algoritmsChosen.remove(new BreadthFirst());
        }
    }

    @FXML
    public void depthFirstOnClick() {
        if(ckbDepthFirst.isSelected()){
            mainScreenController.directionsController.algoritmsChosen.add(new DepthFirst());
        }
        else {
            mainScreenController.directionsController.algoritmsChosen.remove(new DepthFirst());
        }
    }

    @FXML
    public void dijkstraOnClick() {
        if(ckbDjikstra.isSelected()){
            mainScreenController.directionsController.algoritmsChosen.add(new Dijkstra());
        }
        else{
            mainScreenController.directionsController.algoritmsChosen.remove(new Dijkstra());
        }
    }

    @FXML
    public void grandTourOnClick(){
        if(ckbGrandTour.isSelected()){
            mainScreenController.directionsController.algoritmsChosen.add(new GrandTour());
        }
        else {
            mainScreenController.directionsController.algoritmsChosen.remove(new GrandTour());
        }
    }

    // uncomment this and the JFX text box for a good time
//    @FXML
//    public void randomOnClick() {
//        if(ckbRandom.isSelected()){
//            mainScreenController.directionsController.algoritmsChosen.add(new RandomPath());
//        }
//        else {
//            mainScreenController.directionsController.algoritmsChosen.remove(new RandomPath());
//        }
//    }


    // Path finding condition choosing
    @FXML
    public void noCondOnClick(){
        mainScreenController.directionsController.pathFindingConditions = 1;
//        System.out.println("no conditions");
    }

    @FXML
    public void avoidStairsOnClick(){
        mainScreenController.directionsController.pathFindingConditions = 2;
//        System.out.println("avoid stairs");
    }

    @FXML
    public void avoidElevOnClick(){
        mainScreenController.directionsController.pathFindingConditions = 3;
//        System.out.println("avoid elevators");
    }

    // Language preference methods
    public void englishOnClick() {
        mainScreenController.directionsController.textualLanguage = 1;
    }

    public void spanishOnClick() {
        mainScreenController.directionsController.textualLanguage = 2;
    }

    public void frenchOnClick() {
        mainScreenController.directionsController.textualLanguage = 3;
    }

    public void italianOnClick() {
        mainScreenController.directionsController.textualLanguage = 4;
    }

    public void russianOnClick() {
        mainScreenController.directionsController.textualLanguage = 5;
    }
}
