package UIControllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class InformationController implements Initializable {
    @FXML
    JFXButton btnBack;

    @FXML
    MainScreenController mainScreenController;

    public InformationController(){

    }

    public void setMainScreenController(MainScreenController mc){
        this.mainScreenController = mc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void backOnClick(){
//        mainScreenController.gpane.reset();
        mainScreenController.informationBox.setVisible(false);
        mainScreenController.imgView.requestFocus();
//        System.out.println("here");
    }
}
