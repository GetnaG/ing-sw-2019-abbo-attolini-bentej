package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.resources.R;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    /**
     * Tells if answer is ready
     */
    private static boolean answerGiven;
    /**
     * Index of the answer choosen
     */
    private static int answer;
    /**
     * The options are presented in an answer box. Each sublist of options is shown as a group of labels while the main option is a button
     */
    private static HBox answerBox;
    /**
     * The question shown to the player
     */
    private static Text questionText;

    private static LoginPane loginPane;

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

//        StackPane rootStackPane = new GamePane(answerBox, questionText, model.getConfigurationID());
        StackPane rootStackPane = new GamePane(answerBox, questionText, 0);

        masterScene.setRoot(rootStackPane);

    }

    /**
     * Method called  by {@linkplain MatchState} when there is an update.
     */
    public static void notifyUpdatedState() {
        Platform.runLater(
                () -> HallPane.updateHall(2)
        );
    }

    /**
     * Asks the player to choose a powerup
     *
     * @param optionKeys
     * @return
     */
    public static int choosePowerup(List<List<String>> optionKeys) {
        return choosePowerupWithQuestion(optionKeys, R.string("askPowerup"));
    }

    /**
     * General method used to ask to choose a powerup using the given question.
     * @param optionKeys
     * @param question
     * @return
     */
    public static int choosePowerupWithQuestion(List<List<String>> optionKeys, String question) {
        answerGiven = false;
        GamePane.changeAnswerBoxCards(optionKeys);
        questionText.setText(question);
        return 0;
    }
    public static int chooseEffectSequence(List<List<String>> optionKeys) {
        return 0;
    }

    public static int chooseSpawn(List<List<String>> optionKeys) {
        answerGiven = false;
        GamePane.changeAnswerBoxCards(optionKeys);
        questionText.setText(R.string("askSpawn"));
        return 0;
    }

    public static int chooseWeapon(List<List<String>> optionKeys) {
        return chooseWeaponWithQuestion(optionKeys, R.string("askWeapon"));
    }

    public static int chooseWeaponWithQuestion(List<List<String>> optionKeys, String question) {
        answerGiven = false;
        GamePane.changeAnswerBoxCards(optionKeys);
        questionText.setText(question);
        return 0;
    }

    public static int chooseDestination(List<List<String>> optionKeys) {
        answerGiven = false;
        GamePane.changeAnswerBoxSquares(optionKeys);
        questionText.setText(R.string("askSquare"));
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
        answerGiven = false;
        GamePane.changeAnswerSimpleOptions(optionKeys);
        questionText.setText(R.string("askAction"));
        return 0;
    }

    public static void setAnswer(int answer) {
        GUI.answer = answer;
    }

    public static int chooseUseTagBack(List<List<String>> optionKeys) {
        answerGiven = false;
        GamePane.changeAnswerSimpleOptions(optionKeys);
        questionText.setText(R.string("askTagback"));
        return 0;
    }

    public static int chooseTarget(List<List<String>> optionKeys) {
        return 0;
    }

    public static void setController(ClientController controller) {
        controllerGUI = controller;
    }

    public static String askName() {
        String input = loginPane.getInputUsername();

        if (input.length() > 1 && input.length() < 18) {
            return input;
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
        inputUsername = new TextField();
        loginPane = new LoginPane(controllerGUI);
        masterScene = new Scene(loginPane, 1000, 1000);
    }

    public static MatchState getModel() {
        return model;
    }
}
