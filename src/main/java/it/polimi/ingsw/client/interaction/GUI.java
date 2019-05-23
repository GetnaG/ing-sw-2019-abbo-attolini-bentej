package it.polimi.ingsw.client.interaction;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

public class GUI extends Application {
    Scene loginScene;

    @Override
    public void start(Stage stage) {

        StackPane rootStackPane = new StackPane();
        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text("Welcome To Adrenaline");
        HBox usernameHBox = new HBox();
        HBox loginAndRadioBox = new HBox();
        Pane windowLogPane = new Pane();
        Text logWindow = new Text("This is a Log Window");

        rootStackPane.getChildren().add(border);
        border.setCenter(vertical);
        vertical.getChildren().add(topText);
        vertical.getChildren().add(usernameHBox);
        vertical.getChildren().add(loginAndRadioBox);
        vertical.getChildren().add(windowLogPane);
        windowLogPane.getChildren().add(logWindow);

        // Set up usernameHBox
        Label usernameLabel = new Label("Username");
        TextField inputUsername = new TextField();
        usernameHBox.getChildren().addAll(usernameLabel);
        usernameHBox.getChildren().addAll(inputUsername);
        //Set up loginAndRadioBox

        ToggleGroup radioButtons = new ToggleGroup();
        RadioButton socketRadio = new RadioButton("Socket");
        RadioButton rmiRadio = new RadioButton("RMI");
        socketRadio.setToggleGroup(radioButtons);
        rmiRadio.setToggleGroup(radioButtons);
        Button loginButton = new Button("Login");
        VBox buttonGroup = new VBox(socketRadio, rmiRadio);
        loginAndRadioBox.getChildren().addAll(buttonGroup, loginButton);

        // Setting a padding
        border.setPadding(new Insets(200, 200, 200, 200));
        loginButton.setPadding(new Insets(10, 5, 10, 5));
        loginAndRadioBox.setSpacing(50);
        //Setting spacing
        vertical.setSpacing(100);

        Button btn = new Button("OK");

        loginScene = new Scene(rootStackPane, 600, 600);

        stage.setTitle("OK");

        stage.setScene(loginScene);

        stage.show();
    }
}
