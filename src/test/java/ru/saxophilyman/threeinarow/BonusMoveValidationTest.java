package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.element.*;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.field.Position;
import ru.saxophilyman.threeinarow.core.rules.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BonusMoveValidationTest {
    @Test
    void moveWithBonusIsAlwaysValid() {
        ElementProvider provider = new ElementFactory(new Random(3));
        BaseField f = new BaseField(5, 5, provider);

        // Поставим бонус и обычный элемент по соседству
        var bonus = new BonusElement(BonusKind.STRIPE_H);
        var A = BaseElement.of(ElementType.A);

        f.clear();
        f.set(2, 2, bonus);
        f.set(2, 3, A);

        MoveValidationStrategy mv = new MoveValidationClassic();
        MatchPolicyStrategy mp = new MatchPolicyLine3Plus();

        // Свап бонуса с соседом должен считаться валидным (даже если не образует обычного матча)
        assertTrue(mv.isValid(f, new Position(2,2), new Position(2,3), mp));
    }
}