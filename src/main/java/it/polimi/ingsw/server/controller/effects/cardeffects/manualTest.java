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

import java.util.*;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class manualTest {
    List<Damageable> allTargets;
    Player subject;
    Player sameSquare;
    Player targeted;
    Player sameRoom;
    Player visible;
    Player neverVisible;
    Player oneMove;
    GameBoard board;
    MockToClient toClient;

    public static void main(String[] args) {
        manualTest effectTest = new manualTest();
        //for (EffectInterface effect : FromFile.effects().getAll())
        EffectInterface effect = FromFile.effects().get(
                "shockwave_tsunami");
        {
            effectTest.setUp();
            try {
                System.out.println(effect.getName());
                effect.runEffect(effectTest.subject, effectTest.allTargets,
                        effectTest.board,
                        new ArrayList<>(Arrays.asList(effectTest.targeted,
                                effectTest.sameRoom, effectTest.sameRoom))
                        , new ArrayList<>());
                for (Damageable d : effectTest.allTargets) {
                    System.out.println(d.getName() + " " + ((Player) d).getPlayerBoard().getDamage() + " " + ((Player) d).getPlayerBoard().getMarks() + " " + d.getPosition().getID());
                    d.scoreAndResetDamage();
                }
                System.out.println(" ");
            } catch (ToClientException e) {
                e.printStackTrace();
            }
        }
    }

    void setUp() {
        List<Room> configuration = FromFile.maps().get("1");
        subject = new Player("subject");
        toClient = new MockToClient();
        subject.setToClient(toClient);
        subject.setPosition(Square.getSquare(configuration, 5));
        sameSquare = new Player("sameSquare");
        sameSquare.setPosition(Square.getSquare(configuration, 5));
        targeted = new Player("targeted");
        targeted.setPosition(Square.getSquare(configuration, 1));
        sameRoom = new Player("SameRoom");
        sameRoom.setPosition(Square.getSquare(configuration, 4));
        visible = new Player("visible");
        visible.setPosition(Square.getSquare(configuration, 9));
        neverVisible = new Player("neverVisible");
        neverVisible.setPosition(Square.getSquare(configuration, 0));
        oneMove = new Player("oneMove");
        oneMove.setPosition(Square.getSquare(configuration, 6));
        allTargets = new ArrayList<>();
        allTargets.add(subject);
        allTargets.add(sameSquare);
        allTargets.add(targeted);
        allTargets.add(sameRoom);
        allTargets.add(visible);
        allTargets.add(neverVisible);
        allTargets.add(oneMove);
        board = new GameBoard(new KillshotTrack(), configuration);
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
            options.stream().map(Square::getID).forEach(System.out::println);
            try {
                return options.get(new Scanner(System.in).nextInt());
            } catch (Exception e) {
                return chooseDestination(options);
            }
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
            options.stream().map(list -> list.stream().map(Damageable::getName).reduce((a, e) -> a + " " + e)).forEach(System.out::println);
            try {
                return options.get(new Scanner(System.in).nextInt());
            } catch (Exception e) {
                return chooseTarget(options);
            }
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
