package it.polimi.ingsw.server.model.player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A PlayerBoard that implements the rules of a normal turn in a normal game.
 * <p>
 * This class handles damage and marks for the {@link Player}.
 * This keeps track of the damage, marks, skulls, and scores the players
 * accordingly.
 *
 * @author giubots
 */
public class NormalPlayerBoard implements PlayerBoardInterface {

    /**
     * If damage is above or equal to this, adrenaline action one is unlocked.
     */
    private static final int ADR_1_THRESHOLD = 3;

    /**
     * If damage is above or equal to this, adrenaline action two is unlocked.
     */
    private static final int ADR_2_THRESHOLD = 6;

    /**
     * The amount of damage that counts as a killshot.
     */
    private static final int KILLSHOT_TOKEN = 11;

    /**
     * The amount of damage that counts as overkill.
     */
    private static final int OVERKILL_TOKEN = 12;

    /**
     * The maximum number of marks by each player.
     */
    private static final int MAX_MARKS_PER_PLAYER = 3;

    /**
     * The number of players who can get a score.
     * It is the size of the {@linkplain #DAMAGE_POINTS} array.
     */
    private static final int MAX_REWARDED = 6;

    /**
     * The number of points given to the player who dealt first blood.
     */
    private static final int FIRST_BLOOD_POINTS = 1;

    /**
     * The number of points given tho the first {@linkplain #MAX_REWARDED}
     * players.
     * Scoring will depend on the amount of {@linkplain #skulls}.
     */
    private static final int[] DAMAGE_POINTS = {8, 6, 4, 2, 1, 1};
    /**
     * The number of skulls, this influences scoring.
     */
    int skulls;
    /**
     * The damage added since last {@linkplain #resetDamage()} call.
     */
    private List<Player> damage;
    /**
     * The marks added since last {@linkplain #resetDamage()} call.
     */
    private List<Player> marks;

    /**
     * Constructor with no skulls, no damage, no marks.
     */
    NormalPlayerBoard() {
        skulls = 0;
        damage = new ArrayList<>();
        marks = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     *
     * @return true if damage is greater than {@linkplain #ADR_1_THRESHOLD}
     */
    @Override
    public boolean isAdr1Unlocked() {
        return damage.size() >= ADR_1_THRESHOLD;
    }

    /**
     * {@inheritDoc}
     *
     * @return true if damage is greater than {@linkplain #ADR_2_THRESHOLD}
     */
    @Override
    public boolean isAdr2Unlocked() {
        return damage.size() >= ADR_2_THRESHOLD;
    }

    /**
     * {@inheritDoc}
     *
     * @return true if damage is greater than {@linkplain #KILLSHOT_TOKEN}
     */
    @Override
    public boolean isDead() {
        return damage.size() >= KILLSHOT_TOKEN;
    }

    /**
     * {@inheritDoc}
     *
     * @param shooters a {@linkplain List} containing those who are dealing the
     *                 damage
     */
    @Override
    public void addDamage(List<Player> shooters) {
        if (shooters.contains(null))
            throw new IllegalArgumentException("Shooters contains null");
        /*Adding marks to a damageList in the right place*/
        List<Player> damageList = new ArrayList<>();
        for (Player p : shooters) {
            damageList.add(p);
            if (marks.contains(p))
                damageList.addAll(
                        Collections.nCopies(
                                Collections.frequency(marks, p), p));
            marks.removeIf(el -> el.equals(p));
        }

        /*Adding the damage if possible*/
        for (Player p : damageList)
            if (damage.size() < OVERKILL_TOKEN)
                damage.add(p);
            else
                break;
    }

    /**
     * {@inheritDoc}
     *
     * @param shooters a {@linkplain List} containing those who are dealing
     *                 the marks
     */
    @Override
    public void addMarks(List<Player> shooters) {
        for (Player p : shooters)
            if (Collections.frequency(marks, p) < MAX_MARKS_PER_PLAYER)
                marks.add(p);
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
        /*First blood point*/
        if (!damage.isEmpty())
            damage.get(0).addScore(FIRST_BLOOD_POINTS);

        Deque<Player> toBeScored = getSortedPlayers();

        /*Giving points*/
        for (int i = skulls; i < MAX_REWARDED; i++) {
            if (!toBeScored.isEmpty())
                toBeScored.pop().addScore(DAMAGE_POINTS[i]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSkull() {
        skulls++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetDamage() {
        damage.clear();
    }

    /**
     * {@inheritDoc}
     *
     * @return the player who dealt the damage that counts as
     * {@linkplain #KILLSHOT_TOKEN}
     */
    @Override
    public Player getKillshot() {
        if (damage.size() < KILLSHOT_TOKEN)
            return null;
        else
            return damage.get(KILLSHOT_TOKEN - 1);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public List<Player> getMarks() {
        return new ArrayList<>(marks);
    }

    /**
     * {@inheritDoc}
     *
     * @return the player who dealt the damage that counts as
     * {@linkplain #OVERKILL_TOKEN}
     */
    @Override
    public Player getOverkill() {
        if (damage.size() < OVERKILL_TOKEN)
            return null;
        else
            return damage.get(OVERKILL_TOKEN - 1);
    }

    /**
     * Returns a {@linkplain Deque} with the players in the right order for
     * scoring.
     *
     * @return the players in the right order for scoring
     */
    Deque<Player> getSortedPlayers() {
        /*Counting the tokens and creating a Map*/
        Map<Player, Integer> quantityMap = new HashMap<>();
        for (Player p : damage) {
            if (!quantityMap.containsKey(p))
                quantityMap.put(p, 1);
            else
                quantityMap.replace(p, quantityMap.get(p) + 1);
        }

        /*Sorting the players into a list*/
        return quantityMap.entrySet().stream()
                .sorted((a, b) -> {
                    if (a.getValue().equals(b.getValue()))
                        return damage.indexOf(a.getKey()) - damage.indexOf(b.getKey());
                    return b.getValue() - a.getValue();
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public List<Player> getDamage() {
        return damage;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getSkulls() {
        return skulls;
    }
}