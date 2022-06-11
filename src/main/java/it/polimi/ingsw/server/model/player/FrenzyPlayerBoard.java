package it.polimi.ingsw.server.model.player;

import java.util.Deque;

/**
 * A PlayerBoard that implements the rules of a frenzy turn with a frenzy board.
 * <p>
 * This class handles damage and marks for the {@link Player}.
 * This keeps track of the damage, marks, skulls, and scores the players
 * accordingly.
 *
 * @author giubots
 */
public class FrenzyPlayerBoard extends NormalPlayerBoard {
    /**
     * The number of players who can get a score.
     * It is the size of the {@linkplain #DAMAGE_POINTS} array.
     */
    private static final int MAX_REWARDED = 4;

    /**
     * The number of points given tho the first {@linkplain #MAX_REWARDED}
     * players.
     * Scoring will depend on the amount of {@linkplain #skulls}.
     */
    private static final int[] DAMAGE_POINTS = {2, 1, 1, 1};

    /**
     * {@inheritDoc}
     * <p>
     * In a frenzy player board there are no adrenaline actions.
     *
     * @return always false
     */
    @Override
    public boolean isAdr1Unlocked() {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In a frenzy player board there are no adrenaline actions.
     *
     * @return always false
     */
    @Override
    public boolean isAdr2Unlocked() {
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation scores the players using the array
     * {@linkplain #DAMAGE_POINTS}, giving the specified amount of points
     * (from the number of skulls {@linkplain #skulls} to the
     * {@linkplain #MAX_REWARDED}) to the players, starting from the one with
     * most marks, breaking ties in favour of the first who dealt the damage.
     */
    @Override
    public void score() {
        Deque<Player> toBeScored = getSortedPlayers();

        /*Giving points*/
        for (int i = skulls; i < MAX_REWARDED; i++) {
            if (!toBeScored.isEmpty())
                toBeScored.pop().addScore(DAMAGE_POINTS[i]);
        }
    }
}