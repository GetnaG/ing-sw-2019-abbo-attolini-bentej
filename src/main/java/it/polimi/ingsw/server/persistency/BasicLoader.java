package it.polimi.ingsw.server.persistency;

import java.util.List;

/**
 * Implementing classes will handle loading and retrieving objects from file.
 * The implementation must be able to function in a multithreaded environment.
 *
 * @author Abbo Giulio A.
 */
public interface BasicLoader<T> {
    /**
     * Returns the first object with a matching {@code id} ignoring case.
     *
     * @param id the identifier of the requested object
     * @return the first object found with the requested {@code id}
     * @throws java.util.NoSuchElementException if no match is found
     */
    T get(String id);

    /**
     * Returns all the object loaded.
     *
     * @return all the object loaded
     */
    List<T> getAll();

    /**
     * Returns all the objects with a matching {@code id} ignoring case.
     *
     * @param id the identifier of the requested objects
     * @return all the objects found with the requested {@code id}
     */
    List<T> getAll(String id);
}
