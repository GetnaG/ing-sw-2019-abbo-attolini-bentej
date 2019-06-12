package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.clientlogic.PlayerState;
import it.polimi.ingsw.client.resources.R;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class MapPane extends GridPane {

    public MapPane(int mapID) {
        super();

        switch (mapID) {
            case 0:
                buildMap0();
                break;
            case 1:
                buildMap1();
                break;
            case 2:
                buildMap2();
                break;
            case 3:
                buildMap3();
                break;
        }
        this.setAlignment(Pos.CENTER);
    }

    public static SquarePane getCell(String resID) {
        return new SquarePane(resID);
    }

    private void buildMap0() {

        this.add(getCell("1-x1y3"), 0, 0);
        this.add(getCell("1-x2y3"), 1, 0);
        this.add(getCell("1-x3y3"), 2, 0);
        this.add(getCell("1-x4y3"), 3, 0);
        this.add(getCell("1-x1y2"), 0, 1);
        this.add(getCell("1-x2y2"), 1, 1);
        this.add(getCell("1-x3y2"), 2, 1);
        this.add(getCell("1-x4y2"), 3, 1);
        this.add(getCell("1-x1y1"), 0, 2);
        this.add(getCell("1-x2y1"), 1, 2);
        this.add(getCell("1-x3y1"), 2, 2);
        this.add(getCell("1-x4y1"), 3, 2);
        this.setWidth(1500);
    }

    private void buildMap1() {

        this.add(getCell("1-x1y3"), 0, 0);
        this.add(getCell("1-x2y3"), 1, 0);
        this.add(getCell("2-x3y3"), 2, 0);
        this.add(getCell("2-x4y3"), 3, 0);
        this.add(getCell("1-x1y2"), 0, 1);
        this.add(getCell("1-x2y2"), 1, 1);
        this.add(getCell("2-x3y2"), 2, 1);
        this.add(getCell("2-x4y2"), 3, 1);
        this.add(getCell("1-x1y1"), 0, 2);
        this.add(getCell("1-x2y1"), 1, 2);
        this.add(getCell("2-x3y1"), 2, 2);
        this.add(getCell("2-x4y1"), 3, 2);

        this.setWidth(1500);
    }

    private void buildMap2() {
        this.add(getCell("2-x1y3"), 0, 0);
        this.add(getCell("2-x2y3"), 1, 0);
        this.add(getCell("2-x3y3"), 2, 0);
        this.add(getCell("2-x4y3"), 3, 0);
        this.add(getCell("2-x1y2"), 0, 1);
        this.add(getCell("2-x2y2"), 1, 1);
        this.add(getCell("2-x3y2"), 2, 1);
        this.add(getCell("2-x4y2"), 3, 1);
        this.add(getCell("2-x1y1"), 0, 2);
        this.add(getCell("2-x2y1"), 1, 2);
        this.add(getCell("2-x3y1"), 2, 2);
        this.add(getCell("2-x4y1"), 3, 2);
        this.setWidth(1500);
    }

    private void buildMap3() {
        this.add(getCell("2-x1y3"), 0, 0);
        this.add(getCell("2-x2y3"), 1, 0);
        this.add(getCell("1-x3y3"), 2, 0);
        this.add(getCell("1-x4y3"), 3, 0);
        this.add(getCell("2-x1y2"), 0, 1);
        this.add(getCell("2-x2y2"), 1, 1);
        this.add(getCell("1-x3y2"), 2, 1);
        this.add(getCell("1-x4y2"), 3, 1);
        this.add(getCell("2-x1y1"), 0, 2);
        this.add(getCell("2-x2y1"), 1, 2);
        this.add(getCell("1-x3y1"), 2, 2);
        this.add(getCell("1-x4y1"), 3, 2);
        this.setWidth(1500);
    }

    public SquarePane getCellByID(int id) {
        return (SquarePane) this.getChildren().get(id);
    }

    public void insertPlayerInCell(String nicknamePlayer, int cellID) {
        getCellByID(cellID).insertPlayerInSquare(nicknamePlayer);
    }

    public void update() {
        for (int i = 0; i < 12; i++) {
            getCellByID(i).flushState();
        }
        List<PlayerState> playersStateList = GUI.getModel().getPlayersState();

        for (PlayerState state : playersStateList) {
            getCellByID(state.getSquarePosition())
                    .insertPlayerInSquare(state.getNickname());
        }
    }
}
