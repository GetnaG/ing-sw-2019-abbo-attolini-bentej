package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponMarketTest {

    @Test
    void addCard() {
        //1//
        List<WeaponCard> listWC = new ArrayList<>();
        List<AmmoCube> cubes = new ArrayList<>();
        listWC.add(new WeaponCard("001", cubes, null, false ));
        listWC.add(new WeaponCard("002", cubes, null, false ));
        listWC.add(new WeaponCard("003", cubes, null, false ));

        WeaponMarket wmTest = new WeaponMarket(listWC);

        listWC.set(1, null);

        wmTest.addCard(new WeaponCard("004", cubes, null, false ));

        assertEquals(wmTest.getCards().size() , 3);

        //2//
        wmTest.addCard(new WeaponCard("005", cubes, null, false ));

        assertEquals(wmTest.getCards().size() , 3);
    }

    @Test
    void pickWeaponFromList() {
        //1//
        List<WeaponCard> listWC = new ArrayList<>();
        List<AmmoCube> cubes = new ArrayList<>();
        listWC.add(new WeaponCard("001", cubes, null, false ));
        listWC.add(new WeaponCard("002", cubes, null, false ));
        listWC.add(new WeaponCard("003", cubes, null, false ));

        WeaponMarket wmTest = new WeaponMarket(listWC);

        wmTest.pickWeaponFromList(listWC.get(1));

        assertNull(wmTest.getCards().get(1));

        //2//
        List<WeaponCard> list2 = new ArrayList<>();
        WeaponCard a = new WeaponCard("004", cubes, null, false );
        list2.add(new WeaponCard("001", cubes, null, false ));
        list2.add(new WeaponCard("002", cubes, null, false ));
        list2.add(new WeaponCard("003", cubes, null, false ));

        WeaponMarket wmTest2 = new WeaponMarket(list2);

        wmTest.pickWeaponFromList(a);

        assertTrue(!wmTest2.isValidWeapon(a));

    }
}