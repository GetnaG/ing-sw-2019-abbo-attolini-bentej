package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.communication.protocol.MessageType;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.ProtocolMessage;
import it.polimi.ingsw.communication.socket.SocketToClient;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/*
 * Author: giubots
 * Testing: Correct returned value, correct protocol
 */
@Disabled
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

    /* Tests the TESTED function with the provided OPTIONS, of which the
     * #WITHDEAFULT is the one that will be chosen. NAMEGETTER provides the
     * string associated with an element of the options, TYPE is the expected
     * message type.*/
    private <T> void optionsHelper(List<T> options, int withDefault,
                                   FunctionException<? super List<T>, ? extends T> tested,
                                   Function<? super T, String> nameGetter,
                                   MessageType type) {
        /*Preparing the default choice*/
        socket.put(Integer.toString(withDefault));

        try {
            /*Checking the returned object*/
            T answer = tested.apply(options);
            assertEquals(DEFAULT_CHOICE, nameGetter.apply(answer));

            /*Checking the message type*/
            ProtocolMessage sent = socket.contents();
            assertEquals(type, sent.getCommand());

            /*Checking the options provided*/
            String[][] sentOptions = sent.getOptions();
            for (int i = 0; i < options.size(); i++) {
                assertEquals(nameGetter.apply(options.get(i)),
                        sentOptions[i][0]);
            }
        } catch (ToClientException | ChoiceRefusedException e) {
            fail(e);
        }
    }

    @Disabled
    @Test
    void chooseEffectsSequence() {
        optionsHelper(Arrays.asList(
                new Action("", Arrays.asList(new MockEffect("test1"), new MockEffect("test2"))),
                new Action(DEFAULT_CHOICE, new ArrayList<>())),
                1,
                toClient::chooseEffectsSequence,
                Action::getName,
                MessageType.EFFECTS_SEQUENCE);
    }

    @Disabled
    @Test
    void chooseSpawn() {
        optionsHelper(Arrays.asList(
                new PowerupCard("test1", null, null),
                new PowerupCard("test2", null, null),
                new PowerupCard(DEFAULT_CHOICE, null, null)),
                2,
                toClient::chooseSpawn,
                PowerupCard::getId,
                MessageType.SPAWN
        );
    }

    @Disabled
    @Test
    void choosePowerup() {
        optionsHelper(Arrays.asList(
                new PowerupCard("test1", null, null),
                new PowerupCard("test2", null, null),
                new PowerupCard(DEFAULT_CHOICE, null, null)),
                2,
                toClient::choosePowerup,
                PowerupCard::getId,
                MessageType.POWERUP
        );
    }

    @Disabled
    @Test
    void chooseDestination() {
        optionsHelper(Arrays.asList(
                new MockSquare("test1"),
                new MockSquare(DEFAULT_CHOICE),
                new MockSquare("test2")),
                1,
                toClient::chooseDestination,
                Square::getID,
                MessageType.DESTINATION
        );
    }

    @Disabled
    @Test
    void chooseWeaponCard() {
        optionsHelper(Arrays.asList(
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test2", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard(DEFAULT_CHOICE, Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true)),
                2,
                toClient::chooseWeaponCard,
                WeaponCard::getId,
                MessageType.WEAPON
        );
    }

    @Disabled
    @Test
    void chooseWeaponToBuy() {
        optionsHelper(Arrays.asList(
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test2", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard(DEFAULT_CHOICE, Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true)),
                2,
                toClient::chooseWeaponToBuy,
                WeaponCard::getId,
                MessageType.WEAPON_TO_BUY
        );
    }

    @Disabled
    @Test
    void chooseWeaponToDiscard() {
        optionsHelper(Arrays.asList(
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test2", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard(DEFAULT_CHOICE, Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true)),
                2,
                toClient::chooseWeaponToDiscard,
                WeaponCard::getId,
                MessageType.WEAPON_TO_DISCARD
        );
    }

    @Disabled
    @Test
    void chooseWeaponToReload() {
        optionsHelper(Arrays.asList(
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test2", Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard(DEFAULT_CHOICE, Arrays.asList(AmmoCube.RED), null, true),
                new WeaponCard("test1", Arrays.asList(AmmoCube.RED), null, true)),
                2,
                toClient::chooseWeaponToReload,
                WeaponCard::getId,
                MessageType.WEAPON_TO_RELOAD
        );
    }

    @Disabled
    @Test
    void chooseAction() {
        optionsHelper(Arrays.asList(
                new Action(DEFAULT_CHOICE, new ArrayList<>()),
                new Action("test1", new ArrayList<>())),
                0,
                toClient::chooseAction,
                Action::getName,
                MessageType.ACTION
        );
    }

    @Disabled
    @Test
    void choosePowerupForPaying() {
        optionsHelper(Arrays.asList(
                new PowerupCard("test1", null, null),
                new PowerupCard("test2", null, null),
                new PowerupCard(DEFAULT_CHOICE, null, null)),
                2,
                toClient::choosePowerupForPaying,
                PowerupCard::getId,
                MessageType.POWERUP_FOR_PAYING
        );
    }

    @Disabled
    @Test
    void askUseTagback() {
        optionsHelper(Arrays.asList(
                new PowerupCard("test1", null, null),
                new PowerupCard("test2", null, null),
                new PowerupCard(DEFAULT_CHOICE, null, null)),
                2,
                toClient::askUseTagback,
                PowerupCard::getId,
                MessageType.USE_TAGBACK
        );
    }

    @Disabled
    @Test
    void chooseTarget() {
        /*Preparing the default choice*/
        socket.put("2");

        try {
            /*Preparing the options*/
            List<List<Damageable>> options = Arrays.asList(
                    Arrays.asList(new MockDamageable("test1")),
                    Arrays.asList(new MockDamageable("test2"),
                            new MockDamageable("test22")),
                    Arrays.asList(new MockDamageable("test3"),
                            new MockDamageable("test32")),
                    Arrays.asList(new MockDamageable("test4")));

            /*Checking the returned list*/
            List<Damageable> answer = toClient.chooseTarget(options);
            List<Damageable> defaultChoice = options.get(2);
            for (int i = 0; i < defaultChoice.size(); i++) {
                assertEquals(defaultChoice.get(i).getName(), answer.get(i).getName());
            }

            /*Checking the message type*/
            ProtocolMessage sent = socket.contents();
            assertEquals(MessageType.TARGET, sent.getCommand());
            String[][] sentOptions = sent.getOptions();

            /*Checking the options provided*/
            for (int i = 0; i < options.size(); i++) {
                for (int j = 0; j < options.get(i).size(); j++)
                    assertEquals(options.get(i).get(j).getName(),
                            sentOptions[i][j]);
            }
        } catch (ToClientException e) {
            fail(e);
        }
    }

    @Disabled
    @Test
    void chooseUserName() {
        /*Preparing the default choice*/
        socket.put(DEFAULT_CHOICE);

        try {
            /*Checking the returned object*/
            String answer = toClient.chooseUserName();
            assertEquals(DEFAULT_CHOICE, answer);

            /*Checking the message type*/
            ProtocolMessage sent = socket.contents();
            assertEquals(MessageType.NICKNAME, sent.getCommand());
        } catch (ToClientException e) {
            fail(e);
        }
    }

    @Disabled
    @Test
    void quit() {
        socket.put(DEFAULT_CHOICE);

        try {
            toClient.quit();

            /*Checking the message type*/
            ProtocolMessage sent = socket.contents();
            assertEquals(MessageType.NOTIFICATION, sent.getCommand());
            assertEquals(Notification.NotificationType.QUIT,
                    sent.getNotifications()[0].getType());
        } catch (ToClientException e) {
            fail(e);
        }
    }

    @FunctionalInterface
    interface FunctionException<T, R> {
        R apply(T t) throws ToClientException, ChoiceRefusedException;
    }

    private class MockSocket extends Socket {
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

        ProtocolMessage contents() {
            byte[] buf = toClient.toByteArray();
            int length = toClient.size();
            toClient.reset();
            return new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(buf, 0, length))).lines()
                    .map(s -> new Gson().fromJson(s, ProtocolMessage.class))
                    .collect(Collectors.toList()).get(0);
        }

        void put(String string) {
            new PrintWriter(toServer, true).println(new Gson().toJson(
                    new ProtocolMessage(MessageType.ACTION, string)
            ));
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

    private class MockDamageable implements Damageable {
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

    private class MockSquare extends Square {
        private String id;

        MockSquare(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }
    }

    private class MockEffect implements EffectInterface {
        EffectInterface next;
        String name;

        MockEffect(String name) {
            this.name = name;
            next = null;
        }

        @Override
        public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> allTargeted, List<Damageable> damageTargeted) {

        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<AmmoCube> getCost() {
            return new ArrayList<>();
        }

    }
}