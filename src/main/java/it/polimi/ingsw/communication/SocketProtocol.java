package it.polimi.ingsw.communication;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public enum SocketProtocol {
    EFFECTS_SEQUENCE("effect sequence"),
    SPAWN("spawn"),
    POWERUP("powerup"),
    DESTINATION("destination"),
    WEAPON_CARD("weapon card"),
    WEAPON_TO_BUY("weapon to buy"),
    WEAPON_TO_DISCARD("weapon to discard"),
    WEAPON_TO_RELOAD("weapon to reload"),
    ACTION("action"),
    POWERUP_FOR_PAYING("powerup for paying"),
    USE_TAGBACK("use tagback"),
    TARGET("target"),
    NICKNAME("choose name"),
    START_OF_LIST("Choose one of the following:"),
    END_OF_LIST("End of the list."),
    START_LIST_OF_LISTS("List of lists"),
    END_LIST_OF_LISTS("End list of lists");

    private final String command;

    SocketProtocol(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
