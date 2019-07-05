package it.polimi.ingsw.client.interaction.GUI;

import it.polimi.ingsw.client.clientlogic.PlayerState;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the Map of the game using a Grid.
 */
public class MapPane extends GridPane {
    private static int map;

    public MapPane(int mapID) {
        super();
        map = mapID;
        buildMap(mapID);
        this.setAlignment(Pos.CENTER);
    }

    public static SquarePane getCell(String resID) {
        return new SquarePane(resID);
    }

    private void buildMap(int mapID) {
        this.getChildren().removeAll(this.getChildren());
        switch (mapID) {
            case 0: //STD1
                buildMapStd1();
                break;
            case 1: //ADVISED34
                buildMapAdv34();
                break;
            case 2:
                buildMapStd2(); // STD2
                break;
            case 3: // ADVISED45
                buildMapAdv45();
                break;
        }
    }

    private void buildMapStd1() {

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

    private void buildMapAdv45() {

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

    private void buildMapStd2() {
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

    private void buildMapAdv34() {
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

    /**
     *
     */
    public void update() {
        if (map != GUI.getModel().getConfigurationID()) {
            buildMap(GUI.getModel().getConfigurationID());
            map = GUI.getModel().getConfigurationID();
        }
        for (int i = 0; i < 12; i++) {
            getCellByID(i).flushState();
        }
        List<PlayerState> playersStateList = GUI.getModel().getPlayersState();

        for (PlayerState state : playersStateList) {
            if (state.getSquarePosition() == -1)
                continue;
            getCellByID(state.getSquarePosition())
                    .insertPlayerInSquare(state.getNickname());
        }

        for (int i = 0; i < 12; i++) {
            SquarePane sp = getCellByID(i);
            int squareID = i;
            sp.setOnMouseEntered(e -> {
                sp.getImgSquare().setOpacity(0.5);
                if (squareID != 2 && squareID != 4 && squareID != 11) {
                    String cardKeyPowerup = GUI.getModel().getAmmoCardsID().get(squareID);
                    if (!cardKeyPowerup.equals("notSet")) {
                        GamePane.updateRightGeneralTag(List.of(cardKeyPowerup), "The square contains:");
                    }
                } else {
                    int indexStart = 0;
                    switch (squareID) {
                        case 2:
                            indexStart = 0;
                            break;
                        case 4:
                            indexStart = 3;
                            break;
                        case 11:
                            indexStart = 6;
                            break;
                        default:
                            break;
                    }
                    List<String> weaponCardsMarketKeys = GUI.getModel().getWeaponsCardsID().stream().filter(s -> !s.equals("notSet")).collect(Collectors.toList());
                    indexStart = indexStart % weaponCardsMarketKeys.size();
                    int indexEnd = (indexStart + 3) % weaponCardsMarketKeys.size();
                    if (indexEnd == 0) indexEnd = 9;
                    if (indexEnd > indexStart)
                        GamePane.updateRightGeneralTag(weaponCardsMarketKeys.subList(indexStart, indexEnd), "The square contains:");
                }
            });

            sp.setOnMouseExited(e -> {
                sp.getImgSquare().setOpacity(1);
                GamePane.update();
            });
        }
    }


}
