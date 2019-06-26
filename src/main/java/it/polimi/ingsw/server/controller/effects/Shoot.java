package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param allTargets
     * @param board             the board used in the game
     * @param alredyTargeted    the targets already hitted in the player's turn.
     * @param damageTargeted
     */
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> alredyTargeted, List<Damageable> damageTargeted) throws ToClientException {
        this.player = subjectPlayer;
        if (subjectPlayer.getLoadedWeapons().isEmpty())
            return;

        WeaponCard weaponChosen = subjectPlayer.getToClient().chooseWeaponCard(
                subjectPlayer.getLoadedWeapons());

        Action effectChosen = subjectPlayer.getToClient().chooseEffectsSequence(
                weaponChosen.getPossibleSequences().stream().filter(s ->
                        subjectPlayer.canAfford(s.getTotalCost(), false)
                        //TODO: question: can I pay for an effect with powerups?
                ).collect(Collectors.toList()));

        subjectPlayer.pay(effectChosen.getTotalCost());
        effectChosen.runAll(subjectPlayer, allTargets, board, alredyTargeted, damageTargeted);

        //TODO tagback and targeting scope
    }

    /**
     * Returns the name of the effect : "Shoot"
     * @return
     */
    public String getName() {
        return "Shoot";
    }

    /**
     * This effect has no cost.
     *
     * @return an empty list
     */
    @Override
    public List<AmmoCube> getCost() {
        return new ArrayList<>();
    }
}