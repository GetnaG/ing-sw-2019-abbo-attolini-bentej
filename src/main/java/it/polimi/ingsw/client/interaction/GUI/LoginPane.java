package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.resources.R;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.io.IOException;

public class LoginPane extends StackPane {
    private String username;

    private Text logText;
    private TextField inputUsername;

    public LoginPane(ClientController controllerGUI, Text logText, TextField textField) {
        super();
        this.logText = logText;
        this.inputUsername = textField;
        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text(R.string("GUIWelcomeText"));
        HBox usernameHBox = new HBox();
        HBox loginAndRadioBox = new HBox();
        StackPane logWindow = new StackPane();
        logText.setText(R.string("InsertUsername"));
        Rectangle logRectangle = new Rectangle(800, 100);

        this.getChildren().add(border);
        border.setCenter(vertical);
        vertical.getChildren().add(topText);
        vertical.getChildren().add(usernameHBox);
        vertical.getChildren().add(loginAndRadioBox);
        vertical.getChildren().add(logWindow);
        logWindow.getChildren().addAll(logText, logRectangle);

        // Setting up background image
        BackgroundImage img = new BackgroundImage(R.image("logo"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(img);
        this.setBackground(background);
        // setting opacity factor
        vertical.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 10;");
        // Set up usernameHBox
        Label usernameLabel = new Label(R.string("username"));
        usernameHBox.getChildren().addAll(usernameLabel);
        usernameHBox.getChildren().addAll(inputUsername);
        //Set up loginAndRadioBox
        ToggleGroup radioButtons = new ToggleGroup();
        RadioButton socketRadio = new RadioButton("Socket");
        RadioButton rmiRadio = new RadioButton("RMI");
        socketRadio.setToggleGroup(radioButtons);
        rmiRadio.setToggleGroup(radioButtons);

        Button loginButton = new Button(R.string("login"));
        VBox buttonGroup = new VBox(socketRadio, rmiRadio);
        loginAndRadioBox.getChildren().addAll(buttonGroup, loginButton);
        // Set up logWindow
        logRectangle.setStroke(Color.WHITE);
        logRectangle.setStrokeWidth(3);
        logRectangle.setFill(Color.TRANSPARENT);
        logRectangle.setArcHeight(20);
        logRectangle.setArcWidth(20);
        // Setting architecture ( padding, border, margin, ...)
        //border.setPadding(new Insets(100, 100, 100, 100));
        loginButton.setPadding(new Insets(10, 5, 10, 5));
        loginAndRadioBox.setSpacing(50);
        vertical.setPrefWidth(border.getWidth() - 50);
        vertical.setSpacing(100);
        logWindow.setPadding(new Insets(0, 0, 50, 0));
        vertical.setAlignment(Pos.CENTER);
        usernameHBox.setAlignment(Pos.CENTER);
        loginAndRadioBox.setAlignment(Pos.CENTER);
        // Setting text proprieties
        topText.setFont(new Font("Arial", 20));
        topText.setFill(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", 20));
        usernameLabel.setTextFill(Color.WHITE);
        logText.setFont(new Font("Arial", 15));
        logText.setFill(Color.WHITE);
        socketRadio.setTextFill(Color.WHITE);
        rmiRadio.setTextFill(Color.WHITE);
        socketRadio.setSelected(true);
        //  Setting Button Event
        loginButton.setOnAction(e -> {
            GUI.setNickname(inputUsername.getText());
            // telling the controller the connection type
            if (socketRadio.isSelected()) {

                try {
                    controllerGUI.setSocket("localhost", 9002);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            } else {
                try {
                    controllerGUI.setRmi("localhost");//FIXME
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


    }

    public String getInputUsername() {
        return inputUsername.getText();
    }

    public void setLogText(String text) {
        logText.setText(text);
    }
}
