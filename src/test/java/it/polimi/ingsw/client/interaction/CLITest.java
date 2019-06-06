package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.BoardState;
import it.polimi.ingsw.client.clientlogic.MatchState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
 * Author: Abbo Giulio A.
 * Testing: testing the returned value.
 */
class CLITest {
    private static final int PRINTLN_IN_REFRESH = 3;
    private static final int DEFAULT_CHOICE = 2;
    private List<List<String>> options;

    private CLI cli;
    private MockInput in;
    private MockOutput out;
    private MockModel model;

    @Disabled
    @BeforeEach
    void setUp() {
        in = new MockInput();
        out = new MockOutput();
        model = new MockModel();
        cli = new CLI(out, in);
        options = Arrays.asList(
                Arrays.asList("gameTest", "gameTest", "gameTest"),
                Arrays.asList("gameTest"),
                Arrays.asList("gameTest", "gameTest")
        );
    }

    @Disabled
    void notifyUpdatedState() {
        cli.notifyUpdatedState();
        for (String s : out.get(PRINTLN_IN_REFRESH)) {
            assertNotNull(s);
        }
    }

    @Disabled
    @Test
    void chooseEffectSequence() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseEffectSequence(options));
    }

    @Disabled
    @Test
    void chooseSpawn() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseSpawn(options));
    }

    @Disabled
    @Test
    void choosePowerup() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.choosePowerup(options));
    }

    @Disabled
    @Test
    void chooseDestination() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseDestination(options));
    }

    @Disabled
    @Test
    void chooseWeapon() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseWeapon(options));
    }

    @Disabled
    @Test
    void chooseWeaponToBuy() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseWeaponToBuy(options));
    }

    @Disabled
    @Test
    void chooseWeaponToDiscard() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseWeaponToDiscard(options));
    }

    @Disabled
    @Test
    void chooseWeaponToReload() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseWeaponToReload(options));
    }

    @Disabled
    @Test
    void chooseAction() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseAction(options));
    }

    @Disabled
    @Test
    void choosePowerupForPaying() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.choosePowerupForPaying(options));
    }

    @Disabled
    @Test
    void chooseUseTagBack() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseUseTagBack(options));
    }

    @Disabled
    @Test
    void chooseTarget() {
        in.put(Integer.toString(DEFAULT_CHOICE));
        assertEquals(DEFAULT_CHOICE, cli.chooseTarget(options));
    }

    @Disabled
    @Test
    void askName() {
        in.put("name");
        assertEquals("name", cli.askName());
    }

    @Disabled
    @Test
    void sendNotification() {
        cli.sendNotification("gameTest");
        for (String s : out.get(PRINTLN_IN_REFRESH)) {
            assertNotNull(s);
        }
    }

    private class MockInput extends InputStream {
        private ByteArrayInputStream stream;

        MockInput() {
            stream = new ByteArrayInputStream("".getBytes());
        }

        @Override
        public int read() {
            return stream.read();
        }

        void put(String string) {
            stream = new ByteArrayInputStream(string.getBytes());
        }
    }

    private class MockOutput extends PrintStream {
        private List<String> printed;

        MockOutput() {
            super(new ByteArrayOutputStream());
            printed = new ArrayList<>();
        }

        @Override
        public void println(Object x) {
            printed.add(0, x.toString());
        }

        List<String> get(int numOfElements) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < numOfElements; i++) {
                try {
                    list.add(0, printed.get(i));
                } catch (IndexOutOfBoundsException e) {
                    list.add(null);
                }
            }
            return list;
        }
    }

    private class MockModel extends MatchState {
        //TODO
    }
}