package it.polimi.ingsw.server.model;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KillshotTrackTest {



    @Test
    void score() {

        KillshotTrack kt = new KillshotTrack();
        List<Player> hit = new ArrayList<>();


        Player alice = new Player("A");
        int times = 2;
        hit = new ArrayList<>();
        hit.add(alice);
        for(int i = 0; i<times;i++){
            kt.removeSkull();
            kt.addTokens(hit);
        }

        Player bob = new Player("B");
        times = 3;
        hit = new ArrayList<>();
        hit.add(bob);
        for(int i = 0; i<times;i++){
            kt.removeSkull();
            kt.addTokens(hit);
        }


        Player charlie = new Player("C");

        times = 3;
        hit = new ArrayList<>();
        hit.add(charlie);
        for(int i = 0; i<times;i++){
            kt.removeSkull();
            kt.addTokens(hit);
        }

        kt.score();

        assertEquals(4, alice.getScore());
        assertEquals(8, bob.getScore());
        assertEquals(6, charlie.getScore());
    }


    @Test
    void removeSkullAndAddTokens() {

        KillshotTrack kt = new KillshotTrack();
        List<Player> hit = new ArrayList<>();
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");

        assertEquals(kt.getSkullsLeft(),8);

        kt.removeSkull();
        hit.add(alice);
        kt.addTokens(hit);
        assertEquals(kt.getSkullsLeft(), 7);

        hit.add(alice);
        kt.removeSkull();
        kt.addTokens(hit);
        assertEquals(kt.getSkullsLeft(), 6);

        hit = new ArrayList<>();
        hit.add(bob );
        hit.add(bob);
        kt.removeSkull();
        assertEquals(kt.getSkullsLeft(), 5);
    }
}