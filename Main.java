import UIControllers.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Values.AppValues;

import java.io.IOException;

//import imaging.ImagingRequest;

public class Main extends Application {


    @FXML private FXMLLoader loader;

    @FXML private Parent welcomeParent;


    @Override
    //UI part
    public void start(Stage primaryStage){
        long time = System.nanoTime();


        //Parent welcomeParent = FXMLLoader.load(getClass().getResource("Scenes/WelcomeScene.fxml"));

        loader = new FXMLLoader(getClass().getResource("/Scenes/WelcomeScene.fxml"));
        try{
            welcomeParent = loader.load();
        } catch (IOException e){
            e.printStackTrace();
        }

        MainScreenController.setWelcomeParent(welcomeParent);

        Scene scene = new Scene(welcomeParent);
        AppValues.getInstance().currScene = scene;
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.initStyle(StageStyle.UNDECORATED); //This line makes the program fullScreen without messing up popup windows.

//        primaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitHint("");
        primaryStage.show();


        System.out.println("Main Start: " + (System.nanoTime() - time) / 1000000);
    }



    public static void main(String[] args) {
        launch(args);
        System.exit(0);

    }

}
