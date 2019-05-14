package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Author: Abbo Giulio A.
 */
class TypeTest {

    /*Testing that the right element is returned*/
    @Test
    void with() {
        for (Type element : Type.values())
            assertEquals(element, Type.with(element.getCommand()));
    }

    /*Testing that getCommand returns the same as toString*/
    @Test
    void getCommand() {
        for (Type element : Type.values())
            assertEquals(element.toString(), element.getCommand());
    }

    /*Testing that toStrings returns the command*/
    @Test
    void toString1() {
        for (Type element : Type.values())
            assertEquals(element.getCommand(), element.toString());
    }
}