package it.polimi.ingsw.server.model.board;


import it.polimi.ingsw.server.model.board.DominationTrack;
import it.polimi.ingsw.server.model.board.Spawn;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DominationTrackTest {

    @Test
    void score() {

        /**
         * TEST 1
         */
         Spawn s1 = new Spawn();
         Spawn s2 = new Spawn();
         Spawn s3 = new Spawn();
         DominationTrack dt = new DominationTrack(s1,s2,s3);

         Player alice = new Player("Alice");
         Player bob = new Player("Bob");
         Player charlie = new Player("Charlie");


        List<Player> hits = new ArrayList<>();

         hits.add(alice);
         hits.add(bob);
         hits.add(bob);
         hits.add(bob);

         s1.giveDamage(hits);
         s2.giveDamage(hits);
         s3.giveDamage(hits);

         dt.score();

         assertEquals(6,alice.getScore());
         assertEquals(8,bob.getScore());

        /**
         * TEST 2
         */
        s1 = new Spawn();
        s2 = new Spawn();
        s3 = new Spawn();
        dt = new DominationTrack(s1,s2,s3);

        alice = new Player("Alice");
        bob = new Player("Bob");
        charlie = new Player("Charlie");

        hits = new ArrayList<>();
        hits.add(charlie);
        hits.add(bob);
        hits.add(alice);
        hits.add(alice);

        s1.giveDamage(hits);
        s2.giveDamage(hits);
        s3.giveDamage(hits);


        dt.score();

        assertEquals(8, alice.getScore());
        assertEquals(6, bob.getScore());
        assertEquals(6,charlie.getScore());

        /**
         * TEST 3
         */
        s1 = new Spawn();
        s2 = new Spawn();
        s3 = new Spawn();
        dt = new DominationTrack(s1,s2,s3);

        alice = new Player("Alice");
        bob = new Player("Bob");
        charlie = new Player("Charlie");
        Player david = new Player("David");

        hits = new ArrayList<>();
        hits.add(charlie);
        hits.add(bob);
        hits.add(alice);
        hits.add(alice);

        s1.giveDamage(hits);
        s2.giveDamage(hits);
        s3.giveDamage(hits);

        hits = new ArrayList<>();
        hits.add(david);
        s1.giveDamage(hits);
        dt.score();

        assertEquals(8, alice.getScore());
        assertEquals(6, bob.getScore());
        assertEquals(6,charlie.getScore());
        assertEquals(2,david.getScore());



    }
}