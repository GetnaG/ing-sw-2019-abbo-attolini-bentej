package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.resources.R;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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

/**
 * Represents the GUI interface of the game.
 * It can have a {@linkplain LoginPane} , {@linkplain HallPane} or a {@linkplain GamePane}.
 *
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
     * LoginPane input username
     */
    private static TextField inputUsername;
    /**
     * Stage of the windows
     */
    private static Stage stage;

    private static boolean answerGiven;

    private static int answer;

    private static HBox answerBox;

    private static Text questionText;

    public GUI() {

    }

    /**
     * Builds a Hall in the given stage.
     *
     * @param stage
     */
    private static void buildHallPane(Stage stage) {
        StackPane hallGUI = new HallPane(model, controllerGUI);
        stage.getScene().setRoot(hallGUI);
    }

    /**
     * Builds a Game Pane in the given stage/
     */
    public static void buildGamePane(Stage stage) {

        StackPane rootStackPane = new GamePane(answerBox, questionText);

        masterScene.setRoot(rootStackPane);

    }

    public static void notifyUpdatedState() {
        Platform.runLater(
                () -> HallPane.updateHall(2)
        );
    }

    public static int choosePowerup(List<List<String>> optionKeys) {
        return choosePowerupWithQuestion(optionKeys, R.string("askPowerup"));
    }

    public static int choosePowerupWithQuestion(List<List<String>> optionKeys, String question) {
        answerGiven = false;
        GamePane.changeAnswerBox(optionKeys);
        questionText.setText(question);
        return 0;
    }

    public static int chooseEffectSequence(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseSpawn(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseWeapon(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, R.string("askWeapon"));
    }

    public static int chooseWeaponWithQuestion(List<List<String>> optionKeys, String question) {
        answerGiven = false;
        GamePane.changeAnswerBox(optionKeys);
        questionText.setText(question);
        return 0;
    }

    public static int chooseDestination(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseWeaponToBuy(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, R.string("askWeaponBuy"));
    }

    public static int chooseWeaponToDiscard(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, R.string("askWeaponDiscard"));
    }

    public static int chooseWeaponToReload(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, R.string("askWeaponReload"));
    }

    public static int choosePowerupForPaying(List<List<String>> optionKeys) {
        return choosePowerupWithQuestion(optionKeys, R.string("askPowerupPay"));
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
                logText.setText("Username taken and offline");
                break;
        }
    }

    public static int chooseAction(List<List<String>> optionKeys) {
        return 0;
    }

    public static void setAnswer(int answer) {
        GUI.answer = answer;
    }

    public static int chooseUseTagBack(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseTarget(List<List<String>> optionKeys) {
        return 0;
    }

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

    /**
     * Starts the GUI. Never call directly.
     *
     * @param stage stage (O.S dependent)
     */

    public void start(Stage stage) {
        this.stage = stage;
        setUpLoginScene(stage);

        stage.setTitle("Adrenaline");
        stage.setScene(masterScene);
        stage.show();
    }


    public static void setSynchGUI(SyncGUI syncGUI) {
        sync = syncGUI;
    }

    public static void setModel(MatchState modelMVC) {
        model = modelMVC;
    }


    public static boolean isAnswerGiven() {
        return answerGiven;
    }

    public static int getAnswer() {
        return answer;
    }


    public static void setAnswerGiven(boolean isAnwerGiven) {
        answerGiven = isAnwerGiven;
    }

    /**
     * Sets up a login scene in the given stage.
     *
     * @param stage
     */
    private void setUpLoginScene(Stage stage) {
        StackPane loginGUI = new LoginPane(controllerGUI);
        masterScene = new Scene(loginGUI, 1000, 1000);
    }
}
