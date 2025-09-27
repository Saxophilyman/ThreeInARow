package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.element.BaseElement;
import ru.saxophilyman.threeinarow.core.element.ElementFactory;
import ru.saxophilyman.threeinarow.core.element.ElementType;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.rules.MatchPolicyLine3Plus;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MatchPolicyLine3PlusTest {
    @Test
    void detectsHorizontalAndVertical() {
        var provider = new ElementFactory(new Random(1));
        var f = new BaseField(5, 5, provider);
        // заполняем чем угодно, затем подготовим конкретные линии
        f.clear();
        // горизонтальная A A A на (2,0..2)
        var A = BaseElement.of(ElementType.A);
        f.set(2, 0, A);
        f.set(2, 1, A);
        f.set(2, 2, A);
        // вертикальная B B B на (0..2,4)
        var B = BaseElement.of(ElementType.B);
        f.set(0, 4, B);
        f.set(1, 4, B);
        f.set(2, 4, B);

        var mp = new MatchPolicyLine3Plus();
        var res = mp.findMatches(f);

        assertFalse(res.isEmpty());
        // в union должны быть 6 уникальных клеток (3 горизонт + 3 вертикаль)
        assertEquals(6, res.totalCells());
    }
}
