package it.polimi.ingsw.server.persistency;

import java.util.List;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public interface BasicLoader {
    Object get(String id);

    List<Object> getAll();

    List<Object> getAll(String id);
}
