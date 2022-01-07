package UIControllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AlternateLoginController implements Initializable {
    @FXML
    AnchorPane altLoginScreen;
    @FXML
    Button btnCancel;
    @FXML
    Button btnLogin;
    @FXML
    JFXTextField username;
    @FXML
    JFXTextField password;
    @FXML WelcomeController welcomeController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void closeLogin(){

        welcomeController.altLoginClose();
//        System.out.println("suck it");
    }

    public void checkValidInput(){

    }
}
