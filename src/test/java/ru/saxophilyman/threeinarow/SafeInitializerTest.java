package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.SafeInitializer;
import ru.saxophilyman.threeinarow.core.element.ElementFactory;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.rules.MatchPolicyLine3Plus;
import ru.saxophilyman.threeinarow.core.rules.MoveValidationClassic;
import ru.saxophilyman.threeinarow.core.rules.PossibleMovesSimple;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SafeInitializerTest {
    @Test
    void noInitialMatchesAndHasMove() {
        var provider = new ElementFactory(new Random(1));
        var field = new BaseField(8,8, provider);
        var match = new MatchPolicyLine3Plus();
        var mv    = new MoveValidationClassic();
        var pm    = new PossibleMovesSimple();

        var init  = new SafeInitializer(provider, match, mv, pm);
        init.fill(field);

        assertTrue(match.findMatches(field).isEmpty(), "должно не быть стартовых матчей");
        assertTrue(pm.anyMoveExists(field, mv, match), "должен существовать хотя бы один ход");
    }
}
