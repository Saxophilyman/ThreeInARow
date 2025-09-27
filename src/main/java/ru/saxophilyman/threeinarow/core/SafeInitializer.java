package ru.saxophilyman.threeinarow.core;

import ru.saxophilyman.threeinarow.core.element.*;
import ru.saxophilyman.threeinarow.core.field.*;
import ru.saxophilyman.threeinarow.core.rules.*;


import java.util.Objects;

public class SafeInitializer {
    /**
     * Заполняет поле случайными элементами так, чтобы на старте не было матчей, и существовал как минимум один ход.
     */
    private final ElementProvider provider;
    private final MatchPolicyStrategy match;
    private final MoveValidationStrategy validator;
    private final PossibleMovesStrategy possible;

    // Макс. попыток на клетку/инициализацию, чтобы никогда не зависнуть.
    private static final int MAX_RETRIES_PER_CELL = 20;
    private static final int MAX_RETRIES_WHOLE = 200;

    public SafeInitializer(ElementProvider provider,
                           MatchPolicyStrategy match,
                           MoveValidationStrategy validator,
                           PossibleMovesStrategy possible) {
        this.provider = Objects.requireNonNull(provider);
        this.match = Objects.requireNonNull(match);
        this.validator = Objects.requireNonNull(validator);
        this.possible = Objects.requireNonNull(possible);
    }

    /**
     * Полный безопасный инит. Поле предварительно очищается.
     */
    public void fill(CommonField f) {
        int wholeTries = 0;
        while (true) {
            wholeTries++;
            // 1) локально заполняем без немедленных матчей
            clear(f);
            for (int r = 0; r < f.rows(); r++) {
                for (int c = 0; c < f.columns(); c++) {
                    placeCellAvoidingImmediateMatch(f, r, c);
                }
            }
            // 2) если совпадений нет (ок) и существует ход — готово
            var mr = match.findMatches(f);
            boolean okMatches = mr.isEmpty();
            boolean okMoves = possible.anyMoveExists(
                    f,
                    validator,
                    match
            );
            if (okMatches && okMoves) return;

            if (wholeTries >= MAX_RETRIES_WHOLE) {
                // как fallback: выходим даже если поле не идеально
                return;
            }
        }
    }

    private void placeCellAvoidingImmediateMatch(CommonField f, int r, int c) {
        Position p = new Position(r, c);
        int tries = 0;
        while (true) {
            tries++;
            CommonElement e = provider.randomBasic();
            f.set(p, e);
            if (!createsImmediateLine(f, r, c)) return;
            if (tries >= MAX_RETRIES_PER_CELL) return; // сдаёмся, оставляем как есть
        }
    }

    /**
     * Проверяем только «локальные» риски: горизонтальные/вертикальные тройки, в которые входит (r,c).
     */
    private boolean createsImmediateLine(CommonField f, int r, int c) {
        var cur = f.get(r, c);
        if (cur == null) return false;

        // горизонталь: считаем подряд одинаковых слева и справа
        int left = 0;
        for (int k = c - 1; k >= 0; k--) {
            var e = f.get(r, k);
            if (e != null && e.isCorrectElement(cur)) left++;
            else break;
        }
        int right = 0;
        for (int k = c + 1; k < f.columns(); k++) {
            var e = f.get(r, k);
            if (e != null && e.isCorrectElement(cur)) right++;
            else break;
        }
        if (1 + left + right >= 3) return true;

        // вертикаль: вверх/вниз
        int up = 0;
        for (int k = r - 1; k >= 0; k--) {
            var e = f.get(k, c);
            if (e != null && e.isCorrectElement(cur)) up++;
            else break;
        }
        int down = 0;
        for (int k = r + 1; k < f.rows(); k++) {
            var e = f.get(k, c);
            if (e != null && e.isCorrectElement(cur)) down++;
            else break;
        }
        return 1 + up + down >= 3;
    }

    private void clear(CommonField f) {
        for (int r = 0; r < f.rows(); r++)
            for (int c = 0; c < f.columns(); c++)
                f.set(r, c, null);
    }
}
