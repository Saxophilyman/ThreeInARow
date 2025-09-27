package ru.saxophilyman.threeinarow.core.application;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.Scoring;
import ru.saxophilyman.threeinarow.core.element.*;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;
import ru.saxophilyman.threeinarow.core.rules.MatchGroup;
import ru.saxophilyman.threeinarow.core.rules.MatchPolicyLine3Plus;
import ru.saxophilyman.threeinarow.core.rules.MatchPolicyStrategy;
import ru.saxophilyman.threeinarow.core.rules.MatchResult;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BonusBirthSingleWaveTest {

    /** Провайдер без рандома: B, C, B, C, ...; бонусы как в проде. */
    static final class TestProvider implements ElementProvider {
        private int i = 0;
        @Override public CommonElement randomBasic() {
            ElementType t = (i++ % 2 == 0) ? ElementType.B : ElementType.C;
            return BaseElement.of(t);
        }
        @Override public CommonElement createBonus()   { return new BonusElement(BonusKind.STRIPE_H); }
        @Override public CommonElement createStripeH() { return new BonusElement(BonusKind.STRIPE_H); }
        @Override public CommonElement createStripeV() { return new BonusElement(BonusKind.STRIPE_V); }
    }

    private void fillCheckerboard(BaseField f, CommonElement first, CommonElement second) {
        for (int r = 0; r < f.rows(); r++) {
            for (int c = 0; c < f.columns(); c++) {
                f.set(r, c, ((r + c) % 2 == 0) ? first : second);
            }
        }
    }

    private static class BonusScan {
        int count = 0;
        BonusElement first = null;
    }
    private BonusScan scanBonuses(CommonField f) {
        BonusScan s = new BonusScan();
        for (int r = 0; r < f.rows(); r++) {
            for (int c = 0; c < f.columns(); c++) {
                var e = f.get(new Position(r, c));
                if (e instanceof BonusElement b) {
                    s.count++;
                    if (s.first == null) s.first = b;
                }
            }
        }
        return s;
    }

    /** Выполняем РОВНО одну волну: находим группы, создаём бонусы 4+, чистим, обваливаем, доливаем, считаем очки. */
    private int resolveSingleWave(CommonField f, ElementProvider provider, MatchPolicyStrategy match) {
        MatchResult mr = match.findMatches(f);
        assertFalse(mr.isEmpty(), "в этой позиции ожидаем хотя бы одну группу");

        // 1) рождение бонусов (pivot) для групп >=4
        Set<Position> keep = new HashSet<>();
        for (MatchGroup g : mr.groups()) {
            if (g.size() >= 4) {
                Position pivot = g.positions().iterator().next(); // достаточно предсказуемости для теста
                switch (g.shape()) {
                    case LINE_H -> f.set(pivot, provider.createStripeH());
                    case LINE_V -> f.set(pivot, provider.createStripeV());
                    default     -> f.set(pivot, provider.createStripeH());
                }
                keep.add(pivot);
            }
        }

        // 2) очки за эту волну
        int gained = new Scoring().scoreFor(mr);

        // 3) очистить все клетки из union(), кроме pivot'ов с бонусами
        for (Position p : mr.union()) {
            if (!keep.contains(p)) f.set(p, null);
        }

        // 4) гравитация
        collapseDown(f);

        // 5) долив детерминированным провайдером
        refillFromTop(f, provider);

        return gained;
    }

    private void collapseDown(CommonField f) {
        for (int c = 0; c < f.columns(); c++) {
            int write = f.rows() - 1;
            for (int r = f.rows() - 1; r >= 0; r--) {
                var e = f.get(r, c);
                if (e != null) {
                    if (r != write) { f.set(write, c, e); f.set(r, c, null); }
                    write--;
                }
            }
        }
    }

    private void refillFromTop(CommonField f, ElementProvider provider) {
        for (int c = 0; c < f.columns(); c++) {
            for (int r = 0; r < f.rows(); r++) {
                if (f.get(r, c) == null) {
                    f.set(r, c, provider.randomBasic());
                }
            }
        }
    }

    @Test
    void birthFromHorizontalFour_createsStripeH_exactlyOnce() {
        ElementProvider provider = new TestProvider();
        BaseField f = new BaseField(5, 5, provider);

        var eA = BaseElement.of(ElementType.A);
        var eB = BaseElement.of(ElementType.B);
        var eC = BaseElement.of(ElementType.C);

        // безматчевый старт: шахматка B/C
        fillCheckerboard(f, eB, eC);
        // горизонтальная четвёрка A
        f.set(2,0, eA); f.set(2,1, eA); f.set(2,2, eA); f.set(2,3, eA);

        int gained = resolveSingleWave(f, provider, new MatchPolicyLine3Plus());
        assertEquals(45, gained, "ожидаем ровно 45 очков за одну четвёрку");

        BonusScan scan = scanBonuses(f);
        assertEquals(1, scan.count, "должен появиться ровно один бонус");
        assertNotNull(scan.first);
        assertEquals(BonusKind.STRIPE_H, scan.first.kind(), "тип бонуса — горизонтальная полоска");
    }

    @Test
    void birthFromVerticalFour_createsStripeV_exactlyOnce() {
        ElementProvider provider = new TestProvider();
        BaseField f = new BaseField(5, 5, provider);

        var eA = BaseElement.of(ElementType.A);
        var eB = BaseElement.of(ElementType.B);
        var eC = BaseElement.of(ElementType.C);

        // безматчевый старт: шахматка B/C
        fillCheckerboard(f, eB, eC);
        // вертикальная четвёрка A
        f.set(0,1, eA); f.set(1,1, eA); f.set(2,1, eA); f.set(3,1, eA);

        int gained = resolveSingleWave(f, provider, new MatchPolicyLine3Plus());
        assertEquals(45, gained, "ожидаем ровно 45 очков за одну четвёрку");

        BonusScan scan = scanBonuses(f);
        assertEquals(1, scan.count, "должен появиться ровно один бонус");
        assertNotNull(scan.first);
        assertEquals(BonusKind.STRIPE_V, scan.first.kind(), "тип бонуса — вертикальная полоска");
    }
}
