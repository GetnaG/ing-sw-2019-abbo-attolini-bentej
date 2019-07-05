package it.polimi.ingsw.server.model;

/**
 * Represents an Exception thrown in case there is an in-game move against the rules of Adrenaline.
 *
 * @author Fahed Ben Tej
 */
public class AgainstRulesException extends Exception {

    /**
     * Construct an AgainstRulesException with the given message error.
     *
     * @param msg the message error
     */
    public AgainstRulesException(String msg) {
        super(msg);
    }
}
