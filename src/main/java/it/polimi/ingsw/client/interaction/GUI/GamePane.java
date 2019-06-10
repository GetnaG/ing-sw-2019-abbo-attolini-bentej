package it.polimi.ingsw.client.interaction.GUI;

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
import java.util.Map;

public class GamePane extends StackPane {


    private static HBox answerBox;
    private static Text questionText;
    private static MapPane map;

    public GamePane(HBox answerBox, Text questionText, int idMap) {
        super();
        this.answerBox = answerBox;
        BorderPane borderPane = new BorderPane();
        //map = new MapPane(idMap); //TODO Remove following line
        map = new MapPane(0);

        this.getChildren().add(borderPane);

        buildTop(borderPane);
        buildBottom(borderPane);
        buildCenter(borderPane);
        buildLeft(borderPane);
        buildRight(borderPane);

        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

    }

    /**
     * Build the Top part of the given border pane
     *
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
        vbox.setMinHeight(400);


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

        centerBorderPane.setAlignment(centerBorderPane, Pos.CENTER);


    }

    /**
     * Builds the left part of the given border pane
     *
     * @param borderPane
     */
    private static void buildLeft(BorderPane borderPane) {
        VBox vbox = new VBox();
        borderPane.setLeft(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER_LEFT);

        /*
        vbox.getChildren().add(getCard("AD_weapons_IT_024"));
        vbox.getChildren().add(getCard("AD_weapons_IT_025"));
        vbox.getChildren().add(getCard("AD_weapons_IT_026"));
        */


        GUI.getModel().getAmmoCardsID().forEach(ammoCardID -> vbox.getChildren().add(getCard(ammoCardID)));

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

    }

    /**
     * Build the right part of the given border pane
     *
     * @param borderPane
     */
    private static void buildRight(BorderPane borderPane) {
        VBox vbox = new VBox();
        borderPane.setRight(vbox);
        borderPane.setAlignment(vbox, Pos.CENTER_RIGHT);

        GUI.getModel().getWeaponsCardsID().forEach(weaponId -> vbox.getChildren().add(getCard(weaponId)));

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
    }

    private static Node getPlayerTile() {
        // load the image
        Image image = R.image("PlayerBoard");
        // if (GUI.getModel().getIsActionTileFrenzy()) TODO implementare

        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(700);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    private static Node getKillshotTrack() {
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
                Button groupNameButton = new Button(options.indexOf(group) + "");
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
            label.setTextFill(Color.WHITE);
            label.setFont(new Font("Arial", 15));
            label.setLineSpacing(5);
            label.setPadding(new Insets(5, 5, 5, 5));
        });

        return hbox;
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
}
