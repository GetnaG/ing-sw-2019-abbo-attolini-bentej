package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import javafx.scene.layout.StackPane;
import it.polimi.ingsw.client.resources.R;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HallPane extends StackPane {
    private static Text logText;
    private static HBox usersBox;
    /**
     * Players in hall
     */
    private static List<String> playersInHall;
    /**
     * controller
     */
    private static ClientController controller;
    /**
     * model
     */
    private static MatchState model;

    public HallPane(MatchState model, ClientController controllerGUI) {
        super();
        this.model = model;

        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text("Hall");
        StackPane logWindow = new StackPane();
        logText = new Text("");
        Rectangle logRectangle = new Rectangle(400, 40);
        Text textUserBox = new Text("The following users are connected:");
        usersBox = new HBox();
        Button debugSkip = new Button("debug skip");


        this.getChildren().add(border);
        border.setCenter(vertical);
        vertical.getChildren().addAll(topText, textUserBox, usersBox, logWindow);
        logWindow.getChildren().addAll(logText, logRectangle, debugSkip);
        updateHall(10); // TODO Set timer

        // Set up background image
        BackgroundImage img = new BackgroundImage(R.image("logo"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(img);
        this.setBackground(background);
        // setting opacity factor
        vertical.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 10;");
        // Set up playersBox

        // Set up logWindow
        logRectangle.setStroke(Color.WHITE);
        logRectangle.setStrokeWidth(3);
        logRectangle.setFill(Color.TRANSPARENT);
        logRectangle.setArcHeight(20);
        logRectangle.setArcWidth(20);

        // Setting architecture ( padding, border, margin, ...)
        //border.setPadding(new Insets(100, 100, 100, 100));
        vertical.setPrefWidth(border.getWidth() - 50);
        vertical.setSpacing(100);
        logWindow.setPadding(new Insets(0, 0, 50, 0));
        vertical.setAlignment(Pos.CENTER);
        usersBox.setSpacing(10);
        usersBox.setAlignment(Pos.CENTER);

        // Setting text proprieties
        topText.setFont(new Font("Cambria", 40));
        topText.setFill(Color.WHITE);
        logText.setFont(new Font("Cambria", 15));
        logText.setFill(Color.WHITE);
        textUserBox.setFont(new Font("Cambria", 20));
        textUserBox.setFill(Color.WHITE);

        // Setting event
        debugSkip.setOnAction(e -> GUI.buildGamePane(null));


    }

    public static void updateHall(int seconds) {
        if (playersInHall == null)
            playersInHall = new ArrayList<>();

        List<String> oldPlayers = new ArrayList<>();
        playersInHall.addAll(playersInHall);
        playersInHall = model.getConnectedPlayers();

        usersBox.getChildren().removeAll(usersBox.getChildren());
        for (String name : playersInHall) {
            usersBox.getChildren().add(getPlayerBox(name));
        }

        // Find disconections
        for (String name : model.getDisconnectedPlayers())
            if (!playersInHall.contains(name))
                logText.setText(logText.getText() + "\n" + name + " has disconnected");

        // Find new connections
        for (String name : model.getJustConnectedPlayers())
            if (!oldPlayers.contains(name))
                logText.setText(logText.getText() + "\n" + name + " is connected");
    }

    private static Pane getPlayerBox(String nickname) {
        Rectangle rectangle = new Rectangle();
        Text text = new Text(nickname);
        StackPane playerLogo = new StackPane(rectangle, text);

        //Setting propieties
        rectangle.toBack();
        rectangle.setFill(Color.YELLOW);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);
        text.setFill(Color.WHITE);
        text.setFont(new Font("Cambria", 15));
        playerLogo.setPadding(new Insets(10, 10, 10, 10));

        return playerLogo;
    }
}
