package it.polimi.ingsw.server.model.player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A PlayerBoard that implements the rules of a normal turn in a normal game.
 */
public class NormalPlayerBoard implements PlayerBoardInterface {
    private static final int ADR_1_TRESHOLD = 3;
    private static final int ADR_2_TRESHOLD = 6;
    private static final int KILLSHOT_TOKEN = 11;
    private static final int OVERKILL_TOKEN = 12;
    private static final int MAX_MARKS_PER_PLAYER = 3;
    private static final int MAX_REWARDED = 6;
    private static final int FIRST_BLOOD_POINTS = 1;
    private static final int[] DAMAGE_POINTS = {8, 6, 4, 2, 1, 1};

    private int skulls;
    private List<Player> damage;
    private List<Player> marks;

    public NormalPlayerBoard() {
        skulls = 0;
        damage = new ArrayList<>();
        marks = new ArrayList<>();
    }

    @Override
    public boolean isAdr1Unlocked() {
        return damage.size() >= ADR_1_TRESHOLD;
    }

    @Override
    public boolean isAdr2Unlocked() {
        return damage.size() >= ADR_2_TRESHOLD;
    }

    @Override
    public boolean isDead() {
        return damage.size() >= KILLSHOT_TOKEN;
    }

    @Override
    public void addDamage(List<Player> shooters) {
        List<Player> damageList = new ArrayList<>();
        for (Player p : shooters) {
            damageList.add(p);
            for (Player m : marks) {
                if (m.equals(p)) {
                    damageList.add(m);
                    marks.remove(m);
                }
            }
        }
        checkAndAdd(damageList);
    }

    @Override
    public void addMarks(List<Player> shooters) {
        for (Player p : shooters)
            if (Collections.frequency(marks, p) < MAX_MARKS_PER_PLAYER)
                marks.add(p);
    }

    @Override
    public void score() {
        /*First blood point*/
        if (!damage.isEmpty())
            damage.get(0).addScore(FIRST_BLOOD_POINTS);

        /*Counting the tokens and creating a Map*/
        Map<Player, Integer> quantityMap = new HashMap<>();
        for (Player p : damage) {
            if (!quantityMap.containsKey(p))
                quantityMap.put(p, 0);
            else
                quantityMap.replace(p, quantityMap.get(p) + 1);
        }

        /*Sorting the players into a list*/
        Deque<Player> toBeScored = quantityMap.entrySet().stream()
                .sorted((a, b) -> {
                    if (a.getValue().equals(b.getValue()))
                        return damage.indexOf(b.getKey()) - damage.indexOf(a.getKey());
                    return a.getValue() - b.getValue();
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayDeque::new));

        /*Giving points*/
        for (int i = skulls; i<MAX_REWARDED; i++) {
            toBeScored.pop().addScore(DAMAGE_POINTS[i]);
        }
    }

    @Override
    public void addSkull() {
        skulls++;
    }

    @Override
    public void resetDamage() {
        damage.clear();
    }

    @Override
    public Player getKillshot() {
        if (damage.size() < KILLSHOT_TOKEN)
            return null;
        else
            return damage.get(KILLSHOT_TOKEN);
    }

    @Override
    public Player getOverkill() {
        if (damage.size() < OVERKILL_TOKEN)
            return null;
        else
            return damage.get(OVERKILL_TOKEN);
    }

    private void checkAndAdd(List<Player> damage) {
        for (Player p : damage)
            if (damage.size() < OVERKILL_TOKEN)
                damage.add(p);
            else
                break;
    }
}