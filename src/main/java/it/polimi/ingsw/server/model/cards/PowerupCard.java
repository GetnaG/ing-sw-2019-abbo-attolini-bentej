package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.List;

/**
 * This represents a powerup card, with an effect and a cube.
 * Objects of this class are instantiated by a
 * {@link it.polimi.ingsw.server.persistency.PowerupLoader}.
 *
 * @author Abbo Giulio A.
 * @see it.polimi.ingsw.server.persistency.PowerupLoader
 * @see AmmoCube
 * @see EffectInterface
 */
public class PowerupCard extends AbstractCard {
    /**
     * Whether this is usable when choosing an action.
     */
    private boolean usableAsAction;

    /**
     * Whether this is usable when dealing damage.
     */
    private boolean usableOnDealingDamage;

    /**
     * Whether this is usable when receiving damage.
     */
    private boolean usableOnReceivingDamage;

    /**
     * The id associated with the effect of this card.
     */
    private String effectId;

    /**
     * The cube that this class has.
     */
    private AmmoCube cube;

    /**
     * Constructor used for testing, other fields are initialized to true.
     *
     * @param id       the id to locate the resources for this object
     * @param effectId
     * @param cube     the cube of this card
     */
    public PowerupCard(String id, String effectId, AmmoCube cube) {
        super(id);
        this.cube = cube;
        this.effectId = effectId;

        usableAsAction = true;
        usableOnDealingDamage = true;
        usableOnReceivingDamage = true;
    }

    /**
     * Returns the effect associated with this card.
     *
     * @return the effect associated with this card
     */
    public EffectInterface getEffect() {
        //FIXME after effects are implemented
        return new EffectInterface() {

            @Override
            public Iterator<EffectInterface> iterator() {
                return null;
            }

            @Override
            public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
            }

            @Override
            public String getName() {
                return effectId;
            }

            @Override
            public EffectInterface getDecorated() {
                return null;
            }
        };
    }

    /**
     * Whether this powerup can be used when choosing an action.
     * If true, then this can be used before or after action such as "run",
     * "grab" and "shoot".
     *
     * @return true if this can be used as an action
     */
    public boolean isUsableAsAction() {
        return usableAsAction;
    }

    /**
     * Whether this powerup can be used when dealing damage.
     *
     * @return true if this can be used when dealing damage
     */
    public boolean isUsableOnDealingDamage() {
        return usableOnDealingDamage;
    }

    /**
     * Whether this powerup can be used when receiving damage.
     *
     * @return true if this can be used when receiving damage
     */
    public boolean isUsableOnReceivingDamage() {
        return usableOnReceivingDamage;
    }

    /**
     * Returns the cube of this card.
     * A cube can be used for paying and for spawning.
     *
     * @return the cube associated with this card
     */
    public AmmoCube getCube() {
        return cube;
    }
}