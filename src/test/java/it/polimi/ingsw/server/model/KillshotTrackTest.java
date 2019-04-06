package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KillshotTrackTest {



    @Test
    void score() {

        KillshotTrack kt = new KillshotTrack();
        List<Player> hit = new ArrayList<>();


        Player alice = new Player("Alice");
        int times = 2;

        for(int i = 0; i<times;i++){
            hit.add(alice);
            kt.removeSkull();
            kt.addTokens(hit);
            hit = new ArrayList<>();
        }

        Player bob = new Player("Bob");
        times = 3;
        for(int i = 0; i<times;i++){
            hit.add(bob);
            kt.removeSkull();
            kt.addTokens(hit);
            hit = new ArrayList<>();
        }


        Player charlie = new Player("Charlie");

        times = 3;
        for(int i = 0; i<times;i++){
            hit.add(charlie);
            kt.removeSkull();
            kt.addTokens(hit);
            hit = new ArrayList<>();
        }


        kt.score();

        assertEquals(8, bob.getScore());
        assertEquals(6,charlie.getScore());
        assertEquals(4,alice.getScore());






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