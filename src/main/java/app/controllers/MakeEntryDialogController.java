package app.controllers;

import java.io.IOException;
import java.util.ArrayList;

import app.App;

import model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for making a new entry
 *
 * @author Daniel Casper
 */
public class MakeEntryDialogController {

    @FXML private AnchorPane pane;
    @FXML private MenuButton publisher;

    public TextField seriesField;
    public TextField issuesField; 
    private ArrayList<Book> list;

    //private Authenticator authenticator;

    public void setMain(ArrayList<Book> list) {
        this.list=list;
        makePublisherButton();
        //this.authenticator = new Authenticator();
    }

    /**
     * Called when login as student is clicked
     * @param mouseEvent
     * @throws IOException 
     */
    public void loginAsStudent(MouseEvent mouseEvent) throws IOException {
        Entry entry=new Entry(seriesField.getText(), issuesField.getText(), publisher.getText(), list);
        for (Book run: list){
            System.out.println(run);
            System.out.println(run.getPublisher());
        }
        System.out.println("---------------------------");
    }

    public void makePublisherButton(){
        ObservableList<MenuItem> publishers=publisher.getItems();
        MenuItem item1=new MenuItem("DC");
        MenuItem item2=new MenuItem("Marvel");
        MenuItem item3=new MenuItem("Image");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item1.getText());
            }
        });
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item2.getText());
            }
        });
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item3.getText());
            }
        });
        publishers.add(item1);
        publishers.add(item2);
        publishers.add(item3);
    }

}
