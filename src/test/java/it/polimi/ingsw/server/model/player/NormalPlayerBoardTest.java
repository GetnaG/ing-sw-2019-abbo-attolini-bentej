package it.polimi.ingsw.server.model.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: giubots
 * Assuming that class constants are set correctly
 * Testing: normal behaviour
 */
class NormalPlayerBoardTest {
    /*See NormalPlayerBoard for details on this constants*/
    private static final int ADA_1_THRESHOLD = 3;
    private static final int ADA_2_THRESHOLD = 6;
    private static final int KILLSHOT_TOKEN = 11;
    private static final int OVERKILL_TOKEN = 12;
    private static final int MAX_MARKS_PER_PLAYER = 3;
    private static final int MAX_REWARDED = 6;
    private static final int FIRST_BLOOD_POINTS = 1;
    private static final int[] DAMAGE_POINTS = {8, 6, 4, 2, 1, 1};

    private List<Player> twoMarks;
    private NormalPlayerBoard testBoard;
    private Player APlayer;
    private Player BPlayer;
    private Player CPlayer;
    private Player DPlayer;

    @BeforeEach
    void setUp() {
        testBoard = new NormalPlayerBoard();
        APlayer = new Player("A");
        BPlayer = new Player("B");
        CPlayer = new Player("C");
        DPlayer = new Player("D");

        twoMarks = new ArrayList<>();
        twoMarks.add(APlayer);
        twoMarks.add(APlayer);
    }

    /*Testing until there is enough damage*/
    @Test
    void isAdr1Unlocked() {
        for (int i = 0; i < ADA_1_THRESHOLD; i++) {
            assertFalse(testBoard.isAdr1Unlocked());
            giveDamage(testBoard, 1, APlayer);
        }
        assertTrue(testBoard.isAdr1Unlocked());
    }

    /*Testing until there is enough damage*/
    @Test
    void isAdr2Unlocked() {
        for (int i = 0; i < ADA_2_THRESHOLD; i++) {
            assertFalse(testBoard.isAdr2Unlocked());
            giveDamage(testBoard, 1, APlayer);
        }
        assertTrue(testBoard.isAdr2Unlocked());
    }

    /*Testing until there is enough damage*/
    @Test
    void isDead() {
        for (int i = 0; i < KILLSHOT_TOKEN; i++) {
            assertFalse(testBoard.isDead());
            giveDamage(testBoard, 1, APlayer);
        }
        assertTrue(testBoard.isDead());
    }

    /*Testing if marks are taken into account when dealing damage*/
    @Test
    void addDamage() {
        testBoard.addMarks(twoMarks);
        giveDamage(testBoard, ADA_1_THRESHOLD - 2, APlayer);

        assertTrue(testBoard.isAdr1Unlocked());
    }

    /*Testing if marks are memorized and if there are not more than possible*/
    @Test
    void addMarks() {
        testBoard.addMarks(twoMarks);

        assertEquals(2, testBoard.getMarks().size());

        /*Adding more than maximum*/
        for (int i = 0; i < MAX_MARKS_PER_PLAYER + 1; i++)
            twoMarks.add(APlayer);
        testBoard.addMarks(twoMarks);
        assertEquals(MAX_MARKS_PER_PLAYER, testBoard.getMarks().size());
    }

    /*Testing scoring without skulls*/
    @Test
    void score_noSkulls() {
        /*First blood*/
        giveDamage(testBoard, 1, APlayer);

        /*Two with most points*/
        giveDamage(testBoard, 4, BPlayer);
        giveDamage(testBoard, 4, CPlayer);

        /*Other*/
        giveDamage(testBoard, 4, DPlayer);

        testBoard.score();

        assertEquals(FIRST_BLOOD_POINTS + DAMAGE_POINTS[3], APlayer.getScore());
        assertEquals(DAMAGE_POINTS[0], BPlayer.getScore());
        assertEquals(DAMAGE_POINTS[1], CPlayer.getScore());
        assertEquals(DAMAGE_POINTS[2], DPlayer.getScore());
    }

    /*Testing scoring with skulls*/
    @Test
    void score_normal() {
        testBoard.addSkull();

        /*First blood*/
        giveDamage(testBoard, 1, APlayer);

        /*Two with most points*/
        giveDamage(testBoard, 4, BPlayer);
        giveDamage(testBoard, 4, CPlayer);

        /*Other*/
        giveDamage(testBoard, 4, DPlayer);

        testBoard.score();

        assertEquals(FIRST_BLOOD_POINTS + DAMAGE_POINTS[4], APlayer.getScore());
        assertEquals(DAMAGE_POINTS[1], BPlayer.getScore());
        assertEquals(DAMAGE_POINTS[2], CPlayer.getScore());
        assertEquals(DAMAGE_POINTS[3], DPlayer.getScore());
    }

    /*Testing scoring with all skulls*/
    @Test
    void score_noPointsLeft() {
        for (int i = 0; i < MAX_REWARDED; i++)
            testBoard.addSkull();

        /*First blood*/
        giveDamage(testBoard, 1, APlayer);

        /*Two with most points*/
        giveDamage(testBoard, 4, BPlayer);
        giveDamage(testBoard, 4, CPlayer);

        /*Other*/
        giveDamage(testBoard, 4, DPlayer);

        testBoard.score();

        assertEquals(FIRST_BLOOD_POINTS, APlayer.getScore());
        assertEquals(0, BPlayer.getScore());
        assertEquals(0, CPlayer.getScore());
        assertEquals(0, DPlayer.getScore());
    }

    /*Testing if marks are kept and damage is removed*/
    @Test
    void resetDamage() {
        giveDamage(testBoard, ADA_1_THRESHOLD, APlayer);
        testBoard.addMarks(twoMarks);

        testBoard.resetDamage();

        assertFalse(testBoard.isAdr1Unlocked());
        giveDamage(testBoard, ADA_1_THRESHOLD - 2, APlayer);

        assertTrue(testBoard.isAdr1Unlocked());
    }

    /*Testing if the right player is returned*/
    @Test
    void getKillshot() {
        giveDamage(testBoard, KILLSHOT_TOKEN - 1, APlayer);
        giveDamage(testBoard, 1, BPlayer);

        assertEquals(BPlayer, testBoard.getKillshot());
    }

    /*Testing if the right players are returned*/
    @Test
    void getMarks() {
        testBoard.addMarks(twoMarks);

        assertEquals(2, testBoard.getMarks().size());
        assertEquals(APlayer, testBoard.getMarks().get(0));
        assertEquals(APlayer, testBoard.getMarks().get(1));
    }

    /*Testing if the right player is returned*/
    @Test
    void getOverkill() {
        giveDamage(testBoard, OVERKILL_TOKEN - 1, APlayer);
        giveDamage(testBoard, 1, BPlayer);

        assertEquals(BPlayer, testBoard.getOverkill());
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