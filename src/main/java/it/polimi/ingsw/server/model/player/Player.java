package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.ToClientInterface;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.io.CharArrayReader;
import java.util.*;

/**
 * 
 */
public class Player implements Damageable {
    private String nickname;
    private int score;
    private boolean firstPlayer;
    private String figureRes;
    private ToClientInterface toClient;
    private HandManager hand;
    private AmmoBox ammoBox;
    private PlayerBoardInterface playerBoard;
    private Square position;
    private Action adren1;
    private Action adren2;

    //si scorer quando muore

    public Player(String nickname) {
        this(nickname, false, null, null, null);
    }

    public Player(String nickname, boolean firstPlayer, String figureRes,
                  ToClientInterface toClient,
                  PlayerBoardInterface playerBoard) {
        this.nickname = nickname;
        this.firstPlayer = firstPlayer;
        this.figureRes = figureRes;
        this.toClient = toClient;
        this.playerBoard = playerBoard;

        score = 0;
        hand = new HandManager();
        ammoBox = new AmmoBox();

        //TODO: maybe take from a json as "default adrenaline actions"?
        adren1 = new Action();
        adren2 = new Action();
    }

    @Override
    public void giveDamage(List<Player> shooters) {
        playerBoard.addDamage(shooters);

    }

    @Override
    public void giveMark(List<Player> shooters) {
        playerBoard.addDamage(shooters);
    }

    @Override
    public Square getPosition() {
        return position;
    }

    @Override
    public void setPosition(Square newPosition) {
        position = newPosition;
    }

    @Override
    public void scoreAndResetDamage() {
        playerBoard.score();
        playerBoard.addSkull();
        playerBoard.resetDamage();
    }

    @Override
    public Player getKillshotPlayer() {
        return playerBoard.getKillshot();
    }

    @Override
    public Player getOverkillPlayer() {
        return playerBoard.getOverkill();
    }

    public List<Action> getAdrenalineActions() {
        List<Action> actions = new ArrayList<>();
        if (playerBoard.isAdr1Unlocked())
            actions.add(adren1);
        if (playerBoard.isAdr2Unlocked())
            actions.add(adren2);
        return actions;
    }

    public void addAmmo(List<AmmoCube> cubes) {
        ammoBox.addAmmo(cubes);
    }

    public void addPowerup(PowerupCard card) {
        hand.addPowerup(card);
    }

    public void removePowerup(PowerupCard card) {
        hand.removePowerup(card);
    }

    public List<PowerupCard> getAllPowerup() {
        return hand.getPowerups();
    }

    public int getNumOfPowerups() {
        return hand.getPowerups().size();
    }

    public boolean canAfford(WeaponCard card, boolean buying) {
        if (card.getCost().isEmpty())
            return true;
        return ammoBox.checkPrice((buying) ?
                card.getCost().subList(1, card.getCost().size()) :
                card.getCost());
    }

    public List<PowerupCard> canAffordWithPowerup(WeaponCard card,
                                                  boolean buying) {
        //TODO
        return null;
    }

    public void buy(WeaponCard card, List<WeaponCard> asCubes) {
        //TODO
    }

    public List<WeaponCard> getAllWeapons() {
        List<WeaponCard> allWeapons = hand.getLoadedWeapons();
        allWeapons.addAll(hand.getUnloadedWeapons());
        return allWeapons;
    }

    public void discard(WeaponCard card) {
        hand.removeWeapon(card);
    }

    public List<WeaponCard> getLoadedWeapons() {
        return hand.getLoadedWeapons();
    }

    public void unload(WeaponCard weapon) {
        hand.unload(weapon);
    }

    public List<WeaponCard> getReloadableWeapons() {
        return hand.getUnloadedWeapons();
    }

    public void reload(WeaponCard weapon) {
        hand.reload(weapon);
    }

    public boolean canAfford(EffectInterface effect) {
        //TODO
        return true;
    }

    public List<PowerupCard> canAffordWithPowerup(EffectInterface effect) {
        //TODO
        return null;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    /**
     *
     * @param score
     * @deprecated use {@link Player#addScore(int)} instead
     */
    @Deprecated
    public void setScore(int score) {
        this.score = score;
    }



    public void addScore(int score) {
        this.score += score;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public String getFigureRes() {
        return figureRes;
    }

    public ToClientInterface getToClient() {
        return toClient;
    }

    public void setPlayerBoard(PlayerBoardInterface playerBoard) {
        this.playerBoard = playerBoard;
    }
}
