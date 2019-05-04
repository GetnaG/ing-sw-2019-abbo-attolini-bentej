package it.polimi.ingsw.server.persistency;

/**
 * Signals that a resource loaded from file is not valid.
 * <p>
 * This is an unchecked exception because it is dictated by an error in the
 * configuration files from which recovery can not be performed automatically.
 *
 * @author Abbo Giulio A.
 */
class WrongFileInputException extends RuntimeException {

    /**
     * Constructs a new exception with a message containing the provided info.
     * The message will be: <i>"Wrong element</i> {@code wrongElement} <i>in
     * file:</i> {@code filePath} <i>"</i>.
     *
     * @param filePath     the location of the file which generated an error
     * @param wrongElement an indication of where in the file the error occurred
     */
    WrongFileInputException(String filePath, String wrongElement) {
        super("Wrong element " + wrongElement + " in file: " + filePath);
    }

    /**
     * Constructs a new exception with a message containing the provided info.
     * The message will be: <i>"Wrong element</i> {@code wrongElement} <i>in
     * file:</i> {@code filePath} <i>"</i>.
     * The {@code cause} will be attached.
     *
     * @param filePath     the location of the file which generated an error
     * @param wrongElement an indication of where in the file the error occurred
     * @param cause        the throwable that caused this throwable to get thrown
     */
    WrongFileInputException(String filePath, String wrongElement,
                            Exception cause) {
        super("Wrong element " + wrongElement + " in file: " + filePath, cause);
    }
}
