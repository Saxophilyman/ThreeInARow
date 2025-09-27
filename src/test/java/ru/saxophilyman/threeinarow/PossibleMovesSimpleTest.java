package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.element.BaseElement;
import ru.saxophilyman.threeinarow.core.element.ElementFactory;
import ru.saxophilyman.threeinarow.core.element.ElementType;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.rules.MatchPolicyLine3Plus;
import ru.saxophilyman.threeinarow.core.rules.MoveValidationClassic;
import ru.saxophilyman.threeinarow.core.rules.PossibleMovesSimple;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PossibleMovesSimpleTest {
    @Test
    void findsAnyMoveOrNot() {
        var provider = new ElementFactory(new Random(3));
        var f = new BaseField(5, 5, provider); // учитываем минимальный размер 5x5
        f.clear();

        var A = BaseElement.of(ElementType.A);
        var B = BaseElement.of(ElementType.B);

        // Конфигурация с возможным ходом в первой строке:
        // A A B A .
        f.set(0, 0, A);
        f.set(0, 1, A);
        f.set(0, 2, B);
        f.set(0, 3, A);

        var mv = new MoveValidationClassic();
        var mp = new MatchPolicyLine3Plus();
        var pm = new PossibleMovesSimple();

        // Свап (0,2)<->(0,3) даст A A A B → ход существует
        assertTrue(pm.anyMoveExists(f, mv, mp));


        // Теперь поле без возможных ходов: разреженное размещение без соседних непустых клеток
        f.clear();
        f.set(0, 0, A);
        f.set(2, 2, B);
        f.set(4, 4, A);
        // нет ни одной пары соседних непустых клеток → валидного свапа быть не может
        assertFalse(pm.anyMoveExists(f, mv, mp));
    }
}
