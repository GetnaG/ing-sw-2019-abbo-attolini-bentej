package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AmmoCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TurretSquareTest {

    @Test
    @Disabled
    void removeAmmoCard() {
        TurretSquare testTS = new TurretSquare(new AmmoCard(), null);
        Square listener = new Square();
        testTS.setReplacer(listener.replacer);
        AmmoCard card1 = testTS.removeAmmoCard();
        listener.replacer.replaceAll();
        AmmoCard card2 = testTS.getAmmoCard();

        assert(card1!=card2);



    }
}