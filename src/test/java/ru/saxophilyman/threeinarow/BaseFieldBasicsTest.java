package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.element.*;
import ru.saxophilyman.threeinarow.core.field.*;


import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BaseFieldBasicsTest {
    @Test
    void setGetAndSwap() {
        ElementProvider provider = new ElementFactory(new Random(1));
        BaseField f = new BaseField(6, 6, provider);

        CommonElement A = BaseElement.of(ElementType.A);
        CommonElement B = BaseElement.of(ElementType.B);

        f.set(new Position(0,0), A);
        f.set(new Position(0,1), B);

        assertSame(A, f.get(new Position(0,0)));
        assertSame(B, f.get(new Position(0,1)));

        f.swap(new Position(0,0), new Position(0,1));

        assertSame(B, f.get(new Position(0,0)));
        assertSame(A, f.get(new Position(0,1)));
    }

    @Test
    void inBoundsWorks() {
        ElementProvider provider = new ElementFactory();
        BaseField f = new BaseField(6, 6, provider);

        assertTrue(f.inBounds(new Position(0,0)));
        assertFalse(f.inBounds(new Position(-1,0)));
        assertFalse(f.inBounds(new Position(0,7)));
    }

}
