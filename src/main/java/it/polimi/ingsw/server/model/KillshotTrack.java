package it.polimi.ingsw.server.model;

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

                kills.put(p,kills.get(p)+1);
            }
        }

        List<Player> chart = kills.entrySet().stream().sorted(
                                                    (e1,e2)->
                                                            {
                                                                if(e1.getValue().compareTo(e2.getValue())!=0)
                                                                    return e2.getValue()-e1.getValue();
                                                                // we have a tie. The first killer wins
                                                                else return orderedKills.indexOf(e1) - orderedKills.indexOf(e2);
                                                            })
                                                            .map(Map.Entry::getKey)
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
        skullsLeft = skullsLeft - 1;
    }

}