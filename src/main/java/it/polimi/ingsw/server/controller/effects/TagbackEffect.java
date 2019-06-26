package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ChoiceRefusedException;
import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.persistency.FromFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A tagback effect can be used when a damageable receives a point of damage;
 * this damageable gives a mark back to the player who hit him.
 *
 * @author Abbo Giulio A.
 */
public class TagbackEffect implements EffectInterface {
    /**
     * The name of this effect.
     */
    private String id;

    /**
     * Creates a tagback effect with the provided id.
     *
     * @param id the name of the effect
     */
    public TagbackEffect(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This checks all the targets that were damaged by the subject; to each
     * of them who has a tagback powerup and sees the subject asks whether to
     * use it and applies its effect.
     *
     * @param subjectPlayer  the player who dealt the damage
     * @param damageTargeted the targets that received damage by the subject
     */
    @Override
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board,
                          List<Damageable> allTargeted, List<Damageable> damageTargeted)
            throws ToClientException {
        for (Damageable d : damageTargeted) {

            /* Only Players can use a tagback.
             * This could have been solved by adding yet another parameter to
             * runEffect with all the players on the board, but it looks
             * already too crowded up there.
             * Sorry kittens.
             */
            if (d instanceof Player) {
                Player player = (Player) d;
                List<PowerupCard> tagbacks = player.getAllPowerup().stream()
                        .filter(e -> e.equals(FromFile.powerups().get("tagbackGrenade")))
                        .collect(Collectors.toList());
                if (!tagbacks.isEmpty()) {
                    try {
                        PowerupCard chosen = player.getToClient().askUseTagback(tagbacks);
                        player.removePowerup(chosen);
                        board.putPowerupCard(chosen);

                        /*Applying the effect*/
                        subjectPlayer.giveMark(Collections.singletonList(player));
                    } catch (ChoiceRefusedException e) {
                        /*Nothing to recover from*/
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return id;
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
