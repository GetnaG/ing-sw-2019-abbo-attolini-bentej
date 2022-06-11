package it.polimi.ingsw.server.model.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/*
 * Author: giubots
 * Assuming that class constants are set correctly
 * Testing: the differences from a NormalPlayerBoard
 */
class FrenzyPlayerBoardTest {
    /*See NormalPlayerBoard for details on this constants*/
    private static final int MAX_REWARDED = 4;
    private static final int[] DAMAGE_POINTS = {2, 1, 1, 1};


    private FrenzyPlayerBoard board;
    private Player APlayer;
    private Player BPlayer;
    private Player CPlayer;
    private Player DPlayer;

    @BeforeEach
    void setUp() {
        board = new FrenzyPlayerBoard();
        APlayer = new Player("A");
        BPlayer = new Player("B");
        CPlayer = new Player("C");
        DPlayer = new Player("D");
    }

    /*Testing that returns false*/
    @Test
    void isAdr1Unlocked() {
        assertFalse(board.isAdr1Unlocked());
    }

    /*Testing that returns false*/
    @Test
    void isAdr2Unlocked() {
        assertFalse(board.isAdr2Unlocked());
    }

    /*Testing scoring without skulls*/
    @Test
    void score_noSkulls() {
        /*First blood, not rewarded*/
        giveDamage(board, 1, APlayer);

        /*Two with most points*/
        giveDamage(board, 4, BPlayer);
        giveDamage(board, 4, CPlayer);

        /*Other*/
        giveDamage(board, 4, DPlayer);

        board.score();

        assertEquals(DAMAGE_POINTS[3], APlayer.getScore());
        assertEquals(DAMAGE_POINTS[0], BPlayer.getScore());
        assertEquals(DAMAGE_POINTS[1], CPlayer.getScore());
        assertEquals(DAMAGE_POINTS[2], DPlayer.getScore());
    }

    /*Testing scoring with skulls*/
    @Test
    void score_normal() {
        board.addSkull();

        /*First blood, not rewarded*/
        giveDamage(board, 1, APlayer);

        /*Two with most points*/
        giveDamage(board, 4, BPlayer);
        giveDamage(board, 4, CPlayer);

        /*Other*/
        giveDamage(board, 4, DPlayer);

        board.score();

        assertEquals(0, APlayer.getScore());
        assertEquals(DAMAGE_POINTS[1], BPlayer.getScore());
        assertEquals(DAMAGE_POINTS[2], CPlayer.getScore());
        assertEquals(DAMAGE_POINTS[3], DPlayer.getScore());
    }

    /*Testing scoring with all skulls*/
    @Test
    void score_noPointsLeft() {
        for (int i = 0; i < MAX_REWARDED; i++)
            board.addSkull();

        /*First blood, not rewarded*/
        giveDamage(board, 1, APlayer);

        /*Two with most points*/
        giveDamage(board, 4, BPlayer);
        giveDamage(board, 4, CPlayer);

        /*Other*/
        giveDamage(board, 4, DPlayer);

        board.score();

        assertEquals(0, APlayer.getScore());
        assertEquals(0, BPlayer.getScore());
        assertEquals(0, CPlayer.getScore());
        assertEquals(0, DPlayer.getScore());
    }

    /*Adds the damage to the board*/
    private void giveDamage(NormalPlayerBoard board, int amount,
                            Player dealer) {
        List<Player> damage = new ArrayList<>();
        damage.add(dealer);
        for (int i = 0; i < amount; i++)
            board.addDamage(damage);
    }
}