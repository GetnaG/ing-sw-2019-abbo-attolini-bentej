package it.polimi.ingsw.server.model.player;

import java.util.List;

/**
 * 
 */
public class FrenzyPlayerBoard implements PlayerBoardInterface {
    @Override
    public boolean isAdr1Unlocked() {
        return false;
    }

    @Override
    public boolean isAdr2Unlocked() {
        return false;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public void addDamage(List<Player> shooters) {

    }

    @Override
    public void addMarks(List<Player> shooters) {

    }

    @Override
    public void score() {

    }

    @Override
    public void addSkull() {

    }

    @Override
    public void resetDamage() {

    }

    @Override
    public Player getKillshot() {
        return null;
    }

    @Override
    public Player getOverkill() {
        return null;
    }
}