package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.List;

/**
 * Represents the Shoot step in an Action.
 *
 * @author Fahed Ben Tej
 * @see EffectInterface
 * @see Action
 */
public class Shoot implements EffectInterface {

    /**
     * The subject of the turn
     */
    private Player player;
    /**
     * If the player has loaded weapons, asks him to choose a weapon to use.
     * Then asks which effect of that weapon he wants to use and runs it.
     * @param subjectPlayer     the subject of the turn
     * @param board             the board used in the game
     * @param alredyTargeted    the targets already hitted in the player's turn.
     */
    public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        this.player = subjectPlayer;
       if (subjectPlayer.getLoadedWeapons().isEmpty())
            return;

        WeaponCard weaponChosen = subjectPlayer.getToClient().chooseWeaponCard(
                subjectPlayer.getLoadedWeapons()
        );

        EffectInterface effectChosen = subjectPlayer.getToClient().chooseEffectsSequence(
                weaponChosen.getPossibleSequences()
        );

        effectChosen.runEffect(subjectPlayer, board, alredyTargeted);
    }

    /**
     * Returns the name of the effect : "Shoot"
     * @return
     */
    public String getName() {
        return "Shoot";
    }

    /**
     * //FIXME
     * Not used in Shoot.
     * @return  null
     */
    public EffectInterface getDecorated() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToChain(EffectInterface last) {

    }

    /**
     * //TODO
     *
     * @return
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        return null;
    }
}