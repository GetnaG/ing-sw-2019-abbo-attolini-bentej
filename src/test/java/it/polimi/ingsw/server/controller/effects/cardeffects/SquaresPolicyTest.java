package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.board.*;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.persistency.FromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.controller.effects.cardeffects.SquaresPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: this is a fundamental class, extensive testing is done to ensure its behaviour.
 */
class SquaresPolicyTest {
    private List<List<Room>> configurationList;
    private Answers a;
    private Player subject;

    @BeforeEach
    void setUp() {
        configurationList = FromFile.maps().getAll();
        subject = new Player("subject");
        a = new Answers();

        /*Config 0: big in the manual*/
        a.setVisible(0, 0, Arrays.asList(0, 4, 1, 2));
        a.setVisible(0, 1, Arrays.asList(1, 2, 5, 6, 0, 4));
        a.setVisible(0, 2, Arrays.asList(1, 2, 5, 6));
        a.setVisible(0, 4, Arrays.asList(0, 4, 8, 9, 10));
        a.setVisible(0, 5, Arrays.asList(5, 6, 1, 2, 8, 9, 10));
        a.setVisible(0, 6, Arrays.asList(5, 6, 1, 2, 7, 11));
        a.setVisible(0, 7, Arrays.asList(7, 11, 5, 6));
        a.setVisible(0, 8, Arrays.asList(8, 9, 10, 0, 4));
        a.setVisible(0, 9, Arrays.asList(8, 9, 10, 5, 6));
        a.setVisible(0, 10, Arrays.asList(8, 9, 10, 7, 11));
        a.setVisible(0, 11, Arrays.asList(7, 11, 8, 9, 10));
        a.setCardinals(0, 0, Arrays.asList(0, 1, 2, 4, 8));
        a.setCardinals(0, 1, Arrays.asList(0, 1, 2, 5, 9));
        a.setCardinals(0, 2, Arrays.asList(0, 1, 2, 6, 10));
        a.setCardinals(0, 4, Arrays.asList(4, 5, 6, 7, 0, 8));
        a.setCardinals(0, 5, Arrays.asList(4, 5, 6, 7, 1, 9));
        a.setCardinals(0, 6, Arrays.asList(4, 5, 6, 7, 2, 10));
        a.setCardinals(0, 7, Arrays.asList(4, 5, 6, 7, 11));
        a.setCardinals(0, 8, Arrays.asList(8, 9, 10, 11, 0, 4));
        a.setCardinals(0, 9, Arrays.asList(8, 9, 10, 11, 1, 5));
        a.setCardinals(0, 10, Arrays.asList(8, 9, 10, 11, 2, 6));
        a.setCardinals(0, 11, Arrays.asList(8, 9, 10, 11, 7));

        /*Config 1: 3 or 4*/
        a.setVisible(1, 0, Arrays.asList(0, 1, 2, 4, 5, 6));
        a.setVisible(1, 1, Arrays.asList(0, 1, 2));
        a.setVisible(1, 2, Arrays.asList(0, 1, 2, 4, 5, 6));
        a.setVisible(1, 4, Arrays.asList(4, 5, 6, 0, 1, 2));
        a.setVisible(1, 5, Arrays.asList(4, 5, 6, 9, 10));
        a.setVisible(1, 6, Arrays.asList(4, 5, 6, 0, 1, 2, 7, 11));
        a.setVisible(1, 7, Arrays.asList(7, 11, 4, 5, 6));
        a.setVisible(1, 9, Arrays.asList(9, 10, 4, 5, 6));
        a.setVisible(1, 10, Arrays.asList(9, 10, 7, 11));
        a.setVisible(1, 11, Arrays.asList(7, 11, 9, 10));
        a.setCardinals(1, 0, Arrays.asList(0, 1, 2, 4));
        a.setCardinals(1, 1, Arrays.asList(0, 1, 2, 5, 9));
        a.setCardinals(1, 2, Arrays.asList(0, 1, 2, 6, 10));
        a.setCardinals(1, 4, Arrays.asList(4, 5, 6, 7, 0));
        a.setCardinals(1, 5, Arrays.asList(4, 5, 6, 7, 1, 9));
        a.setCardinals(1, 6, Arrays.asList(4, 5, 6, 7, 2, 10));
        a.setCardinals(1, 7, Arrays.asList(4, 5, 6, 7, 11));
        a.setCardinals(1, 9, Arrays.asList(9, 10, 11, 1, 5));
        a.setCardinals(1, 10, Arrays.asList(9, 10, 11, 2, 6));
        a.setCardinals(1, 11, Arrays.asList(9, 10, 11, 7));

        /*Config 2: any on the manual*/
        a.setVisible(2, 0, Arrays.asList(0, 1, 2, 4, 5));
        a.setVisible(2, 1, Arrays.asList(0, 1, 2));
        a.setVisible(2, 2, Arrays.asList(0, 1, 2, 3, 6, 7, 10, 11));
        a.setVisible(2, 3, Arrays.asList(3, 0, 1, 2, 6, 7, 10, 11));
        a.setVisible(2, 4, Arrays.asList(4, 5, 0, 1, 2));
        a.setVisible(2, 5, Arrays.asList(4, 5, 9));
        a.setVisible(2, 6, Arrays.asList(6, 7, 10, 11, 0, 1, 2));
        a.setVisible(2, 7, Arrays.asList(6, 7, 10, 11, 3));
        a.setVisible(2, 9, Arrays.asList(9, 6, 7, 10, 11, 4, 5));
        a.setVisible(2, 10, Arrays.asList(6, 7, 10, 11, 9));
        a.setVisible(2, 11, Arrays.asList(6, 7, 10, 11));
        a.setCardinals(2, 0, Arrays.asList(0, 1, 2, 3, 4));
        a.setCardinals(2, 1, Arrays.asList(0, 1, 2, 3, 5, 9));
        a.setCardinals(2, 2, Arrays.asList(0, 1, 2, 3, 6, 10));
        a.setCardinals(2, 3, Arrays.asList(0, 1, 2, 3, 7, 11));
        a.setCardinals(2, 4, Arrays.asList(4, 5, 6, 7, 0));
        a.setCardinals(2, 5, Arrays.asList(4, 5, 6, 7, 1, 9));
        a.setCardinals(2, 6, Arrays.asList(4, 5, 6, 7, 2, 10));
        a.setCardinals(2, 7, Arrays.asList(4, 5, 6, 7, 3, 11));
        a.setCardinals(2, 9, Arrays.asList(9, 10, 11, 1, 5));
        a.setCardinals(2, 10, Arrays.asList(9, 10, 11, 2, 6));
        a.setCardinals(2, 11, Arrays.asList(9, 10, 11, 3, 7));

        /*Config 3: 4 or 5*/
        a.setVisible(3, 0, Arrays.asList(0, 4, 1, 2));
        a.setVisible(3, 1, Arrays.asList(1, 2, 0, 4, 5));
        a.setVisible(3, 2, Arrays.asList(1, 2, 3, 6, 7, 10, 11));
        a.setVisible(3, 3, Arrays.asList(3, 1, 2, 6, 7, 10, 11));
        a.setVisible(3, 4, Arrays.asList(0, 4, 8, 9));
        a.setVisible(3, 5, Arrays.asList(5, 1, 2, 8, 9));
        a.setVisible(3, 6, Arrays.asList(6, 7, 10, 11, 1, 2));
        a.setVisible(3, 7, Arrays.asList(6, 7, 10, 11, 3));
        a.setVisible(3, 8, Arrays.asList(8, 9, 0, 4));
        a.setVisible(3, 9, Arrays.asList(8, 9, 5, 6, 7, 10, 11));
        a.setVisible(3, 10, Arrays.asList(6, 7, 10, 11, 8, 9));
        a.setVisible(3, 11, Arrays.asList(6, 7, 10, 11));
        a.setCardinals(3, 0, Arrays.asList(0, 1, 2, 3, 4, 8));
        a.setCardinals(3, 1, Arrays.asList(0, 1, 2, 3, 5, 9));
        a.setCardinals(3, 2, Arrays.asList(0, 1, 2, 3, 6, 10));
        a.setCardinals(3, 3, Arrays.asList(0, 1, 2, 3, 7, 11));
        a.setCardinals(3, 4, Arrays.asList(4, 5, 6, 7, 0, 8));
        a.setCardinals(3, 5, Arrays.asList(4, 5, 6, 7, 1, 9));
        a.setCardinals(3, 6, Arrays.asList(4, 5, 6, 7, 2, 10));
        a.setCardinals(3, 7, Arrays.asList(4, 5, 6, 7, 3, 11));
        a.setCardinals(3, 8, Arrays.asList(8, 9, 10, 11, 0, 4));
        a.setCardinals(3, 9, Arrays.asList(8, 9, 10, 11, 1, 5));
        a.setCardinals(3, 10, Arrays.asList(8, 9, 10, 11, 2, 6));
        a.setCardinals(3, 11, Arrays.asList(8, 9, 10, 11, 3, 7));
    }

    /*Testing VISIBLE with all squares in every configuration*/
    @Test
    void getValidDestinations_visible() {
        for (int conf = 0; conf < configurationList.size(); conf++) {
            GameBoard board = new GameBoard(new KillshotTrack(), configurationList.get(conf));
            for (Room room : configurationList.get(conf)) {
                for (Square square : room.getAllSquares()) {
                    subject.setPosition(square);
                    try {
                        assertEquals(a.getVisible(conf, square),
                                VISIBLE.getValidDestinations(subject, board, new ArrayList<>()));
                    } catch (AgainstRulesException e) {
                        fail(e);
                    }
                }
            }
        }
    }

    /*Testing VISIBLE_NOT_SELF with all squares in every configuration*/
    @Test
    void getValidDestinations_visibleNotSelf() {
        for (int conf = 0; conf < configurationList.size(); conf++) {
            GameBoard board = new GameBoard(new KillshotTrack(), configurationList.get(conf));
            for (Room room : configurationList.get(conf)) {
                for (Square square : room.getAllSquares()) {
                    subject.setPosition(square);
                    try {
                        assertEquals(a.getVisibleNotSelf(conf, square),
                                VISIBLE_NOT_SELF.getValidDestinations(subject, board, new ArrayList<>()));
                    } catch (AgainstRulesException e) {
                        fail(e);
                    }
                }
            }
        }
    }

    /*Testing SUBJECT_CARDINALS with all squares in every configuration*/
    @Test
    void getValidDestinations_subjectCardinals() {
        for (int conf = 0; conf < configurationList.size(); conf++) {
            GameBoard board = new GameBoard(new KillshotTrack(), configurationList.get(conf));
            for (Room room : configurationList.get(conf)) {
                for (Square square : room.getAllSquares()) {
                    subject.setPosition(square);
                    try {
                        assertEquals(a.getCardinals(conf, square),
                                SUBJECT_CARDINALS.getValidDestinations(subject, board, new ArrayList<>()));
                    } catch (AgainstRulesException e) {
                        fail(e);
                    }
                }
            }
        }
    }

    /*Testing ALL with all squares in every configuration*/
    @Test
    void getValidDestinations_all() {
        for (List<Room> rooms : configurationList) {
            GameBoard board = new GameBoard(new KillshotTrack(), rooms);
            Set<Square> all = rooms.stream().flatMap(room -> room.getAllSquares().stream()).collect(Collectors.toSet());
            for (Room room : rooms) {
                for (Square square : room.getAllSquares()) {
                    subject.setPosition(square);
                    try {
                        assertEquals(all, ALL.getValidDestinations(subject, board, new ArrayList<>()));
                    } catch (AgainstRulesException e) {
                        fail(e);
                    }
                }
            }
        }
    }

    /*Testing TO_SUBJECT and NONE, which return null*/
    @Test
    void getValidDestinations_toSubject_none() {
        for (List<Room> rooms : configurationList) {
            GameBoard board = new GameBoard(new KillshotTrack(), rooms);
            for (Room room : rooms) {
                for (Square square : room.getAllSquares()) {
                    subject.setPosition(square);
                    try {
                        assertNull(TO_SUBJECT.getValidDestinations(subject, board, new ArrayList<>()));
                        assertNull(NONE.getValidDestinations(subject, board, new ArrayList<>()));
                    } catch (AgainstRulesException e) {
                        fail(e);
                    }
                }
            }
        }
    }

    /*Testing TO_PREVIOUS, only generic case*/
    @Test
    void getValidDestinations_toPrevious() {
        Player prev = new Player("prev");
        prev.setPosition(new Square(SquareColor.BLUE));

        try {
            assertEquals(prev.getPosition(), TO_PREVIOUS.getValidDestinations(subject,
                    null, Collections.singletonList(prev)).toArray()[0]);
        } catch (AgainstRulesException | NullPointerException e) {
            fail(e);
        }

        assertThrows(AgainstRulesException.class,
                () -> TO_PREVIOUS.getValidDestinations(subject, null, new ArrayList<>()));
    }

    /*Testing TO_SUBJECT.apply with generic input*/
    @Test
    void apply_toSubject() {
        Player target = new Player("target");
        target.setPosition(new Square(SquareColor.BLUE));
        subject.setPosition(new Square(SquareColor.RED));

        TO_SUBJECT.apply(subject, target, null);

        assertEquals(subject.getPosition(), target.getPosition());
    }

    /*Testing other values with generic input*/
    @Test
    void apply_other() {
        Square initial = new Square(SquareColor.BLUE);
        Player target = new Player("target");

        for (SquaresPolicy policy : values()) {
            if (policy != TO_SUBJECT && policy != SUBJECT_CARDINALS_STAY) {
                /*No destinations*/
                target.setPosition(initial);
                policy.apply(null, target, null);
                assertEquals(initial, target.getPosition());

                /*Empty destinations*/
                policy.apply(null, target, new HashSet<>());
                assertEquals(initial, target.getPosition());

                /*Some destinations*/
                List<Square> destinations = Collections.singletonList(new Square(SquareColor.RED));
                policy.apply(null, target, new HashSet<>(destinations));
                assertEquals(destinations.get(0), target.getPosition());
            }
        }


    }

    private class Answers {
        private List<Map<Square, Set<Square>>> visible;
        private List<Map<Square, Set<Square>>> visibleNotSelf;
        private List<Map<Square, Set<Square>>> cardinals;

        Answers() {
            visible = new ArrayList<>(Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>()));
            visibleNotSelf = new ArrayList<>(Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>()));
            cardinals = new ArrayList<>(Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>()));
        }

        void setVisible(int confID, int id, List<Integer> valuesID) {
            Set<Square> values = valuesID.stream()
                    .map(s -> Square.getSquare(configurationList.get(confID), s))
                    .collect(Collectors.toSet());
            Square square = Square.getSquare(configurationList.get(confID), id);

            visible.get(confID).put(square, new HashSet<>(values));
            values.remove(square);
            visibleNotSelf.get(confID).put(square, values);
        }

        void setCardinals(int confID, int id, List<Integer> valuesID) {
            Set<Square> values = valuesID.stream()
                    .map(s -> Square.getSquare(configurationList.get(confID), s))
                    .collect(Collectors.toSet());
            cardinals.get(confID).put(Square.getSquare(configurationList.get(confID), id), values);
        }

        Set<Square> getVisible(int confID, Square square) {
            return visible.get(confID).get(square);
        }

        Set<Square> getVisibleNotSelf(int confID, Square square) {
            return visibleNotSelf.get(confID).get(square);
        }

        Set<Square> getCardinals(int confID, Square square) {
            return cardinals.get(confID).get(square);
        }
    }
}