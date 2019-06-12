package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.resources.R;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.List;

public class SquarePane extends StackPane {

    private static ImageView imgSquare;
    private static Text players;

    public SquarePane(String resImage) {
        players = new Text();
        imgSquare = getCell(resImage);
        this.getChildren().add(0, imgSquare);
        this.getChildren().add(1, players);
    }

    public static ImageView getCell(String resID) {
        ImageView img = new ImageView(R.image(resID));
        img.setPreserveRatio(true);
        img.setFitHeight(150);
        img.setOnMouseEntered(e -> {
            img.setOpacity(0.5);
        });
        img.setOnMouseExited(e -> {
            img.setOpacity(1);
        });
        return img;
    }

    public void insertPlayerInSquare(String player) {
        players.setText(players.getText() + "\n" + player);
    }

    public void flushState() {
        players.setText("");
    }

}
