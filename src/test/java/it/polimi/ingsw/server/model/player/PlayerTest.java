package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.ToClientInterface;
import it.polimi.ingsw.server.controller.ScoreListener;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Using following classes and relying on their behaviour: AmmoBox,
 * HandManager, NormalPlayerBoard.
 * Testing: normal behaviour
 */
class PlayerTest {
    private AmmoBox AAmmoBox;
    private HandManager AHandManager;
    private PlayerBoardInterface APlayerBoard;
    private Player APlayer;

    private PlayerBoardInterface BPlayerBoard;
    private Player BPlayer;

    private List<Player> playersList; /*Contains two BPlayers*/
    private List<Player> APlayerList; /*Contains one APlayer*/
    private List<Player> BPlayerList; /*Contains one BPlayer*/


    @BeforeEach
    void setUp() {
        AAmmoBox = new AmmoBox();
        AHandManager = new HandManager();
        APlayerBoard = new NormalPlayerBoard();
        ScoreListener AScorer = createScorer();
        APlayer = new Player(AHandManager, AAmmoBox, APlayerBoard, AScorer);

        AmmoBox BAmmoBox = new AmmoBox();
        HandManager BHandManager = new HandManager();
        BPlayerBoard = new NormalPlayerBoard();
        ScoreListener BScorer = createScorer();
        BPlayer = new Player(BHandManager, BAmmoBox, BPlayerBoard, BScorer);

        playersList = new ArrayList<>();
        playersList.add(BPlayer);
        playersList.add(BPlayer);

        APlayerList = new ArrayList<>();
        APlayerList.add(APlayer);

        BPlayerList = new ArrayList<>();
        BPlayerList.add(BPlayer);
    }

    /*Testing that damage is added*/
    @Test
    void giveDamage() {
        /*Counting how much counts as killshot*/
        int countDeathAmount = 0;
        while (!APlayerBoard.isDead()) {
            APlayerBoard.addDamage(APlayerList);
            countDeathAmount++;
        }
        APlayerBoard.resetDamage();

        /*Giving damage*/
        for (int i = 0; i < countDeathAmount; i++)
            APlayer.giveDamage(APlayerList);

        /*Checking that was added*/
        assertTrue(APlayerBoard.isDead());
    }

    /*Checks that marks are added to the board*/
    @Test
    void giveMark() {
        APlayer.giveMark(playersList);

        assertEquals(playersList, APlayerBoard.getMarks());
    }

    /*Testing that the position is memorized*/
    @Test
    void getPosition() {
        Square position = new Square();
        APlayer.setPosition(position);

        assertEquals(position, APlayer.getPosition());
    }

    /*Testing that the position is memorized*/
    @Test
    void setPosition() {
        Square position = new Square();
        APlayer.setPosition(position);

        assertEquals(position, APlayer.getPosition());
    }

    /*Testing that the damaging players receive points*/
    @Test
    void scoreAndResetDamage() {
        APlayerBoard.addDamage(playersList);
        APlayer.scoreAndResetDamage();

        assertTrue(BPlayer.getScore() != 0);
    }

    /*Testing if the right player is returned*/
    @Test
    void getKillshotPlayer() {
        assertNull(APlayer.getKillshotPlayer());

        /*Counting how much counts as killshot*/
        int countDeathAmount = 0;
        while (!APlayerBoard.isDead()) {
            APlayerBoard.addDamage(APlayerList);
            countDeathAmount++;
        }
        APlayerBoard.resetDamage();

        /*Giving damage*/
        for (int i = 0; i < countDeathAmount - 1; i++)
            APlayerBoard.addDamage(APlayerList);
        APlayerBoard.addDamage(BPlayerList);

        assertEquals(BPlayer, APlayer.getKillshotPlayer());
    }

    /*Testing if the right player is returned*/
    @Test
    void getOverkillPlayer() {
        assertNull(APlayer.getKillshotPlayer());

        /*Counting how much counts as killshot*/
        int countDeathAmount = 0;
        while (!APlayerBoard.isDead()) {
            APlayerBoard.addDamage(APlayerList);
            countDeathAmount++;
        }
        APlayerBoard.resetDamage();

        /*Giving damage*/
        for (int i = 0; i < countDeathAmount; i++)
            APlayerBoard.addDamage(APlayerList);
        APlayerBoard.addDamage(BPlayerList);

        assertEquals(BPlayer, APlayer.getOverkillPlayer());
    }

    /*Testing with no damage, damage greater than required for adrenaline
    action one and same for adrenaline action two*/
    @Test
    void getAdrenalineActions() {
        assertTrue(APlayer.getAdrenalineActions().isEmpty());

        while (!APlayerBoard.isAdr1Unlocked())
            APlayerBoard.addDamage(playersList);

        assertEquals(1, APlayer.getAdrenalineActions().size());

        while (!APlayerBoard.isAdr2Unlocked())
            APlayerBoard.addDamage(playersList);

        assertEquals(2, APlayer.getAdrenalineActions().size());
    }

    /*Testing if ammo cubes are added*/
    @Test
    void addAmmo() {
        List<AmmoCube> cubeList = Arrays.asList(AmmoCube.BLUE, AmmoCube.YELLOW);

        APlayer.addAmmo(cubeList);

        assertTrue(APlayer.canAfford(cubeList, false));
    }

    /*Testing adding a powerup card*/
    @Test
    void addPowerup() {
        PowerupCard card1 = new PowerupCard("card1", "Effect", AmmoCube.BLUE);
        PowerupCard card2 = new PowerupCard("card2", "Effect", AmmoCube.RED);

        AHandManager.addPowerup(card1);

        APlayer.addPowerup(card2);

        assertEquals(2, APlayer.getAllPowerup().size());
        assertTrue(APlayer.getAllPowerup().contains(card1));
        assertTrue(APlayer.getAllPowerup().contains(card2));
    }

    /*Testing removing a powerup*/
    @Test
    void removePowerup() {
        PowerupCard card1 = new PowerupCard("card1", "Effect", AmmoCube.BLUE);
        PowerupCard card2 = new PowerupCard("card2", "Effect", AmmoCube.RED);

        AHandManager.addPowerup(card1);
        AHandManager.addPowerup(card2);

        APlayer.removePowerup(card1);

        assertEquals(1, APlayer.getAllPowerup().size());
        assertFalse(APlayer.getAllPowerup().contains(card1));
        assertTrue(APlayer.getAllPowerup().contains(card2));
    }

    /*Testing if the powerups are added to the player*/
    @Test
    void getAllPowerup() {
        PowerupCard card1 = new PowerupCard("card1", "Effect", AmmoCube.BLUE);
        PowerupCard card2 = new PowerupCard("card2", "Effect", AmmoCube.RED);

        AHandManager.addPowerup(card1);
        AHandManager.addPowerup(card2);

        assertEquals(2, APlayer.getAllPowerup().size());
        assertTrue(APlayer.getAllPowerup().contains(card1));
        assertTrue(APlayer.getAllPowerup().contains(card2));
    }

    /*Testing if a card is added to the powerups*/
    @Test
    void getNumOfPowerups() {
        AHandManager.addPowerup(new PowerupCard("test card", "Effect", AmmoCube.RED));

        assertEquals(1, APlayer.getNumOfPowerups());
    }

    /*Testing normal behaviour*/
    @Test
    void canAfford() {
        AAmmoBox.addAmmo(AmmoCube.BLUE);
        AAmmoBox.addAmmo(AmmoCube.YELLOW);

        assertTrue(APlayer.canAfford(Arrays.asList(AmmoCube.YELLOW, AmmoCube.BLUE),
                false));

        assertFalse(APlayer.canAfford(Arrays.asList(AmmoCube.RED, AmmoCube.BLUE),
                false));
    }

    /*Testing normal behaviour*/
    @Test
    void canAffordWithPowerups_normal() {
        PowerupCard card = new PowerupCard("card test", "Effect", AmmoCube.BLUE);
        AHandManager.addPowerup(card);
        AAmmoBox.addAmmo(AmmoCube.YELLOW);

        assertTrue(APlayer.canAffordWithPowerups(
                Arrays.asList(AmmoCube.YELLOW, AmmoCube.BLUE), false)
                .contains(card));

        assertTrue(APlayer.canAffordWithPowerups(
                Arrays.asList(AmmoCube.RED, AmmoCube.BLUE), false)
                .isEmpty());
    }

    /*Testing with cubes ANY*/
    @Test
    void canAffordWithPowerups_ANY() {
        PowerupCard card = new PowerupCard("card test", "Effect", AmmoCube.BLUE);
        AHandManager.addPowerup(card);
        AAmmoBox.addAmmo(AmmoCube.YELLOW);

        assertTrue(APlayer.canAffordWithPowerups(
                Arrays.asList(AmmoCube.YELLOW, AmmoCube.ANY), false).contains(card));

        assertTrue(APlayer.canAffordWithPowerups(
                Arrays.asList(AmmoCube.RED, AmmoCube.ANY), false)
                .isEmpty());
    }

    /*Testing card with one cube*/
    @Test
    void buy_oneCube() {
        WeaponCard card = new WeaponCard("Weapon",
                Arrays.asList(AmmoCube.BLUE), new String[][]{{"Test"}}, true);
        AAmmoBox.addAmmo(AmmoCube.BLUE);

        APlayer.buy(card, null);

        assertEquals(1, AAmmoBox.getBlue());
        assertTrue(AHandManager.getLoadedWeapons().contains(card));
    }

    /*Testing card with more than one cubes*/
    @Test
    void buy_moreCubes() {
        WeaponCard card = new WeaponCard("Weapon",
                Arrays.asList(AmmoCube.BLUE, AmmoCube.RED), new String[][]{{"Test"}}, true);
        PowerupCard powerup = new PowerupCard("powerup", "Effect", AmmoCube.RED);
        AAmmoBox.addAmmo(AmmoCube.BLUE);
        AHandManager.addPowerup(powerup);

        APlayer.buy(card, Arrays.asList(powerup));

        assertEquals(1, AAmmoBox.getBlue());
        assertFalse(AHandManager.getPowerups().contains(powerup));
        assertTrue(AHandManager.getLoadedWeapons().contains(card));
    }

    /*Testing removing a weapon from the player*/
    @Test
    void discard() {
        WeaponCard card = new WeaponCard("card", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card);

        APlayer.discard(card);

        assertTrue(APlayer.getAllWeapons().isEmpty());
    }

    /*Testing normal behaviour for loaded and unloaded weapons*/
    @Test
    void unload_normal() {
        WeaponCard card1 = new WeaponCard("card 1", Collections.emptyList(), new String[][]{{"Test"}}, true);
        WeaponCard card2 = new WeaponCard("card 2", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card1);
        AHandManager.addWeaponCard(card2);
        AHandManager.unload(card2);

        APlayer.unload(card1);

        assertEquals(2, APlayer.getReloadableWeapons().size());
        assertTrue(APlayer.getReloadableWeapons().contains(card1));
        assertTrue(APlayer.getReloadableWeapons().contains(card2));

        assertTrue(APlayer.getLoadedWeapons().isEmpty());
        assertFalse(APlayer.getLoadedWeapons().contains(card1));
        assertFalse(APlayer.getLoadedWeapons().contains(card2));
    }

    /*Testing unloading an unloaded weapon*/
    @Test
    void unload_alreadyUnloaded() {
        WeaponCard card1 = new WeaponCard("card 1", Collections.emptyList(), new String[][]{{"Test"}}, true);
        WeaponCard card2 = new WeaponCard("card 2", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card1);
        AHandManager.addWeaponCard(card2);
        AHandManager.unload(card2);

        assertThrows(IllegalArgumentException.class,
                () -> APlayer.unload(card2));
    }

    /*Testing normal behaviour for loaded and unloaded weapons*/
    @Test
    void reload_normal() {
        WeaponCard card1 = new WeaponCard("card 1", Collections.emptyList(), new String[][]{{"Test"}}, true);
        WeaponCard card2 = new WeaponCard("card 2", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card1);
        AHandManager.addWeaponCard(card2);
        AHandManager.unload(card2);

        APlayer.reload(card2);

        assertEquals(2, APlayer.getLoadedWeapons().size());
        assertTrue(APlayer.getLoadedWeapons().contains(card1));
        assertTrue(APlayer.getLoadedWeapons().contains(card2));

        assertTrue(APlayer.getReloadableWeapons().isEmpty());
        assertFalse(APlayer.getReloadableWeapons().contains(card1));
        assertFalse(APlayer.getReloadableWeapons().contains(card2));
    }

    /*Testing reloading a reloaded weapon*/
    @Test
    void reload_alreadyReloaded() {
        WeaponCard card = new WeaponCard("card", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card);

        assertThrows(IllegalArgumentException.class,
                () -> APlayer.reload(card));
    }

    /*Testing normal behaviour for loaded and unloaded weapons*/
    @Test
    void getLoadedWeapons() {
        WeaponCard card1 = new WeaponCard("card 1", Collections.emptyList(), new String[][]{{"Test"}}, true);
        WeaponCard card2 = new WeaponCard("card 2", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card1);
        AHandManager.addWeaponCard(card2);
        AHandManager.unload(card2);

        assertEquals(1, APlayer.getLoadedWeapons().size());
        assertTrue(APlayer.getLoadedWeapons().contains(card1));
        assertFalse(APlayer.getLoadedWeapons().contains(card2));
    }

    /*Testing normal behaviour for loaded and unloaded weapons*/
    @Test
    void getReloadableWeapons() {
        WeaponCard card1 = new WeaponCard("card 1", Collections.emptyList(), new String[][]{{"Test"}}, true);
        WeaponCard card2 = new WeaponCard("card 2", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card1);
        AHandManager.addWeaponCard(card2);
        AHandManager.unload(card2);

        assertEquals(1, APlayer.getReloadableWeapons().size());
        assertFalse(APlayer.getReloadableWeapons().contains(card1));
        assertTrue(APlayer.getReloadableWeapons().contains(card2));
    }

    /*Testing normal behaviour for loaded and unloaded weapons*/
    @Test
    void getAllWeapons() {
        WeaponCard card1 = new WeaponCard("card 1", Collections.emptyList(), new String[][]{{"Test"}}, true);
        WeaponCard card2 = new WeaponCard("card 2", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card1);
        AHandManager.addWeaponCard(card2);
        AHandManager.unload(card2);

        assertEquals(2, APlayer.getAllWeapons().size());
        assertTrue(APlayer.getAllWeapons().contains(card1));
        assertTrue(APlayer.getAllWeapons().contains(card2));
    }

    /*Testing if the right number is returned with loaded and unloaded weapons*/
    @Test
    void getNumOfWeapons() {
        WeaponCard card1 = new WeaponCard("card 1", Collections.emptyList(), new String[][]{{"Test"}}, true);
        WeaponCard card2 = new WeaponCard("card 2", Collections.emptyList(), new String[][]{{"Test"}}, true);
        AHandManager.addWeaponCard(card1);
        AHandManager.addWeaponCard(card2);
        AHandManager.unload(card2);

        assertEquals(2, APlayer.getNumOfWeapons());
    }

    /*Testing nickname is correctly memorized*/
    @Test
    void getNickname() {
        Player player = new Player("Test");

        assertEquals("Test", player.getNickname());
    }

    /*Testing getter for the score*/
    @Test
    void getScore() {
        APlayer.addScore(7);

        assertEquals(7, APlayer.getScore());
    }

    /*Testing with positive score*/
    @Test
    void addScore_normal() {
        APlayer.addScore(7);
        APlayer.addScore(7);

        assertEquals(14, APlayer.getScore());
    }

    /*Testing with negative score*/
    @Test
    void addScore_negative() {
        assertThrows(IllegalArgumentException.class,
                () -> APlayer.addScore(-7));
    }

    /*Testing if the returned value is the right one*/
    @Test
    void isFirstPlayer() {
        Player player = new Player("", true, "", null, null, null);

        assertTrue(player.isFirstPlayer());
    }

    /*Testing if the returned string is the right one*/
    @Test
    void getFigureRes() {
        Player player = new Player("", true, "Test", null, null, null);

        assertEquals("Test", player.getFigureRes());
    }

    /*Testing if the returned object is the right one*/
    @Test
    void getToClient() {
        ToClientInterface toClientInterface = new PlayerTest.MyToClientInterface();
        Player player = new Player("", true, "Test", toClientInterface, null, null);

        assertEquals(toClientInterface, player.getToClient());
    }

    /*Testing if the board is changed and marks are kept*/
    @Test
    void setPlayerBoard() {
        APlayer.giveMark(playersList);
        APlayer.setPlayerBoard(BPlayerBoard);

        assertEquals(playersList, BPlayerBoard.getMarks());
    }

    /*Creates a mock ScoreListener*/
    private ScoreListener createScorer() {
        return new ScoreListener() {
            @Override
            public void addKilled(Damageable killed) {
            }

            @Override
            public void scoreAllKilled() {
            }

            @Override
            public void scoreBoard() {
            }

            @Override
            public List<Damageable> getKilled() {
                return new ArrayList<>();
            }

            @Override
            public void emptyKilledList() {
            }
        };
    }

    /*A mock ToClientInterface*/
    private static class MyToClientInterface implements ToClientInterface {
        @Override
        public EffectInterface chooseEffectsSequence(List<EffectInterface> options) {
            return null;
        }

        @Override
        public PowerupCard chooseSpawn(List<PowerupCard> option) {
            return null;
        }

        @Override
        public PowerupCard choosePowerup(List<PowerupCard> options) {
            return null;
        }

        @Override
        public Square chooseDestination(List<Square> options) {
            return null;
        }

        @Override
        public WeaponCard chooseWeaponCard(List<WeaponCard> options) {
            return null;
        }

        @Override
        public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) {
            return null;
        }

        @Override
        public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) {
            return null;
        }

        @Override
        public WeaponCard chooseWeaponToReload(List<WeaponCard> options) {
            return null;
        }

        @Override
        public Action chooseAction(List<Action> options) {
            return null;
        }

        @Override
        public PowerupCard choosePowerupForPaying(List<PowerupCard> options) {
            return null;
        }

        @Override
        public PowerupCard askUseTagback(List<PowerupCard> options) {
            return null;
        }

        @Override
        public List<Damageable> chooseTarget(List<List<Damageable>> options) {
            return null;
        }
    }
}