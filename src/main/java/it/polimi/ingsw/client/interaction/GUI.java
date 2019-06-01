package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.ClientMain;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.resources.R;
import javafx.animation.PauseTransition;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents the GUI interface of the game.
 *
 * @author Fahed B. Tej
 */
public class GUI extends Application {
    /**
     * The scene used during the game
     */
    private static Scene masterScene;
    /**
     * Controller of the view
     */
    private static ClientController controllerGUI;
    /**
     * Players in hall
     */
    private static List<String> playersInHall;
    /**
     * The model of the game
     */
    private static MatchState model;
    /**
     * Sync GUI
     */
    private static SyncGUI sync;
    /**
     * log text in the GUI
     */
    private static Text logText;
    /**
     * Users in the hall
     */
    private static HBox usersBox;
    /**
     * Login input username
     */
    private static TextField inputUsername;
    /**
     * Stage of the windows
     */
    private static Stage stage;

    private static StackPane rootStackPane;

    private static HBox answerBox;

    private static boolean answerGiven;

    private static int answer;

    private static Text questionText;

    public GUI() {

    }

    /**
     * Builds a Hall in the given stage.
     *
     * @param stage
     */
    private static void buildHallPane(Stage stage) {

        StackPane rootStackPaneHall = new StackPane();
        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text("Hall");
        StackPane logWindow = new StackPane();
        logText = new Text("");
        Rectangle logRectangle = new Rectangle(400, 40);
        Text textUserBox = new Text("The following users are connected:");
        usersBox = new HBox();
        Button debugSkip = new Button("debug skip");


        rootStackPaneHall.getChildren().add(border);
        border.setCenter(vertical);
        vertical.getChildren().addAll(topText, textUserBox, usersBox, logWindow);
        logWindow.getChildren().addAll(logText, logRectangle, debugSkip);
        updateHall(10);
        //playersInHall = controller.getPlayersInHall();
        //playersInHall.forEach(p -> usersBox.getChildren().add(getPlayerBox(p)));

        // Set up background image
        BackgroundImage img = new BackgroundImage(R.image("logo"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(img);
        rootStackPaneHall.setBackground(background);
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
        debugSkip.setOnAction(e -> buildGamePane(null));


        stage.getScene().setRoot(rootStackPaneHall);
    }

    /**
     * Builds a Game Pane in the given stage/
     */
    private static void buildGamePane(Stage stage) {

        StackPane rootStackPane = new StackPane();
        BorderPane borderPane = new BorderPane();

        rootStackPane.getChildren().add(borderPane);

        buildTop(borderPane);
        buildBottom(borderPane);
        buildCenter(borderPane);
        buildLeft(borderPane);
        buildRight(borderPane);

        rootStackPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        masterScene.setRoot(rootStackPane);

    }

    /**
     * Build the Top part of the given border pane
     * @param borderPane
     */
    private static void buildTop(BorderPane borderPane) {
        HBox hbox = new HBox(getKillshotTrack(), getPlayerTile());
        borderPane.setTop(hbox);
        borderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(30);
    }

    /**
     * Build the Bottom part of the given border pane
     * @param borderPane
     */
    private static void buildBottom(BorderPane borderPane) {
        VBox vbox = new VBox();
        answerBox = new HBox();

        borderPane.setBottom(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(getQuestionBox("Wait for your turn"));
        vbox.getChildren().add(answerBox);

/*
      //  vbox.getChildren().add(chageAnswerBox(new ArrayList<>()));
        //TODO Remove the following 3 lines
        List<String> opt = new ArrayList<>();
        opt.add("AD_powerups_IT_026");
        opt.add("AD_powerups_IT_027");
        opt.add("AD_powerups_IT_028");
        List<List<String>> options = new ArrayList<>();
        options.add(opt);


        new Thread(() ->sync.chooseWeapon(options)).run();
        System.out.println("ddd");

 */


        vbox.setFillWidth(true);
        vbox.setMinHeight(400);


    }

    /**
     * Builds the center part of the given border pane
     * @param borderPane
     */
    private static void buildCenter(BorderPane borderPane) {
        BorderPane centerBorderPane = new BorderPane();
        borderPane.setCenter(centerBorderPane);

        centerBorderPane.setCenter(getMap());
        insertOtherPlayers(centerBorderPane);

        centerBorderPane.setAlignment(centerBorderPane, Pos.CENTER);


    }

    /**
     * Builds the left part of the given border pane
     * @param borderPane
     */
    private static void buildLeft(BorderPane borderPane) {
        VBox vbox = new VBox();
        borderPane.setLeft(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER_LEFT);

        vbox.getChildren().add(getCard("AD_weapons_IT_024"));
        vbox.getChildren().add(getCard("AD_weapons_IT_025"));
        vbox.getChildren().add(getCard("AD_weapons_IT_026"));

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

    }

    /**
     * Build the right part of the given border pane
     * @param borderPane
     */
    private static void buildRight(BorderPane borderPane) {
        VBox vbox = new VBox();
        borderPane.setRight(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER_RIGHT);

        vbox.getChildren().add(getCard("AD_powerups_IT_026"));
        vbox.getChildren().add(getCard("AD_powerups_IT_027"));
        vbox.getChildren().add(getCard("AD_powerups_IT_028"));

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);


    }

    private static Node getPlayerTile() {
        // load the image
        Image image = R.image("PlayerBoard");

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(700);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private static Node getKillshotTrack() {
        // load the image
        Image image = R.image("ActionTile");

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(60);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private static ImageView getCard(String imageName) {
        Image image = R.image(imageName);

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(80);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private static Node getQuestionBox(String question) {
        StackPane stackPane = new StackPane();
        questionText = new Text(question);
        Rectangle rectangle = new Rectangle(700, 100);

        stackPane.getChildren().addAll(questionText, rectangle);

        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(5);
        questionText.setFill(Color.WHITE);

        return stackPane;
    }

    /**
     * Builds the options and puts them in the Answer Box
     *
     * @param options
     * @return
     */
    private static void changeAnswerBox(List<List<String>> options) {
        Platform.runLater(() -> {
            ImageView imgBox = new ImageView();
            answerBox.getChildren().removeAll(answerBox.getChildren());

            // Creating a GroupBoxAnswer for each option
            options.forEach(group -> {
                HBox groupAnswerBox = getGroupBoxAnswer(group, imgBox, options.indexOf(group));
                answerBox.getChildren().add(groupAnswerBox);
            });

            answerBox.getChildren().add(imgBox);


            answerBox.setSpacing(10);
            answerBox.setAlignment(Pos.CENTER);
        });

    }

    private static HBox getGroupBoxAnswer(List<String> group, ImageView imgBox, int groupIndex) {
        HBox hbox = new HBox();
        // TOP GROUP Name
        Button groupNameButton = new Button(groupIndex + "");
        groupNameButton.setOnMouseClicked(e -> {
            answer = groupIndex;
            answerGiven = true;
        });
        hbox.getChildren().add(groupNameButton);

        // Creating a list of labels for each element of the group
        List<Label> labelsGroupElements = new ArrayList<>();
        // Adding label to list using the element as key to retrieve the name of the label
        group.forEach(element -> {
            //  Label label = new Label(R.string(element));
            Label label = new Label("some name"); // TODO Remove this line
            labelsGroupElements.add(label);
            createCardAnimation(label, element, imgBox);
            //  button.setDisable(true);
            hbox.getChildren().add(label);
            label.setTextFill(Color.WHITE);
            label.setFont(new Font("Arial", 15));
            label.setLineSpacing(5);
            label.setPadding(new Insets(5, 5, 5, 5));
        } );

        return hbox;
    }

    private static Node getMap() {
        Image image = R.image("Map");

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(500);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private static void insertOtherPlayers(BorderPane borderPane) {
        Node alice = getCirclePlayer("Alice");
        Node bob = getCirclePlayer("Bob");
        Node charlie = getCirclePlayer("Charlie");

        borderPane.setLeft(alice);
        borderPane.setTop(bob);
        borderPane.setRight(charlie);

        borderPane.setAlignment(alice, Pos.CENTER_RIGHT);
        borderPane.setAlignment(bob, Pos.CENTER);
        borderPane.setAlignment(charlie, Pos.CENTER_LEFT);
    }

    private static Node getCirclePlayer(String nickname) {
        StackPane stackPane = new StackPane();
        Text text = new Text(nickname);
        Circle circle = new Circle(50, Color.TRANSPARENT);

        stackPane.getChildren().addAll(text, circle);

        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);

        return stackPane;
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

    public static void notifyUpdatedState() {
        Platform.runLater(
                () -> updateHall(2)
        );
    }

    public static int chooseEffectSequence(List<List<String>> optionKeys) {
        return 0;
    }

    private List<String> getPlayersWaiting() {
        List<String> players = new ArrayList<>();
        players.add("Hello");
        players.add("Hello");
        players.add("Hello");
        return players;
    }

    public static int chooseSpawn(List<List<String>> optionKeys) {
        return 0;
    }

    public static int choosePowerup(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseDestination(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseWeapon(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, "Choose one of the following weapon: ");
    }

    public static int chooseWeaponWithQuestion(List<List<String>> optionKeys, String question) {
        answerGiven = false;
        changeAnswerBox(optionKeys);
        questionText.setText(question);
        return 0;
    }

    public static int chooseWeaponToBuy(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, "Choose one of the following weapon to buy: ");
    }

    public static int chooseWeaponToDiscard(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, "Choose one of the following weapon to discard: ");
    }

    public static int chooseWeaponToReload(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, "Choose one of the following weapon to reload");
    }

    public static int chooseAction(List<List<String>> optionKeys) {
        return 0;
    }

    public static int choosePowerupForPaying(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseUseTagBack(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseTarget(List<List<String>> optionKeys) {
        return 0;
    }

    /**
     * Sets controller in the view.
     *
     * @param controller
     */

    public static void setController(ClientController controller) {
        controllerGUI = controller;
    }

    public static String askName() {

        if (inputUsername.getText().length() > 1 && inputUsername.getText().length() < 18) {
            return inputUsername.getText();
        } else {
            logText.setText("Insert valid username");
            return "ERROR";
        }
    }

    public static void sendNotification(String notificationKey) {
        switch (notificationKey) {
            case "GREET":
                break;
            case "USERNAME_AVAILABLE":
                buildHallPane(stage);
                break;
            case "USERNAME_TAKEN_AND_OFFLINE":
                buildHallPane(stage);
                break;
            case "USERNAME_TAKEN_AND_ONLINE":
                //        logText.setText("Username taken and offline");
                break;
        }

    }


    public void drawState() {

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

    public static void setSynchGUI(SyncGUI syncGUI) {
        sync = syncGUI;
    }

    public static void setModel(MatchState modelMVC) {
        model = modelMVC;
    }

    private static void createCardAnimation(Node node, String resID, ImageView imgBox) {
        node.setOnMouseEntered(e -> {
            imgBox.setVisible(true);
            imgBox.setImage(getCard(resID).getImage());
            node.setStyle("-fx-font-weight: bold");
        });
        node.setOnMouseExited(e -> {
            imgBox.setVisible(false);
            node.setStyle("-fx-font-weight: regular");
        });
    }

    public static boolean isAnswerGiven() {
        return answerGiven;
    }

    public static int getAnswer() {
        return answer;
    }

    /**
     * Starts the GUI. Never call directly.
     *
     * @param stage stage (O.S dependent)
     */

    public void start(Stage stage) {
        this.stage = stage;
        setUpLoginScene(stage);
        masterScene = new Scene(rootStackPane, 1000, 1000);
        buildGamePane(null);
        stage.setTitle("Adrenaline");
        stage.setScene(masterScene);
        stage.show();
        //ClientMain.setControllerModel(this);

    }

    /**
     * Sets up a login scene in the given stage.
     *
     * @param stage
     */
    private void setUpLoginScene(Stage stage) {

        rootStackPane = new StackPane();
        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text("Welcome To Adrenaline");
        HBox usernameHBox = new HBox();
        HBox loginAndRadioBox = new HBox();
        StackPane logWindow = new StackPane();
        Text logText = new Text("Insert a username ");
        Rectangle logRectangle = new Rectangle(800, 100);

        rootStackPane.getChildren().add(border);
        border.setCenter(vertical);
        vertical.getChildren().add(topText);
        vertical.getChildren().add(usernameHBox);
        vertical.getChildren().add(loginAndRadioBox);
        vertical.getChildren().add(logWindow);
        logWindow.getChildren().addAll(logText, logRectangle);

        // Set up background image
        BackgroundImage img = new BackgroundImage(R.image("logo"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(img);
        rootStackPane.setBackground(background);
        // setting opacity factor
        vertical.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 10;");
        // Set up usernameHBox
        Label usernameLabel = new Label("Username");
        inputUsername = new TextField();
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

            // telling the controller the connection type
            if (socketRadio.isSelected()) {

                try {
                    controllerGUI.setConnection("localhost", 9000);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            } else {
                controllerGUI.setConnection();
            }
        });


    }

    /**
     * Builds the options and puts them in the Answer Box
     *
     * @param options
     * @return
     */
    private Node getDumbAnswerBox(List<List<String>> options) {
        answerBox = new HBox();
        Button answer1 = new Button("Shoot");
        Button answer2 = new Button("Grab");
        Button answer3 = new Button("Move");
        ImageView imgBox = new ImageView();

        imgBox.setTranslateY(-40);
        createCardAnimation(answer1, "", imgBox);

        answerBox.getChildren().addAll(answer1, answer2, answer3, imgBox);
        answerBox.setSpacing(10);
        answerBox.setAlignment(Pos.CENTER);
        return answerBox;
    }

    private String fromResIDToName(String resID) {
        return "A"; // TODO Implement
    }

}
