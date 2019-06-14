package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpawnSquareTest {

    @Test
    void pickWeapon() {
        List<WeaponCard> listWC = new ArrayList<>();
        List<AmmoCube> cubes = new ArrayList<>();
        listWC.add(new WeaponCard("001", cubes, null, false ));
        listWC.add(new WeaponCard("002", cubes, null, false ));
        listWC.add(new WeaponCard("003", cubes, null, false ));

        WeaponMarket wmTest = new WeaponMarket(listWC);

        SpawnSquare sqT = new SpawnSquare(AmmoCube.BLUE, wmTest);

        sqT.setReplacer(new GameBoard(null, new ArrayList<>()));

        sqT.pickWeapon(wmTest.getCards().get(1));

        assertNull(wmTest.getCards().get(1));
    }
}