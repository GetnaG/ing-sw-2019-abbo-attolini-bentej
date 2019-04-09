package it.polimi.ingsw.server.model.board;


import it.polimi.ingsw.server.model.board.KillshotTrack;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KillshotTrackTest {



    @Test
    void score() {

        KillshotTrack kt = new KillshotTrack();
        List<Player> hit;


        Player alice = new Player("A");
        Player bob = new Player("B");
        Player charlie = new Player("Charlie");

        // Alice got 2 kills
        kt.removeSkull();
        kt.removeSkull();
        kt.addTokens(Arrays.asList(alice,alice));

        // Bob got 3 kills
        kt.removeSkull();
        kt.removeSkull();
        kt.addTokens(Arrays.asList(bob,bob,bob));

        // Charlie got 3 kills
        kt.removeSkull();
        kt.removeSkull();
        kt.addTokens(Arrays.asList(charlie,charlie,charlie));

        kt.score();

        assertEquals(4, alice.getScore());
        assertEquals(8, bob.getScore());
        assertEquals(6, charlie.getScore());
    }



}