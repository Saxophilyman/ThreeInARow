package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.element.*;
import static org.junit.jupiter.api.Assertions.*;


public class CommonElementCompatibilityTest {
    @Test
    void baseElementsMatchOnlyWithSameType() {
        CommonElement a1 = BaseElement.of(ElementType.A);
        CommonElement a2 = BaseElement.of(ElementType.A);
        CommonElement b  = BaseElement.of(ElementType.B);

        assertTrue(a1.isCorrectElement(a2));
        assertFalse(a1.isCorrectElement(b));
        assertFalse(b.isCorrectElement(a1));
    }

//    @Test
//    void bonusDoesNotParticipateInNormalMatches() {
//        CommonElement a = BaseElement.of(ElementType.A);
//        CommonElement bonus = BonusElement.instance();
//
//        assertFalse(a.isCorrectElement(bonus));
//        assertFalse(bonus.isCorrectElement(a));
//        assertFalse(bonus.isCorrectElement(bonus));
//    }
}
