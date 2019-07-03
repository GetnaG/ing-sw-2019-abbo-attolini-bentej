package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.clientlogic.PlayerState;
import it.polimi.ingsw.server.model.player.Player;
import javafx.scene.layout.StackPane;
import it.polimi.ingsw.client.resources.R;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GamePane extends StackPane {


    private static HBox answerBox;
    private static Text questionText;
    private static MapPane map;
    private static StackPane playerBoard;
    private static StackPane killshotTrack;
    /**
     * VBox used to store the WeaponCards
     */
    private static VBox vboxLeftCards;
    /**
     * VBox used to store the AmmoCards
     */
    private static VBox vboxRightCards;

    private static boolean ready = false;

    public GamePane(HBox answerBox, Text question, int idMap) {
        this.answerBox = answerBox;
        answerBox.setMinWidth(1680);
        answerBox.setMinHeight(400);
        if (questionText == null) questionText = new Text();
        BorderPane borderPane = new BorderPane();
        map = new MapPane(idMap);

        this.getChildren().add(borderPane);
        this.ready = true;
        buildTop(borderPane);
        buildBottom(borderPane);
        buildCenter(borderPane);
        buildLeft(borderPane);
        buildRight(borderPane);
        update();

        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

    }

    /**
     * Build the Top part of the given border pane
     *
     * @param borderPane
     */
    private static void buildTop(BorderPane borderPane) {
        PlayerState playerState = GUI.getPlayerState(GUI.getNickname());
        playerBoard = getPlayerBoard(playerState);
        killshotTrack = getKillshotTrack(GUI.getModel());
        HBox hbox = new HBox(killshotTrack, getPlayerActionTile(), playerBoard);
        borderPane.setTop(hbox);
        borderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(30);
    }

    /**
     * Build the Bottom part of the given border pane
     *
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

        vbox.setFillWidth(true);
    }

    /**
     * Builds the center part of the given border pane
     *
     * @param borderPane
     */
    private static void buildCenter(BorderPane borderPane) {
        BorderPane centerBorderPane = new BorderPane();
        borderPane.setCenter(centerBorderPane);

        centerBorderPane.setCenter(map);
        insertOtherPlayers(centerBorderPane);
    }

    /**
     * Builds the left part of the given border pane
     *
     * @param borderPane
     */
    private static void buildLeft(BorderPane borderPane) {
        vboxLeftCards = new VBox();
        borderPane.setLeft(vboxLeftCards);
        borderPane.setAlignment(vboxLeftCards, Pos.CENTER_LEFT);
        PlayerState currentPlayerState = GUI.getModel().getPlayersState().stream()
                .filter(p -> p.getNickname().equals(GUI.getNickname()))
                .collect(Collectors.toList()).get(0);
        updateWeaponCards(currentPlayerState.getLoadedWeapons(), currentPlayerState.getUnloadedWeapons());
        vboxLeftCards.setAlignment(Pos.CENTER);
        vboxLeftCards.setSpacing(10);
    }

    /**
     * Shows the list of loaded weapon cards to the player
     */
    private static void updateWeaponCards(List<String> loadedWeaponCardsIDs, List<String> unloadedWeaponCards) {
        if (vboxLeftCards == null) return;
        vboxLeftCards.getChildren().removeAll(vboxLeftCards.getChildren());
        Label topLabel = new Label(R.string("weapon"));
        topLabel.setFont(R.font("AllertaStencil-Regular.ttf", 20));
        topLabel.setTextFill(Color.WHITE);
        vboxLeftCards.getChildren().add(topLabel);
        loadedWeaponCardsIDs.forEach(ammoCardID -> vboxLeftCards.getChildren().add(getCard(ammoCardID, false)));
        unloadedWeaponCards.forEach(ammoCardID -> vboxLeftCards.getChildren().add(getCard(ammoCardID, true)));
        for (int i = loadedWeaponCardsIDs.size() + unloadedWeaponCards.size(); i < 3; i++) {
            vboxLeftCards.getChildren().add(getCard("refuse", false));
        }
    }

    /**
     * Build the right part of the given border pane
     *
     * @param borderPane
     */
    private static void buildRight(BorderPane borderPane) {
        vboxRightCards = new VBox();
        borderPane.setRight(vboxRightCards);
        borderPane.setAlignment(vboxRightCards, Pos.CENTER_RIGHT);
        PlayerState currentPlayerState = GUI.getModel().getPlayersState().stream()
                .filter(p -> p.getNickname().equals(GUI.getNickname()))
                .collect(Collectors.toList()).get(0);
        updatePowerupCards(currentPlayerState.getPowerups(), R.string("powerup"), true);
        vboxRightCards.setAlignment(Pos.CENTER);
        vboxRightCards.setSpacing(10);
    }

    /**
     * Shows the list of ammo cards to the player
     *
     * @param powerupCardIDs
     * @param title
     * @param isPowerup tells if we are updating a powerup or an on square movement
     */
    private static void updatePowerupCards(List<String> powerupCardIDs, String title, boolean isPowerup) {
        if (vboxRightCards != null) {
            vboxRightCards.getChildren().removeAll(vboxRightCards.getChildren());
            Label topLabel = new Label(title);
            topLabel.setFont(R.font("AllertaStencil-Regular.ttf", 20));
            topLabel.setTextFill(Color.WHITE);
            vboxRightCards.getChildren().add(topLabel);
            powerupCardIDs.forEach(powerupCardID -> vboxRightCards.getChildren().add(getCard(powerupCardID, false)));
            for (int i = powerupCardIDs.size(); i < 3 && isPowerup; i++) {
                vboxRightCards.getChildren().add(getCard("refuse", false));
            }
        }
    }

    public static void updateRightGeneralTag(List<String> cardIDs, String title) {
        updatePowerupCards(cardIDs, title, false);
    }
    private static StackPane getPlayerBoard(PlayerState playerState) {
        StackPane stack = new StackPane();
        // load the image
        Image image = R.image("PlayerBoard");
        // if (GUI.getModel().getIsActionTileFrenzy()) TODO implementare
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(600);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        GridPane gp = new GridPane();
        stack.getChildren().add(0, iv);
        stack.getChildren().add(1, gp);
        updatePlayerBoard(gp, playerState.getDamage(), playerState.getSkullNumber());
        for (int i = 0; i < 13; i++) {
            if (i == 0)
                gp.getColumnConstraints().add(new ColumnConstraints(48)); // column i is 49 wide
            else gp.getColumnConstraints().add(new ColumnConstraints(35));
        }
        for (int i = 0; i < 3; i++) {
            gp.getRowConstraints().add(new RowConstraints(70));
        }

        return stack;
    }

    private static StackPane getKillshotTrack(MatchState state) {
        StackPane stack = new StackPane();
        // load the image
        Image image = R.image("killshotTrack");
        // if (GUI.getModel().getIsActionTileFrenzy()) TODO implementare
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(500);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        GridPane gp = new GridPane();
        gp.setMaxWidth(500);
        gp.setMinWidth(500);
        gp.setMaxHeight(iv.getFitHeight());
        gp.setMinHeight(iv.getFitHeight());
        stack.getChildren().add(0, iv);
        stack.getChildren().add(1, gp);
        for (int i = 0; i < 9; i++) {
            if (i == 8)
                gp.getColumnConstraints().add(new ColumnConstraints(50)); // column i is 49 wide
            else gp.getColumnConstraints().add(new ColumnConstraints(50));
        }
        gp.getRowConstraints().add(new RowConstraints(iv.getFitHeight()));
        updateKillshotTrack(gp, GUI.getModel().getKillshotTrack());
        return stack;
    }

    private static void updateKillshotTrack(GridPane grid, List<List<String>> playersKillshot) {
        if (killshotTrack == null || playersKillshot == null) return;
        // Flushing previous state
        grid.getChildren().removeAll(grid.getChildren());
        // Inserting new state
        for (List<String> nicknameList : playersKillshot) {
            Text text = new Text("");
            text.setFill(Color.WHITE);
            nicknameList.forEach(name -> text.setText(text.getText() + "\n" + name));
            grid.add(text, playersKillshot.indexOf(nicknameList), 0);
        }
    }

    private static void updatePlayerBoard(GridPane grid, List<String> playersDamage, int timesKilled) {
        grid.getChildren().removeAll(grid.getChildren());
        grid.getColumnConstraints().removeAll(grid.getColumnConstraints());
        for (int i = 0; i < playersDamage.size(); i++) {
            Text text = new Text(playersDamage.get(i));
            text.setFill(Color.WHITE);
            text.setFont(new Font("Arial", 20));
            grid.add(text, i, 1);
        }

        for (int i = 0; i < timesKilled; i++) {
            Text text = new Text("  X");
            text.setFill(Color.WHITE);
            text.setFont(new Font("Arial", 20));
            grid.add(text, 2 + i, 2);
        }
        grid.setGridLinesVisible(true);
    }

    private static Node getPlayerActionTile() {
        Image image;
        // load the image
        if (GUI.getModel().getIsActionTileFrenzy())
            image = R.image("actionTileFrenzy");
        else
            image = R.image("ActionTile");

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(60);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private static Node getKillshotTrack() {
        Image image = R.image("killshotTrack");

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(60);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private static ImageView getCard(String imageName, boolean dark) {
        Image image = R.image(imageName);

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(80);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        if (dark)
            iv.setOpacity(0.5);
        return iv;
    }

    private static Node getQuestionBox(String question) {
        StackPane stackPane = new StackPane();
        Rectangle rectangle = new Rectangle(700, 50);

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
    public static void changeAnswerBoxSquares(List<List<String>> options) {
        Platform.runLater(() -> {
            answerBox.getChildren().removeAll(answerBox.getChildren());

            // Creating a GroupBoxAnswer for each option
            options.forEach(group -> {
                HBox groupAnswerBox = getGroupBoxAnswerSquares(group, options.indexOf(group));
                answerBox.getChildren().add(groupAnswerBox);
            });
            answerBox.setSpacing(10);
            answerBox.setAlignment(Pos.CENTER);
        });
    }

    private static HBox getGroupBoxAnswerSquares(List<String> group, int groupIndex) {
        HBox hbox = new HBox();
        // TOP GROUP Name
        Button groupNameButton = new Button(groupIndex + "");
        groupNameButton.setOnMouseClicked(e -> {
            GUI.setAnswer(groupIndex);
            GUI.setAnswerGiven(true);
        });
        hbox.getChildren().add(groupNameButton);

        groupNameButton.setOnMouseEntered(e -> {
            map.getCellByID(Integer.parseInt(group.get(0))).setOpacity(0.5);
        });
        groupNameButton.setOnMouseExited(e -> {
            map.getCellByID(Integer.parseInt(group.get(0))).setOpacity(1);
        });

        return hbox;
    }

    /**
     * Builds the options and puts them in the Answer Box
     *
     * @param options
     * @return
     */
    public static void changeAnswerSimpleOptions(List<List<String>> options) {
        Platform.runLater(() -> {
            ImageView imgBox = new ImageView();
            answerBox.getChildren().removeAll(answerBox.getChildren());

            // Creating a GroupBoxAnswer for each option
            options.forEach(group -> {
                HBox hbox = new HBox();
                // TOP GROUP Name
                Button groupNameButton = new Button(group.get(0));
                groupNameButton.setOnMouseClicked(e -> {
                    GUI.setAnswer(options.indexOf(group));
                    GUI.setAnswerGiven(true);
                });
                hbox.getChildren().add(groupNameButton);

                answerBox.getChildren().add(hbox);
            });

            answerBox.setSpacing(10);
            answerBox.setAlignment(Pos.CENTER);
        });

    }

    /**
     * Builds the options and puts them in the Answer Box
     *
     * @param options
     * @return
     */
    public static void changeAnswerBoxCards(List<List<String>> options) {
        Platform.runLater(() -> {
            ImageView imgBox = new ImageView();
            answerBox.getChildren().removeAll(answerBox.getChildren());

            // Creating a GroupBoxAnswer for each option
            options.forEach(group -> {
                HBox groupAnswerBox = getGroupBoxAnswerCards(group, imgBox, options.indexOf(group));
                answerBox.getChildren().add(groupAnswerBox);
            });

            answerBox.getChildren().add(imgBox);


            answerBox.setSpacing(10);
            answerBox.setAlignment(Pos.CENTER);
        });

    }

    private static void createCardAnimation(Node node, String resID, ImageView imgBox) {
        imgBox.setVisible(true);
        imgBox.setImage(getCard(resID, false).getImage());
        imgBox.setFitHeight(180);
        node.setStyle("-fx-font-weight: bold");
        node.setOnMouseEntered(e -> {
            imgBox.setVisible(true);
            imgBox.setImage(getCard(resID, false).getImage());
            imgBox.setFitHeight(180);
            node.setStyle("-fx-font-weight: bold");
        });
        node.setOnMouseExited(e -> {
            imgBox.setVisible(false);
            node.setStyle("-fx-font-weight: regular");
        });
    }

    private static HBox getGroupBoxAnswerCards(List<String> group, ImageView imgBox, int groupIndex) {
        HBox hbox = new HBox();
        // TOP GROUP Name
        Button groupNameButton = new Button(groupIndex + "");
        groupNameButton.setOnMouseClicked(e -> {
            GUI.setAnswer(groupIndex);
            GUI.setAnswerGiven(true);
        });

        hbox.getChildren().add(groupNameButton);

        // Creating a list of labels for each element of the group
        List<Label> labelsGroupElements = new ArrayList<>();
        // Adding label to list using the element as key to retrieve the name of the label
        group.forEach(element -> {
            Label label = new Label(R.string(element));
            labelsGroupElements.add(label);
            createCardAnimation(label, element, imgBox);
            hbox.getChildren().add(label);
            groupNameButton.setOnMouseEntered(e -> {
                imgBox.setVisible(true);
                imgBox.setImage(getCard(element, false).getImage());
                imgBox.setFitHeight(180);
            });
            groupNameButton.setOnMouseExited(e -> imgBox.setVisible(false));

            label.setTextFill(Color.WHITE);
            label.setFont(new Font("Arial", 15));
            label.setLineSpacing(5);
            label.setPadding(new Insets(5, 5, 5, 5));
        });


        return hbox;
    }

    private static void insertOtherPlayers(BorderPane borderPane) {
        List<String> otherPlayers = GUI.getModel().getPlayersState().stream()
                .map(playerState -> playerState.getNickname())
                .collect(Collectors.toList());

        HBox playersContainerTop = new HBox();
        HBox playersContainerLeft = new HBox();
        HBox playersContainerRight = new HBox();
        borderPane.setLeft(playersContainerLeft);
        borderPane.setRight(playersContainerRight);
        borderPane.setTop(playersContainerTop);
        for (String nickname : otherPlayers) {
            int index = otherPlayers.indexOf(nickname);
            if (index == 0)
                playersContainerLeft.getChildren().add(getCirclePlayer(nickname));
            else if (index >= 1 && index <= 3)
                playersContainerTop.getChildren().add(getCirclePlayer(nickname));
            else playersContainerRight.getChildren().add(getCirclePlayer(nickname));
        }

        playersContainerTop.setAlignment(Pos.CENTER);
        playersContainerTop.setSpacing(20);
        playersContainerLeft.setAlignment(Pos.CENTER);
        playersContainerLeft.setSpacing(20);
        playersContainerRight.setAlignment(Pos.CENTER);
        playersContainerRight.setSpacing(20);
    }

    private static Node getCirclePlayer(String nickname) {
        StackPane stackPane = new StackPane();
        Text text = new Text(nickname);
        Circle circle = new Circle(50, Color.TRANSPARENT);

        stackPane.getChildren().addAll(text, circle);

        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(5);
        text.setFill(Color.WHITE);

        stackPane.setOnMouseEntered(e -> {
            PlayerState hooveredPlayerState = GUI.getModel().getPlayersState().stream()
                    .filter(s -> s.getNickname().equals(text.getText()))
                    .collect(Collectors.toList()).get(0);
            updateWeaponCards(hooveredPlayerState.getLoadedWeapons(), hooveredPlayerState.getUnloadedWeapons());
            updatePowerupCards(hooveredPlayerState.getPowerups(), R.string("powerup"), true);
        });
        stackPane.setOnMouseExited(e -> {
            PlayerState clientPlayerState = GUI.getModel().getPlayersState().stream()
                    .filter(s -> s.getNickname().equals(GUI.getNickname()))
                    .collect(Collectors.toList()).get(0);
            updateWeaponCards(clientPlayerState.getLoadedWeapons(), clientPlayerState.getUnloadedWeapons());
            updatePowerupCards(clientPlayerState.getPowerups(), R.string("powerup"), true);
        });

        return stackPane;
    }

    public static void update() {
        if (!ready) return;
        PlayerState playerState = GUI.getPlayerState(GUI.getNickname());
        updatePowerupCards(playerState.getPowerups(), R.string("powerup"), true);
        updateWeaponCards(playerState.getLoadedWeapons(), playerState.getUnloadedWeapons());
        updatePlayerBoard((GridPane) playerBoard.getChildren().get(1),
                playerState.getDamage(),
                playerState.getSkullNumber());
        if (killshotTrack != null)
            updateKillshotTrack((GridPane) killshotTrack.getChildren().get(1), GUI.getModel().getKillshotTrack());
        map.update();
    }

    public static Text getQuestionText() {
        if (questionText == null) questionText = new Text();
        return questionText;
    }

    public static void setQuestionText(String question) {
        if (questionText == null) questionText = new Text();
        questionText.setText(question);
    }


    public static void setWaitForTurn() {
        Platform.runLater(() -> {
            questionText.setText(R.string("wait"));
            answerBox.getChildren().removeAll(answerBox.getChildren());
        });
    }
}
