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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A player can grab either an {@link AmmoCard} or a {@link WeaponCard}.
 * The items depicted in the ammo card are added to the player and it is
 * discarded; if the player is in a {@link Square} with a
 * {@link it.polimi.ingsw.server.model.board.WeaponMarket} then he can choose
 * a weapon to buy, if necessary a weapon to put down and a powerup to pay
 * the cost.
 *
 * @author Fahed Ben Tej
 * @author Abbo Giulio A.
 * @see AmmoCard
 * @see WeaponCard
 */
public class Grab implements EffectInterface {

    /**
     * Runs the Grab effect for the specified player.
     *
     * @param subjectPlayer  the player who calls this effect
     * @param allTargets     not used, can be null
     * @param board          the game board
     * @param allTargeted    not used, can be null
     * @param damageTargeted not used, can be null
     */
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board,
                          List<Damageable> allTargeted, List<Damageable> damageTargeted)
            throws ToClientException {

        /*We can grab an AmmoCard if there's an AmmoTile*/
        Square position = subjectPlayer.getPosition();
        AmmoCard card = position.getAmmoCard();
        if (card != null) {
            subjectPlayer.addAmmo(card.getCubes());

            /*If the tile depicts a powerup card, draw one*/
            if (card.hasPowerup() && subjectPlayer.getAllPowerup().size() < 3)
                subjectPlayer.addPowerup(board.getPowerupCard());

            /*Recycling the ammo card*/
            board.putAmmoCard(card);
            return;
        }

        /*We can grab a Weapon if there's a Market in our position*/
        SpawnSquare spawnSquare = position.getRoom().getSpawnSquare();
        if (spawnSquare.equals(position)) {

            /*Getting the weapons in market*/
            List<WeaponCard> weaponAvailable = spawnSquare.getMarket().getCards();
            weaponAvailable = weaponAvailable.stream().filter(w -> w != null).collect(Collectors.toList());

            /*Adding the weapons that the player can buy to weaponsAffordable*/
            List<WeaponCard> weaponsAffordable = weaponAvailable.stream()
                    .filter(weaponCard -> subjectPlayer.canAfford(weaponCard.getCost(), true))
                    .collect(Collectors.toList());

            /*If there are no weapons it is player's mistake and he grabs nothing*/
            if (weaponsAffordable.isEmpty())
                return;

            /*Asking the player which weapon he wants to buy*/
            WeaponCard weaponToBuy =
                    subjectPlayer.getToClient().chooseWeaponToBuy(weaponsAffordable);

            /*If the player has 3 weapons, he has to discard one.*/
            WeaponCard weaponToDiscard = null;
            if (subjectPlayer.getNumOfWeapons() >= 3)
                weaponToDiscard = subjectPlayer.getToClient().chooseWeaponToDiscard(subjectPlayer.getAllWeapons());

            /*If necessary, choosing a powerup to pay*/
            PowerupCard powerupToPay = null;
            if (!subjectPlayer.canAfford(weaponToBuy.getCost(), true) && !subjectPlayer.canAffordWithPowerups(weaponToBuy.getCost(), true).isEmpty()) {
                powerupToPay = subjectPlayer.getToClient().choosePowerupForPaying(
                        subjectPlayer.canAffordWithPowerups(weaponToBuy.getCost(), true));
            }

            /*The player buys the weapon with the chosen powerup*/
            if (weaponToDiscard != null)
                subjectPlayer.discard(weaponToDiscard);
            spawnSquare.pickWeapon(weaponToBuy);
            subjectPlayer.buy(weaponToBuy, powerupToPay);
            if (weaponToDiscard != null)
                spawnSquare.getMarket().addCard(weaponToDiscard);
        }
    }

    /**
     * Returns the name of this Effect: "Grab"
     *
     * @return "Grab"
     */
    public String getName() {
        return "Grab";
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