package it.polimi.ingsw.communication;

/**
 * Thrown if there is a problem when communicating with the client.
 * This can be due to an issue with the communication medium or if the client
 * does not answer after some time.
 *
 * @author giubots
 */
public class ToClientException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the details, should tell why this was thrown
     */
    public ToClientException(String message) {
        super("Communication exception, " + message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param message the details, should tell why this was thrown
     * @param cause   the throwable that caused this throwable to get thrown
     */
    public ToClientException(String message, Throwable cause) {
        super("Communication exception, " + message, cause);
    }
}
