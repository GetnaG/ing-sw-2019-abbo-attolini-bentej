package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.SpawnSquare;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.board.WeaponMarket;
import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
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
        AmmoCard card = position.getAmmoCard();
        if(card != null){
            subjectPlayer.addAmmo(card.getCubes());
            // If the tile depicts a powerup card, draw one.
            if (card.hasPowerup() && subjectPlayer.getAllPowerup().size() >= 3){
                subjectPlayer.addPowerup(board.getPowerupCard());
            }
            board.putAmmoCard(card);
            return;
        }

        try {
            // We can grab a Weapon if there's a Market in our position
            if (position.getRoom().getSpawnSquare().equals(position)) {
                //get weapons in market
                SpawnSquare spawnSquare = position.getRoom().getSpawnSquare();
                List<WeaponCard> weaponAvailable = spawnSquare.getMarket().getCards();

                /*Adding the weapons that the player can buy to weaponsAffordable*/
                List<WeaponCard> weaponsAffordable = weaponAvailable.stream()
                        .filter(weaponCard -> subjectPlayer.canAfford(weaponCard.getCost(), true) ||
                                !subjectPlayer.canAffordWithPowerups(weaponCard.getCost(), true).isEmpty())
                        .collect(Collectors.toList());

                /*If there are no weapons it is player's mistake and he grabs nothing*/
                if (weaponsAffordable.isEmpty()) {
                    return;
                }

                // ask the player which weapon he wants to buy
                WeaponCard weaponToBuy = subjectPlayer.getToClient().chooseWeaponToBuy(weaponsAffordable);
                // If he has 3 weapons, he has to discard one.
                if (subjectPlayer.getNumOfWeapons() >= 3) {
                    WeaponCard weaponToDiscard = subjectPlayer.getToClient().chooseWeaponToDiscard(subjectPlayer.getAllWeapons());
                    subjectPlayer.discard(weaponToDiscard);
                    spawnSquare.getMarket().addCard(weaponToDiscard);
                }

                /*If necessary, choosing a powerup to pay*/
                PowerupCard powerupToPay = null;
                if (!subjectPlayer.canAfford(weaponToBuy.getCost(), true)) {
                    powerupToPay = subjectPlayer.getToClient().choosePowerupForPaying(
                            subjectPlayer.canAffordWithPowerups(weaponToBuy.getCost(), true));
                }

                // The player buys the weapon with the chosen powerup
                spawnSquare.pickWeapon(weaponToBuy);
                subjectPlayer.buy(weaponToBuy, powerupToPay);
            }
        } catch (ToClientException e) {
            //TODO Handle if the user is disconnected
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