package Memento;

import MementoPattern.CareTaker;
import MementoPattern.Originator;
import UIControllers.WelcomeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Test;

public class MementoMoriTest {
    @FXML
    private FXMLLoader loader;

    @FXML private Parent welcomeParent;

    Originator originator = new Originator();
    CareTaker careTaker = new CareTaker();
    WelcomeController welcomeController1;
    WelcomeController welcomeController2;

    @Test
    public  void mementoShit(){


        originator.setState(welcomeController1);
        careTaker.add(originator.saveStateToMemento());
        originator.setState(welcomeController2);
        careTaker.add(originator.saveStateToMemento());

        System.out.println("Current State: " + originator.getState());
        originator.getStateFromMemento(careTaker.get(0));
        System.out.println("First saved State: " + originator.getState());

    }

}