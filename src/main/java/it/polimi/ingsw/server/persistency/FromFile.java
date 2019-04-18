package it.polimi.ingsw.server.persistency;

import it.polimi.ingsw.server.model.cards.AmmoCard;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class FromFile {
    private static final String AMMO_CARDS =
            "./resources/cards/jsons/ammocards.json";

    private static AmmoCardLoader ammoCardLoader;

    private FromFile() {
    }

    public static synchronized BasicLoader<AmmoCard> ammoCards() {
        if (ammoCardLoader == null)
            ammoCardLoader = new AmmoCardLoader(AMMO_CARDS);
        return ammoCardLoader;
    }
}
