package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.resources.R;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.List;

public class SquarePane extends StackPane {

    private ImageView imgSquare;
    private Text players;

    public SquarePane(String resImage) {
        players = new Text();
        imgSquare = getCell(resImage);
        this.getChildren().add(0, imgSquare);
        this.getChildren().add(1, players);

        imgSquare.setOnMouseEntered(e -> {
            imgSquare.setOpacity(0.5);
        });
        imgSquare.setOnMouseExited(e -> {
            imgSquare.setOpacity(1);
        });
    }

    public static ImageView getCell(String resID) {
        ImageView img = new ImageView(R.image(resID));
        img.setPreserveRatio(true);
        img.setFitHeight(150);
        return img;
    }

    public void insertPlayerInSquare(String player) {

        players.setText(players.getText() + "\n" + player);
    }

    public void flushState() {
        players.setText("");
    }

    public ImageView getImgSquare() {
        return imgSquare;
    }
}
