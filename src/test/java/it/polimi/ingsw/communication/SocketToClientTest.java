package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.controller.effects.EffectIterator;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


/*
 * Author: Abbo Giulio A.
 * Testing: Correct returned value, correct protocol
 */
class SocketToClientTest {
    private static final String DEFAULT_CHOICE = "Default";
    private MockSocket socket;
    private SocketToClient toClient;

    @BeforeEach
    void setUp() {
        socket = new MockSocket();
        try {
            toClient = new SocketToClient(socket);
        } catch (ToClientException ignored) {
        }

        /*Flushing the buffer*/
        socket.contents();
    }

    @AfterEach
    void tearDown() {
        socket.close();
    }

    @Test
    void chooseEffectsSequence() {

        /*The object that should be returned*/
        EffectInterface expectedChoice = new MockEffect("10", null);

        /*Writing the choice to the socket*/
        socket.put("1");

        /*Asking and checking the answer*/
        List<EffectInterface> options = Arrays.asList(
                new MockEffect("00", new MockEffect("01", null)),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseEffectsSequence(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_MULTI.getCommand(),
                Type.PROTOCOL_LIST.getCommand(),
                "00",
                "01",
                Type.PROTOCOL_END_LIST.getCommand(),
                Type.PROTOCOL_LIST.getCommand(),
                "01",
                Type.PROTOCOL_END_LIST.getCommand(),
                Type.PROTOCOL_END_MULTI.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.EFFECTS_SEQUENCE.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseSpawn() {

        /*The object that should be returned*/
        PowerupCard expectedChoice =
                new PowerupCard(DEFAULT_CHOICE, null, null);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<PowerupCard> options = Arrays.asList(
                new PowerupCard("name", null, null),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseSpawn(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.SPAWN.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void choosePowerup() {

        /*The object that should be returned*/
        PowerupCard expectedChoice =
                new PowerupCard(DEFAULT_CHOICE, null, null);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<PowerupCard> options = Arrays.asList(
                new PowerupCard("name", null, null),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.choosePowerup(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.POWERUP.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseDestination() {

        /*The object that should be returned*/
        Square expectedChoice =
                new MockSquare(DEFAULT_CHOICE);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<Square> options = Arrays.asList(
                new MockSquare("name"),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseDestination(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.DESTINATION.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseWeaponCard() {

        /*The object that should be returned*/
        WeaponCard expectedChoice =
                new WeaponCard(DEFAULT_CHOICE, new ArrayList<>(), null, false);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<WeaponCard> options = Arrays.asList(
                new WeaponCard("name", new ArrayList<>(), null, false),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseWeaponCard(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.WEAPON.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseWeaponToBuy() {

        /*The object that should be returned*/
        WeaponCard expectedChoice =
                new WeaponCard(DEFAULT_CHOICE, new ArrayList<>(), null, false);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<WeaponCard> options = Arrays.asList(
                new WeaponCard("name", new ArrayList<>(), null, false),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseWeaponToBuy(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.WEAPON_TO_BUY.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseWeaponToDiscard() {

        /*The object that should be returned*/
        WeaponCard expectedChoice =
                new WeaponCard(DEFAULT_CHOICE, new ArrayList<>(), null, false);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<WeaponCard> options = Arrays.asList(
                new WeaponCard("name", new ArrayList<>(), null, false),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseWeaponToDiscard(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.WEAPON_TO_DISCARD.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseWeaponToReload() {

        /*The object that should be returned*/
        WeaponCard expectedChoice =
                new WeaponCard(DEFAULT_CHOICE, new ArrayList<>(), null, false);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<WeaponCard> options = Arrays.asList(
                new WeaponCard("name", new ArrayList<>(), null, false),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseWeaponToReload(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.WEAPON_TO_RELOAD.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseAction() {

        /*The object that should be returned*/
        Action expectedChoice =
                new Action(DEFAULT_CHOICE, new ArrayList<>());

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<Action> options = Arrays.asList(
                new Action("name", new ArrayList<>()),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseAction(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.ACTION.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void choosePowerupForPaying() {

        /*The object that should be returned*/
        PowerupCard expectedChoice =
                new PowerupCard(DEFAULT_CHOICE, null, null);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<PowerupCard> options = Arrays.asList(
                new PowerupCard("name", null, null),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.choosePowerupForPaying(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.POWERUP_FOR_PAYING.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void askUseTagback() {

        /*The object that should be returned*/
        PowerupCard expectedChoice =
                new PowerupCard(DEFAULT_CHOICE, null, null);

        /*Writing the choice to the socket*/
        socket.put(DEFAULT_CHOICE);

        /*Asking and checking the answer*/
        List<PowerupCard> options = Arrays.asList(
                new PowerupCard("name", null, null),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.askUseTagback(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_LIST.getCommand(),
                "name",
                DEFAULT_CHOICE,
                Type.PROTOCOL_END_LIST.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.USE_TAGBACK.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseTarget() {

        /*The object that should be returned*/
        List<Damageable> expectedChoice =
                Arrays.asList(new MockDamageable("10"));

        /*Writing the choice to the socket*/
        socket.put("1");

        /*Asking and checking the answer*/
        List<List<Damageable>> options = Arrays.asList(
                Arrays.asList(new MockDamageable("00"), new MockDamageable("01")),
                expectedChoice
        );
        try {
            assertEquals(expectedChoice, toClient.chooseTarget(options));
        } catch (ToClientException e) {
            fail(e);
        }

        /*Checking the protocol*/
        List<String> expected = Arrays.asList(
                Type.PROTOCOL_MULTI.getCommand(),
                Type.PROTOCOL_LIST.getCommand(),
                "00",
                "01",
                Type.PROTOCOL_END_LIST.getCommand(),
                Type.PROTOCOL_LIST.getCommand(),
                "01",
                Type.PROTOCOL_END_LIST.getCommand(),
                Type.PROTOCOL_END_MULTI.getCommand()
        );
        List<String> actual = socket.contents();
        assertEquals(Type.TARGET.getCommand(), actual.remove(0));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(0), actual.get(0));
        }
    }

    @Test
    void chooseUserName() {
        socket.put("test name");
        try {
            assertEquals("test name", toClient.chooseUserName());
        } catch (ToClientException e) {
            fail(e);
        }
        assertEquals(Type.NICKNAME.getCommand(), socket.contents().get(0));
    }

    @Test
    void quit() {
        socket.put(DEFAULT_CHOICE);
        assertDoesNotThrow(() -> toClient.quit());
        assertEquals(Type.QUIT.getCommand(), socket.contents().get(0));
    }

    class MockSocket extends Socket {
        private ByteArrayOutputStream toClient;
        private ByteArrayOutputStream toServer;

        MockSocket() {
            toClient = new ByteArrayOutputStream();
            toServer = new ByteArrayOutputStream();
        }

        @Override
        public InputStream getInputStream() {
            byte[] buf = toServer.toByteArray();
            int length = toServer.size();
            toServer.reset();
            return new ByteArrayInputStream(buf, 0, length);
        }

        @Override
        public OutputStream getOutputStream() {
            return toClient;
        }

        List<String> contents() {
            byte[] buf = toClient.toByteArray();
            int length = toClient.size();
            toClient.reset();
            return new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(buf, 0, length)))
                    .lines().collect(Collectors.toList());
        }

        void put(String string) {
            new PrintWriter(toServer, true).println(string);
        }

        @Override
        public synchronized void close() {
            try {
                super.close();
                toClient.close();
                toServer.close();
            } catch (IOException ignored) {
            }
        }
    }

    class MockDamageable implements Damageable {
        private String id;

        MockDamageable(String id) {
            this.id = id;
        }

        @Override
        public void giveDamage(List<Player> shooters) {

        }

        @Override
        public void giveMark(List<Player> shooters) {

        }

        @Override
        public Square getPosition() {
            return null;
        }

        @Override
        public void setPosition(Square newPosition) {

        }

        @Override
        public void scoreAndResetDamage() {

        }

        @Override
        public Player getKillshotPlayer() {
            return null;
        }

        @Override
        public Player getOverkillPlayer() {
            return null;
        }

        @Override
        public String getName() {
            return id;
        }
    }

    class MockSquare extends Square {
        private String id;

        public MockSquare(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }
    }

    class MockEffect implements EffectInterface {
        EffectInterface next;
        String name;

        public MockEffect(String name, MockEffect next) {
            this.name = name;
            this.next = next;
        }

        @Override
        public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> allTargeted, List<Damageable> damageTargeted) {

        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public EffectInterface getDecorated() {
            return next;
        }

        @Override
        public void addToChain(EffectInterface last) {
            if (next == null)
                next = last;
            else
                next.addToChain(last);
        }

        @Override
        public Iterator<EffectInterface> iterator() {
            return new EffectIterator(this);
        }
    }
}