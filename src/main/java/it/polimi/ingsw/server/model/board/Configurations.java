package it.polimi.ingsw.server.model.board;

/**
 * Represents the available configurations.
 * A visual example can be found in @LabelBundle.properties.
 */
public enum Configurations {
    STANDARD1(0), STANDARD2(2), ADVISED34(1), ADVISED45(3);

    private final int id;

    private Configurations(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
