package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.SpawnSquare;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A player can grab any Grabbable object. A Grabbable object can be an AmmoCard
 * or a Turret.
 *
 * @author Fahed Ben Tej
 * @see it.polimi.ingsw.server.model.cards.AmmoCard
 * @see it.polimi.ingsw.server.model.board.Turret
 */
public class Grab implements EffectInterface {


    /**
     * Runs Grab effect for the specified player.
     *
     * @param subjectPlayer  the player who calls this effect
     * @param allTargets     all the targets on the board
     * @param board          the game board (for applying the effect)
     * @param allTargeted    a list of elements already targeted by this chain
     * @param damageTargeted a list of the elements that have received damage
     */
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> allTargeted, List<Damageable> damageTargeted){
        Square position = subjectPlayer.getPosition();

        // We can grab an AmmoCard if there's an AmmoTile
        if(position.getAmmoCard() != null){
            AmmoCard card = position.getAmmoCard();
            subjectPlayer.addAmmo(card.getCubes());
            // If the tile depicts a powerup card, draw one.
            if (card.hasPowerup() && subjectPlayer.getAllPowerup().size() >= 3){
                subjectPlayer.addPowerup(board.getPowerupCard());
            }
        }

        try {
            // We can grab a Weapon if there's a Market in our position
            if (position.getRoom().getSpawnSquare().equals(position)) {
                //get weapons in market and filter only the affordable ones
                List<WeaponCard> weaponAvailable = position.getRoom().getSpawnSquare().getMarket().getCards();
                List<WeaponCard> weaponsAffordable = weaponAvailable.stream().filter(card -> subjectPlayer.canAfford(card.getCost(), true)).collect(Collectors.toList());
                // ask the player which weapon he wants to buy
                WeaponCard weaponToBuy = subjectPlayer.getToClient().chooseWeaponToBuy(weaponsAffordable);
                // If he has 3 weapons, he has to discard one.
                if (subjectPlayer.getNumOfWeapons() >= 3) {
                    WeaponCard weaponToDiscard = subjectPlayer.getToClient().chooseWeaponToDiscard(subjectPlayer.getAllWeapons());
                    subjectPlayer.discard(weaponToDiscard);
                }
                // The player buys the weapon with the chosen powerup eventually
                List<PowerupCard> powerupToPay = List.of(subjectPlayer.getToClient().choosePowerupForPaying(subjectPlayer.getAllPowerup()));
                subjectPlayer.buy(weaponToBuy, powerupToPay);
            }
        } catch (ToClientException e) {
            //TODO Handle if the user is disconnected
        }
        if(position.getTurret() != null){
            // we can grab a Turret //TODO Implement Turret which is an extra-feature
        }
    }

    /**
     * Return the name name of the Effect: "Grab"
     * @return
     */
    public String getName() {
        return "Grab";
    }

    /**
     * Has no decorated.
     * @return null
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
     * Has no iterator
     * @return null
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        return null;
    }
}