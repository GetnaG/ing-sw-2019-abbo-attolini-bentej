package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This class represents the Killshot Track used in a normal game.
 * It contains the skulls and the player's tokens.
 *
 * @author Fahed Ben Tej
 */
public class KillshotTrack extends AbstractTrack {

    /**
     *  skullsLeft represents the skulls left.
     */
    private int skullsLeft;
    /**
     * tokenTrack represents the 8 spaces where tokens are placed.
     */
    private List<List<Player>> tokenTrack;

    /**
     * Creates a KillShots track with 8 skulls.
     */
    public KillshotTrack() {
        skullsLeft = 8;
        tokenTrack = new ArrayList<>();

    }

    /**
     * Creates a KillShots track with the given number of skulls.
     */
    public KillshotTrack(int skullsLeft) {
        this.skullsLeft = skullsLeft;
        tokenTrack = new ArrayList<>();

    }

    /**
     * @return an int representing the skulls left
     */
    @Override
    public int getSkullsLeft() {
        return skullsLeft;
    }

    /**
     * This method is used to score the Killshot Track.
     * Must be called from outside when {@code skullsLeft} is 0.
     * @return void
     */
    @Override
    public void score() {


        Map<Player,Integer> kills = new HashMap<>();
        List<Player> orderedKills = new ArrayList<>();

        // getting the kills for every player and storing the order ( which will be useful in case of a tie)
        for(List<Player> l : tokenTrack){
            for(Player p : l) {
                if(!kills.containsKey(p))
                    kills.put(p,0);
                if(!orderedKills.contains(p))
                    orderedKills.add(p);

                int currentKills = kills.get(p);
                kills.put(p, currentKills+1);
            }
        }

        //Comparing two players according in decreasing order according to number of kills. In case of a tie, the player who did a kill first wins.

        Comparator<Player> chartComparator = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                Integer p1Kills = kills.get(p1);
                Integer p2Kills = kills.get(p2);

                if(p1Kills.compareTo(p2Kills) != 0)
                    return p2Kills.compareTo(p1Kills);
                else {
                    // the player who did a kills first wins
                    return orderedKills.indexOf(p1) - orderedKills.indexOf(p2);
                }
            }
        };

        List<Player> chart = orderedKills.stream()
                                                    .sorted(chartComparator::compare)
                                                    .collect(Collectors.toList());

        // chart is ordered so we just have set score according to the rules
        scoreChart(chart);
    }

    static void scoreChart(List<Player> chart) {
        int points = -1;
        for( Player p : chart){
            switch(points){
                case -1 :
                    points = 8;
                    p.setScore(p.getScore()+points);
                    break;
                case 8 :
                    points = 6;
                    p.setScore(p.getScore()+points);
                    break;
                case 6 :
                    points = 4;
                    p.setScore(p.getScore()+points);
                    break;
                case 4 :
                    points = 2;
                    p.setScore(p.getScore()+points);
                    break;
                case 2 :
                    p.setScore(p.getScore()+points);
                    points = 1;
                    break;
                default:     
            }

        }
    }

    /**
     * Adding Token to the Track. Must be called after removeSkull() .
     *
     * @param tokens Player who did the kill. If overkilled, the list must contain the same player two times.
     *
     * @return
     */
    @Override
    public void addTokens(List<Player> tokens) {

        tokenTrack.add(new ArrayList(tokens));
    }

    /**
     * Removing the leftmost skull.
     */
    @Override
    public void removeSkull() {
        if (skullsLeft > 0)
            skullsLeft = skullsLeft - 1;
    }

    /**
     * Gets game mode.
     */
    @Override
    public String getGameMode(){
        return "Deathmatch";
    }

}