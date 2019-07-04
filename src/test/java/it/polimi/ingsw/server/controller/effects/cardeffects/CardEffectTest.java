package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.KillshotTrack;
import it.polimi.ingsw.server.model.board.Room;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.persistency.FromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: generic testing with only a couple of effects.
 */
class CardEffectTest {
    private List<Damageable> allTargets;
    private Player subject;
    private Player sameSquare;
    private Player targeted;
    private Player sameRoom;
    private Player visible;
    private Player neverVisible;
    private GameBoard board;
    private MockToClient toClient;
    private Answers a;

    @BeforeEach
    void setUp() {
        List<Room> configuration = FromFile.maps().get("1");
        subject = new Player("subject");
        toClient = new MockToClient();
        subject.setToClient(toClient);
        subject.setPosition(Square.getSquare(configuration, 0));
        sameSquare = new Player("sameSquare");
        sameSquare.setPosition(Square.getSquare(configuration, 0));
        targeted = new Player("targeted");
        targeted.setPosition(Square.getSquare(configuration, 1));
        sameRoom = new Player("SameRoom");
        sameRoom.setPosition(Square.getSquare(configuration, 2));
        visible = new Player("visible");
        visible.setPosition(Square.getSquare(configuration, 5));
        neverVisible = new Player("neverVisible");
        neverVisible.setPosition(Square.getSquare(configuration, 9));
        allTargets = new ArrayList<>();
        allTargets.add(subject);
        allTargets.add(sameSquare);
        allTargets.add(targeted);
        allTargets.add(sameRoom);
        allTargets.add(visible);
        allTargets.add(neverVisible);
        board = new GameBoard(new KillshotTrack(), configuration);

        a = new Answers();
        EffectInterface eff = FromFile.effects().get("lockRifle");
        a.setDamage(eff, 2);
        a.setMarks(eff, 1);
        a.setPosition(eff, null);
        a.setSquares(eff, new ArrayList<>());
        a.setTargets(eff, Arrays.asList(
                Collections.singletonList(sameSquare),
                Collections.singletonList(sameRoom),
                Collections.singletonList(targeted),
                Collections.singletonList(visible)));

        eff = FromFile.effects().get("lockRifle_secondLock");
        a.setDamage(eff, 0);
        a.setMarks(eff,1 );
        a.setPosition(eff, null);
        a.setSquares(eff, new ArrayList<>());
        a.setTargets(eff, Arrays.asList(
                Collections.singletonList(sameSquare),
                Collections.singletonList(sameRoom),
                Collections.singletonList(visible)));
    }

    @Test
    void runEffect() {
        for (EffectInterface effect : Arrays.asList(FromFile.effects().get(
                "lockRifle"), FromFile.effects().get("lockRifle_secondLock")))
        {
            List<Damageable> allTargeted = new ArrayList<>();
            allTargeted.add(targeted);
            List<Damageable> damageTargeted = new ArrayList<>();
            try {
                effect.runEffect(subject, allTargets, board, allTargeted, damageTargeted);
            } catch (ToClientException e) {
                fail(e);
            }
            assertEquals(a.getSquares(effect), toClient.destinations);
            assertTrue(compareLists(a.getTargets(effect), toClient.targets));
            allTargeted.remove(targeted);
            for (Damageable d : allTargeted) {
                assertEquals(a.getDamage(effect), ((Player) d).getPlayerBoard().getDamage().size());
                assertEquals(a.getMarks(effect), ((Player) d).getPlayerBoard().getMarks().size());
                if (a.getPosition(effect) != null)
                    assertEquals(a.getPosition(effect), d.getPosition());
            }
        }
    }

    private boolean compareLists (List<? extends Object> a, List<? extends Object> b) {
        return new HashSet<>(a).equals(new HashSet<>(b));
    }

    private class Answers {
        private Map<String, Integer> damage;
        private Map<String, Integer> marks;
        private Map<String, Square> position;
        private Map<String, List<Square>> squares;
        private Map<String, List<List<Damageable>>> targets;

        public Answers() {
            damage = new HashMap<>();
            marks = new HashMap<>();
            position = new HashMap<>();
            squares = new HashMap<>();
            targets = new HashMap<>();
        }

        public int getDamage(EffectInterface effect) {
            return damage.get(effect.getName());
        }

        public int getMarks(EffectInterface effect) {
            return marks.get(effect.getName());
        }

        public Square getPosition(EffectInterface effect) {
            return position.get(effect.getName());
        }

        public List<Square> getSquares(EffectInterface effect) {
            return squares.get(effect.getName());
        }

        public List<List<Damageable>> getTargets(EffectInterface effect) {
            return targets.get(effect.getName());
        }

        void setDamage(EffectInterface effect, int amount) {
            damage.put(effect.getName(), amount);
        }

        void setMarks(EffectInterface effect, int amount) {
            marks.put(effect.getName(), amount);
        }

        void setSquares(EffectInterface effect, List<Square> list) {
            squares.put(effect.getName(), list);
        }

        void setPosition(EffectInterface effect, Square pos) {
            position.put(effect.getName(), pos);
        }

        void setTargets(EffectInterface effect, List<List<Damageable>> list) {
            targets.put(effect.getName(), list);
        }
    }

    private class MockToClient implements ToClientInterface {
        private List<Square> destinations;
        private List<List<Damageable>> targets;

        MockToClient() {
            destinations = new ArrayList<>();
            targets = new ArrayList<>();
        }

        @Override
        public Action chooseEffectsSequence(List<Action> options) {
            return options.get(0);
        }

        @Override
        public PowerupCard chooseSpawn(List<PowerupCard> options) {
            return options.get(0);
        }

        @Override
        public PowerupCard choosePowerup(List<PowerupCard> options) {
            return options.get(0);
        }

        @Override
        public Square chooseDestination(List<Square> options) {
            destinations = options;
            return options.get(0);
        }

        @Override
        public WeaponCard chooseWeaponCard(List<WeaponCard> options) {
            return options.get(0);
        }

        @Override
        public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) {
            return options.get(0);
        }

        @Override
        public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) {
            return options.get(0);
        }

        @Override
        public WeaponCard chooseWeaponToReload(List<WeaponCard> options) {
            return options.get(0);
        }

        @Override
        public Action chooseAction(List<Action> options) {
            return options.get(0);
        }

        @Override
        public PowerupCard choosePowerupForPaying(List<PowerupCard> options) {
            return options.get(0);
        }

        @Override
        public PowerupCard askUseTagback(List<PowerupCard> options) {
            return options.get(0);
        }

        @Override
        public List<Damageable> chooseTarget(List<List<Damageable>> options) {
            targets = options;
            return options.get(0);
        }

        @Override
        public String chooseUserName() {
            return "name";
        }

        @Override
        public void quit() {

        }

        @Override
        public void sendNotification(Notification.NotificationType type) {

        }

        @Override
        public void sendUpdate(UpdateBuilder update) {

        }
    }
}