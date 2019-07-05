package it.polimi.ingsw.server.model.board;

/**
 * WALL Represents a wall between Squares and/or Rooms. Cannot be crossed. Graphical element: *******.
 * DOOR Represents a door between Squares. Can be crossed. Graphical element: *** ***.
 * CORRIDOR Represents a corridor among the Squares of a Room. Can be crossed. Graphical element: *     *.
 */
public enum Border {
    WALL, DOOR, CORRIDOR
}
