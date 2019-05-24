package it.polimi.ingsw.client.interaction;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {
    Scene loginScene;

    @Override
    public void start(Stage stage) {
        setUpLoginScene(stage);
        stage.show();

    }

    private void setUpLoginScene(Stage stage) {

        StackPane rootStackPane = new StackPane();
        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text("Welcome To Adrenaline");
        HBox usernameHBox = new HBox();
        HBox loginAndRadioBox = new HBox();
        StackPane logWindow = new StackPane();
        Text logText = new Text("This is a Log Window");
        Rectangle logRectangle = new Rectangle(200, 40);

        rootStackPane.getChildren().add(border);
        border.setCenter(vertical);
        vertical.getChildren().add(topText);
        vertical.getChildren().add(usernameHBox);
        vertical.getChildren().add(loginAndRadioBox);
        vertical.getChildren().add(logWindow);
        logWindow.getChildren().addAll(logText, logRectangle);

        // Set up background image
        Image img = new Image("http://www.gioconauta.it/wp-content/uploads/2016/12/adrenaline-e1481789187635.jpg");
        Background background = new Background(new BackgroundImage(img, null, null, null, null));
        rootStackPane.setBackground(background);
        // setting opacity factor
        vertical.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 10;");
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
        // Set up logWindow
        logRectangle.setStroke(Color.WHITE);
        logRectangle.setStrokeWidth(3);
        logRectangle.setFill(Color.TRANSPARENT);
        logRectangle.setArcHeight(4);
        logRectangle.setArcWidth(4);
        // Setting distances ( padding, border, margin, ...)
        border.setPadding(new Insets(100, 100, 100, 100));
        loginButton.setPadding(new Insets(10, 5, 10, 5));
        loginAndRadioBox.setSpacing(50);
        vertical.setPrefWidth(border.getWidth() - 50);
        vertical.setSpacing(100);
        logWindow.setPadding(new Insets(0, 0, 50, 0));
        vertical.setAlignment(Pos.CENTER);
        usernameHBox.setAlignment(Pos.CENTER);
        loginAndRadioBox.setAlignment(Pos.CENTER);
        // Setting text propieties
        topText.setFont(new Font("Cambria", 20));
        topText.setFill(Color.WHITE);
        usernameLabel.setFont(new Font("Cambria", 20));
        usernameLabel.setTextFill(Color.WHITE);
        logText.setFont(new Font("Cambria", 15));
        logText.setFill(Color.WHITE);
        socketRadio.setTextFill(Color.WHITE);
        rmiRadio.setTextFill(Color.WHITE);

        loginScene = new Scene(rootStackPane, 600, 600);

        stage.setTitle("Adrenaline");

        stage.setScene(loginScene);
    }
}
