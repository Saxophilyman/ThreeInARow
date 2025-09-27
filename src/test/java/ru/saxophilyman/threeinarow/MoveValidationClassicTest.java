package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.element.BaseElement;
import ru.saxophilyman.threeinarow.core.element.ElementFactory;
import ru.saxophilyman.threeinarow.core.element.ElementType;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.field.Position;
import ru.saxophilyman.threeinarow.core.rules.MatchPolicyLine3Plus;
import ru.saxophilyman.threeinarow.core.rules.MoveValidationClassic;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveValidationClassicTest {
    @Test
    void validSwapCreatesMatch() {
        var provider = new ElementFactory(new Random(2));
        var f = new BaseField(5, 5, provider);  // учитываем минимальный размер 5x5
        f.clear();

        var A = BaseElement.of(ElementType.A);
        var B = BaseElement.of(ElementType.B);

        // Ряд: [A][A][B][A][.]
        f.set(0, 0, A);
        f.set(0, 1, A);
        f.set(0, 2, B);
        f.set(0, 3, A);

        var mv = new MoveValidationClassic();
        var mp = new MatchPolicyLine3Plus();

        // Соседний свап (0,2) <-> (0,3): после него будет A A A B -> валидный ход
        assertTrue(mv.isValid(f, new Position(0, 2), new Position(0, 3), mp));

        // Несоседний свап — должен быть невалидным
        assertFalse(mv.isValid(f, new Position(0, 0), new Position(0, 2), mp));
    }
}
