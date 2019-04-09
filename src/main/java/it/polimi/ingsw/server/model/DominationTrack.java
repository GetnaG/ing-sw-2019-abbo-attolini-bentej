package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.Spawn;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class DominationTrack extends AbstractTrack {
    /**
     * First Spawn
     */
    private Spawn firstSpawn;
    /**
     * Second Spawn
     */
    private Spawn secondSpawn;
    /**
     * Third Spawn
     */
    private Spawn thirdSpawn;
    /**
     * Skulls Left
     */
    private int skullsLeft;

    /**
     * Points to give
     */
    private int points;


    /**
     * Default constructor
     */
    public DominationTrack(Spawn firstSpawn, Spawn secondSpawn, Spawn thirdSpawn) {
        this.firstSpawn = firstSpawn;
        this.secondSpawn = secondSpawn;
        this.thirdSpawn = thirdSpawn;
        skullsLeft = 8;
        points = 8;
    }

    /**
     * @return an integer representing the skulls left
     */
    @Override
    public int getSkullsLeft() {
        return skullsLeft;
    }

    /**
     * This method is used to score the Killshot Track.
     * Must be called from outside when {@code}skullsLeft is 0.
     * @return void
     */
    @Override
    public void score() {

        List<Player> tokens = new ArrayList<>();

        tokens.addAll(firstSpawn.getTokens());
        tokens.addAll(secondSpawn.getTokens());
        tokens.addAll(thirdSpawn.getTokens());

        Map<Player,Integer> spawnHits =  new HashMap<>();


        for (Player p : firstSpawn.getTokens())
            spawnHits.merge(p,1,Integer::sum);



        for (Player p : secondSpawn.getTokens())
            spawnHits.merge(p,1,Integer::sum);
        for (Player p : thirdSpawn.getTokens())
            spawnHits.merge(p,1,Integer::sum);

        List<Player> chart = spawnHits.entrySet().stream()
                .sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue()))
                .map( Map.Entry::getKey)
                .collect(Collectors.toList());

        // chart is ordered so we just have set score according to the rules

        List<Player> playersSamePosition = new ArrayList<>();
        playersSamePosition.add(chart.get(0));
        for (int i = 0; i < chart.size() -1 ; i++) {
            int nextPlayerPoints = spawnHits.get(chart.get(i+1));
            int curPlayerPoints = spawnHits.get(chart.get(i));

            if (curPlayerPoints != nextPlayerPoints) {

                addPoints(playersSamePosition);
                playersSamePosition = new ArrayList<>();
                playersSamePosition.add(chart.get(i+1));

                // if the the next player is the last and the currents player has different points, the next player is alone for sure
                if( i == chart.size() -2) {
                    ArrayList<Player> alonePlayer = new ArrayList<>();
                    alonePlayer.add(chart.get(i+1));
                    addPoints(alonePlayer);
                }
            }
            else  {
                playersSamePosition.add(chart.get(i+1));
                // if the next player is the last and the curPlayerPoints = nextPlayerPoints, we have to score them together
                if ( i == chart.size()-2)
                    addPoints(playersSamePosition);
            }

        }
    }

    /**
     * This methods is used to add points to the players. Players in the same position will get an equal amount of points,
     * but the next position will be empty.
     * @param playersSamePosition a list of player in the same position. The next position will be empty.
     */
    private  void addPoints(List<Player> playersSamePosition) {
        for (Player p : playersSamePosition)
            p.setScore(p.getScore()+ points);
        // 8 -> 6 -> 4 -> 2 -> 1 There is an unconventional use of the break statement.
        switch (points) {
            case 8 :
                points = 6;
                if (playersSamePosition.size() == 1)
                    break;
                else playersSamePosition.remove(playersSamePosition.size()-1);
            case 6 :
                points = 4;
                if (playersSamePosition.size() == 1)
                    break;
                else playersSamePosition.remove(playersSamePosition.size()-1);
            case 4 :
                points = 2;
                if (playersSamePosition.size() == 1)
                    break;
                else playersSamePosition.remove(playersSamePosition.size()-1);
            case 2 :
                if (playersSamePosition.size() == 1)
                    break;
                else playersSamePosition.remove(playersSamePosition.size()-1);
                break;
        }
    }



    /**
     * Does nothing. Not used in Domination Mode.
     *
     * @param tokens Player who did the kill. If overkilled, the list must contain the same player two times.
     *
     * @return
     */
    @Override
    public void addTokens(List<Player> tokens) {
        //Does nothing. Not used in Domination Mode.
    }

    /**
     * Removing the leftmost skull.
     */
    @Override
    public void removeSkull() {
        skullsLeft = skullsLeft-1;

    }


}