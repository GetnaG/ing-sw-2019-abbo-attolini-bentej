package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.resources.R;
import javafx.application.Application;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents the GUI interface of the game.
 *
 * @author Fahed B. Tej
 */
public class GUI extends Application implements InteractionInterface {
    /**
     * The scene used during the game
     */
    private Scene masterScene;
    /**
     * Controller of the view
     */
    private ClientController controller;
    /**
     * Players in hall
     */
    List<String> playersInHall;
    /**
     * The model of the game
     */
    private MatchState model;
    /**
     * Sync GUI
     */
    private SyncGUI sync;
    /**
     * log text in the GUI
     */
    private Text logText;
    /**
     * Users in the hall
     */
    private HBox usersBox;
    /**
     * Login input username
     */
    private TextField inputUsername;
    /**
     * Stage of the windows
     */
    private Stage stage;


    public GUI() {

    }

    /**
     * Starts the GUI. Never call directly.
     *
     * @param stage stage (O.S dependent)
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        controller = new ClientController(null, this);
        setUpLoginScene(stage);
        stage.show();

    }

    /**
     * Sets up a login scene in the given stage.
     *
     * @param stage
     */
    private void setUpLoginScene(Stage stage) {

        StackPane rootStackPane = new StackPane();
        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text("Welcome To Adrenaline");
        HBox usernameHBox = new HBox();
        HBox loginAndRadioBox = new HBox();
        StackPane logWindow = new StackPane();
        Text logText = new Text("Insert a username ");
        Rectangle logRectangle = new Rectangle(400, 40);

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
        topText.setFont(new Font("Cambria", 20));
        topText.setFill(Color.WHITE);
        usernameLabel.setFont(new Font("Cambria", 20));
        usernameLabel.setTextFill(Color.WHITE);
        logText.setFont(new Font("Cambria", 15));
        logText.setFill(Color.WHITE);
        socketRadio.setTextFill(Color.WHITE);
        rmiRadio.setTextFill(Color.WHITE);
        socketRadio.setSelected(true);
        //  Setting Button Event
        loginButton.setOnAction(e -> {

            // telling the controller the connection type
            if (socketRadio.isSelected()) {
                controller.setConnection();
            } else {
                try {
                    controller.setConnection("localhost", 9000);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            logText.setText("Checking username...");
            buildHallPane(stage);

        });

        masterScene = new Scene(rootStackPane, 1200, 1200);
        stage.setTitle("Adrenaline");
        stage.setScene(masterScene);
    }

    /**
     * Builds a Hall in the given stage.
     *
     * @param stage
     */
    private void buildHallPane(Stage stage) {
        StackPane rootStackPane = new StackPane();
        BorderPane border = new BorderPane();
        VBox vertical = new VBox();
        Text topText = new Text("Hall");
        StackPane logWindow = new StackPane();
        logText = new Text("Match starting in 27 seconds...");
        Rectangle logRectangle = new Rectangle(400, 40);
        Text textUserBox = new Text("The following users are connected:");
        usersBox = new HBox();
        Button debugSkip = new Button("debug skip");


        rootStackPane.getChildren().add(border);
        border.setCenter(vertical);
        vertical.getChildren().addAll(topText, textUserBox, usersBox, logWindow);
        logWindow.getChildren().addAll(logText, logRectangle, debugSkip);
        controller.getPlayersInHall().forEach(p -> usersBox.getChildren().add(getPlayerBox(p)));

        // Set up background image
        BackgroundImage img = new BackgroundImage(R.image("logo"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(img);
        rootStackPane.setBackground(background);
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

        masterScene.setRoot(rootStackPane);
    }

    /**
     * Builds a Game Pane in the given stage/
     */
    private void buildGamePane(Stage stage) {

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
    private void buildTop(BorderPane borderPane) {
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
    private void buildBottom(BorderPane borderPane) {
        VBox vbox = new VBox();
        borderPane.setBottom(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(getQuestionBox());
        vbox.getChildren().add(getAnswerBox());


    }

    /**
     * Builds the center part of the given border pane
     * @param borderPane
     */
    private void buildCenter(BorderPane borderPane) {
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
    private void buildLeft(BorderPane borderPane) {
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
    private void buildRight(BorderPane borderPane) {
        VBox vbox = new VBox();
        borderPane.setRight(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER_RIGHT);

        vbox.getChildren().add(getCard("AD_powerups_IT_026"));
        vbox.getChildren().add(getCard("AD_powerups_IT_027"));
        vbox.getChildren().add(getCard("AD_powerups_IT_028"));

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);


    }


    private Node getPlayerTile() {
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

    private Node getKillshotTrack() {
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

    private Node getCard(String imageName) {
        Image image = R.image(imageName);

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(80);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private Node getQuestionBox() {
        StackPane stackPane = new StackPane();
        Text text = new Text("Choose an Action");
        Rectangle rectangle = new Rectangle(700, 100);

        stackPane.getChildren().addAll(text, rectangle);

        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(5);

        return stackPane;
    }

    private Node getAnswerBox() {
        HBox hbox = new HBox();
        Button answer1 = new Button("Shoot");
        Button answer2 = new Button("Grab");
        Button answer3 = new Button("Move");

        hbox.getChildren().addAll(answer1, answer2, answer3);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    private Node getMap() {
        Image image = R.image("Map");

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(500);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;


    }

    private void insertOtherPlayers(BorderPane borderPane) {
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

    private Node getCirclePlayer(String nickname) {
        StackPane stackPane = new StackPane();
        Text text = new Text(nickname);
        Circle circle = new Circle(50, Color.TRANSPARENT);

        stackPane.getChildren().addAll(text, circle);

        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);

        return stackPane;
    }

    private List<String> getPlayersWaiting() {
        List<String> players = new ArrayList<>();
        players.add("Hello");
        players.add("Hello");
        players.add("Hello");
        return players;
    }

    private Pane getPlayerBox(String nickname) {
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

    @Override
    public void notifyUpdatedState() {
        updateHall(10);
    }

    @Override
    public int chooseEffectSequence(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseSpawn(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int choosePowerup(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseDestination(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseWeapon(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseWeaponToBuy(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseWeaponToDiscard(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseWeaponToReload(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseAction(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int choosePowerupForPaying(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseUseTagBack(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseTarget(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public void drawState() {

    }

    /**
     * Sets controller in the view.
     *
     * @param controller
     */
    @Override
    public void setController(ClientController controller) {
        this.controller = controller;
    }


    @Override
    public String askName() {

        if (inputUsername.getText().length() > 1 && inputUsername.getText().length() < 18) {
            return inputUsername.getText();
        } else {
            logText.setText("Insert valid username");
            return "ERROR";
        }
    }

    @Override
    public void sendNotification(String notificationKey) {
        switch (notificationKey) {
            /**
             * case USERNAME_AVAIABLE:
             *  buildHallPane(stage);
             *  break;
             * case USERNAME_TAKEN_AND_OFFLINE:
             *  buildHallPane(stage);
             *  break;
             * case USERNAME_TAKEN_AND_ONLINE:
             *   logText.setText("Username taken and offline");
             *   break;
             *
             */

        }

    }

    public void updateHall(int seconds) {
        List<String> playersInHallRecent = controller.getPlayersInHall();
        if (playersInHall != null) {
            // sau that there was a player disconected
            for (String p : playersInHall) {
                if (!playersInHallRecent.contains(p)) {
                    // user was disconected
                    logText.setText("The following users are disconnected: " + p);
                }
            }
        }
        playersInHallRecent.add("hello");
        usersBox.getChildren().removeAll();
        playersInHall.forEach(p -> usersBox.getChildren().add(getPlayerBox(p)));
        logText.setText("Match starting in " + seconds + "seconds...");
    }

    public void setSynchGUI(SyncGUI sync) {
        this.sync = sync;
    }

    public void setModel(MatchState model) {
        this.model = model;
    }


}
