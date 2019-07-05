package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ChoiceRefusedException;
import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.persistency.FromFile;

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
     * If the player has loaded weapons, asks him to choose a weapon to use.
     * Then asks which effect of that weapon he wants to use and runs it.
     *
     * @param subjectPlayer   the subject of the turn
     * @param allTargets      all the targets on the board
     * @param board           the board used in the game
     * @param alreadyTargeted the targets already hit in the player's turn
     * @param damageTargeted  the targets that received damage
     */
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> alreadyTargeted, List<Damageable> damageTargeted) throws ToClientException {
        if (subjectPlayer.getLoadedWeapons().isEmpty())
            return;

        WeaponCard weaponChosen = subjectPlayer.getToClient().chooseWeaponCard(
                subjectPlayer.getLoadedWeapons());
        List<Action> effectsPlayerCanAfford = weaponChosen.getPossibleSequences().stream().filter(s ->
                subjectPlayer.canAfford(s.getTotalCost(), false)).collect(Collectors.toList());
        if (effectsPlayerCanAfford.isEmpty()) return;

        Action effectChosen = subjectPlayer.getToClient().chooseEffectsSequence(effectsPlayerCanAfford);

        subjectPlayer.pay(effectChosen.getTotalCost());
        effectChosen.runAll(subjectPlayer, allTargets, board, alreadyTargeted, damageTargeted);
        subjectPlayer.unload(weaponChosen);

        /*Checking if the subject has and wants to use a targeting scope*/
        List<PowerupCard> targetingScopes = subjectPlayer.getAllPowerup().stream()
                .filter(PowerupCard::isUsableOnDealingDamage)
                .filter((powerupCard -> subjectPlayer.canAfford(powerupCard.getEffect().getCost(), false)))
                .collect(Collectors.toList());
        if (!targetingScopes.isEmpty()) {
            try {
                PowerupCard chosen = subjectPlayer.getToClient().choosePowerup(targetingScopes);
                //TODO pay price
                subjectPlayer.removePowerup(chosen);
                board.putPowerupCard(chosen);
                chosen.getEffect().runEffect(subjectPlayer, allTargets, board, alreadyTargeted, damageTargeted);
            } catch (ChoiceRefusedException e) {
                /*The player does not want to use the powerups: continuing.*/
            }
        }

        /*Checking if someone can use tagback*/
        FromFile.effects().get("tagbackGrenade").runEffect(subjectPlayer,
                allTargets, board, alreadyTargeted, damageTargeted);
    }

    /**
     * Returns the name of the effect : "Shoot"
     *
     * @return "shoot"
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