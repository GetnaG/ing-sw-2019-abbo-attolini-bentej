package it.polimi.ingsw.server;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.board.AbstractSquare;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.*;

/**
 * 
 */
public interface ToClientInterface {

    /**
     * 
     */
    //TODO ugly workaround
    public SuspensionListener listener = null;

    /**
     * @param options 
     * @return
     */
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options);

    /**
     * @param option 
     * @return
     */
    public PowerupCard chooseSpawn(List<PowerupCard> option);

    /**
     * @param options 
     * @return
     */
    public PowerupCard choosePowerup(List<PowerupCard> options);

    /**
     * @param options 
     * @return
     */
    public AbstractSquare chooseDestination(List<AbstractSquare> options);

    /**
     * @param options 
     * @return
     */
    public WeaponCard chooseWeaponCard(List<WeaponCard> options);

    /**
     * @param options 
     * @return
     */
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options);

    /**
     * @param options 
     * @return
     */
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options);

    /**
     * @param options 
     * @return
     */
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options);

    /**
     * @param options 
     * @return
     */
    public Action chooseAction(List<Action> options);

    /**
     * @param options 
     * @return
     */
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options);

    /**
     * @param options 
     * @return
     */
    public PowerupCard askUseTagback(List<PowerupCard> options);

}