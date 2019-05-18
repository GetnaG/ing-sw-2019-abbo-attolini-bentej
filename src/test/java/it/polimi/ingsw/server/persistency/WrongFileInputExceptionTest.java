package it.polimi.ingsw.server.persistency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: constructors.
 */
class WrongFileInputExceptionTest {

    @Test
    void constructor_1() {
        String path = "path";
        String element = "element";

        String message =
                new WrongFileInputException(path, element).getMessage();

        assertEquals("Wrong element " + element + " in file: " + path, message);
    }

    @Test
    void constructor_2() {
        String path = "path";
        String element = "element";
        Exception e = new Exception();

        Exception wfie = new WrongFileInputException(path, element, e);

        assertEquals("Wrong element " + element + " in file: " + path, wfie.getMessage());
        assertEquals(e, wfie.getCause());
    }
}