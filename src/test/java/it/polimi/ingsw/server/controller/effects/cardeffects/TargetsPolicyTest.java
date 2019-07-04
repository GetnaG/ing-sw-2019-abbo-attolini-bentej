package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.KillshotTrack;
import it.polimi.ingsw.server.model.board.Room;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.persistency.FromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static it.polimi.ingsw.server.controller.effects.cardeffects.TargetsPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: this is a fundamental class, extensive testing is done to ensure its behaviour.
 */
class TargetsPolicyTest {
    private Set<Damageable> allTargets;
    private Player subject;
    private Player sameSquare;
    private Player sameRoom;
    private Player visible;
    private Player notVisible;
    private GameBoard board;

    @BeforeEach
    void setUp() {
        List<Room> configuration = FromFile.maps().get("1");
        subject = new Player("subject");
        subject.setPosition(Square.getSquare(configuration, 0));
        sameSquare = new Player("sameSquare");
        sameSquare.setPosition(Square.getSquare(configuration, 0));
        sameRoom = new Player("SameRoom");
        sameRoom.setPosition(Square.getSquare(configuration, 2));
        visible = new Player("visible");
        visible.setPosition(Square.getSquare(configuration, 5));
        notVisible = new Player("notVisible");
        notVisible.setPosition(Square.getSquare(configuration, 9));

        allTargets = new HashSet<>();
        allTargets.add(subject);
        allTargets.add(sameSquare);
        allTargets.add(sameRoom);
        allTargets.add(visible);
        allTargets.add(notVisible);

        board = new GameBoard(new KillshotTrack(), configuration);
    }

    @Test
    void getValidTargets_visible() {
        Set<Damageable> targets = new HashSet<>();
        targets.add(sameSquare);
        targets.add(sameRoom);
        targets.add(visible);

        try {
            assertEquals(targets, VISIBLE.getValidTargets(subject, allTargets,
                    new ArrayList<>(), board));
        } catch (AgainstRulesException e) {
            fail(e);
        }
    }

    @Test
    void getValidTargets_notVisible() {
        Set<Damageable> targets = new HashSet<>();
        targets.add(notVisible);

        try {
            assertEquals(targets, NOT_VISIBLE.getValidTargets(subject, allTargets,
                    new ArrayList<>(), board));
        } catch (AgainstRulesException e) {
            fail(e);
        }
    }

    @Test
    void getValidTargets_visibleByPrevious() {
        Set<Damageable> targets = new HashSet<>();
        targets.add(sameSquare);
        targets.add(sameRoom);
        targets.add(visible);

        try {
            assertEquals(targets, VISIBLE_BY_PREVIOUS.getValidTargets(notVisible,
                    allTargets, Collections.singletonList(subject), board));
        } catch (AgainstRulesException e) {
            fail(e);
        }

        assertThrows(AgainstRulesException.class, () ->
                VISIBLE_BY_PREVIOUS.getValidTargets(notVisible, allTargets,
                        new ArrayList<>(), board));
    }

    @Test
    void getValidTargets_all() {
        Set<Damageable> targets = new HashSet<>();
        targets.add(sameSquare);
        targets.add(sameRoom);
        targets.add(visible);
        targets.add(notVisible);

        try {
            assertEquals(targets, ALL.getValidTargets(subject, allTargets,
                    new ArrayList<>(), board));
        } catch (AgainstRulesException e) {
            fail(e);
        }
    }
}