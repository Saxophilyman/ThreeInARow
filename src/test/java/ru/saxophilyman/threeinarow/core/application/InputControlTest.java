package ru.saxophilyman.threeinarow.core.application;

import org.junit.jupiter.api.Test;

import ru.saxophilyman.threeinarow.core.field.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputControlTest {
    @Test
    void parseHelpQuit() {
        var ic = new InputControl();
        assertTrue(ic.parse("q") instanceof Command.Quit);
        assertTrue(ic.parse("help") instanceof Command.Help);
    }

    @Test void parseCells() {
        var ic = new InputControl();
        var cmd = ic.parse("A2 B3");
        assertTrue(cmd instanceof Command.Move);
        var m = (Command.Move) cmd;
        assertEquals(new Position(1,0), m.a()); // A2 -> row=1 col=0
        assertEquals(new Position(2,1), m.b()); // B3 -> row=2 col=1
    }

}
