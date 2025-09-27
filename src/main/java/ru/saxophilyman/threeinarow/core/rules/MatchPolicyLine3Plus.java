package ru.saxophilyman.threeinarow.core.rules;

import ru.saxophilyman.threeinarow.core.element.CommonElement;
import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchPolicyLine3Plus implements MatchPolicyStrategy {
    private final int minRun;

    //конструктор по умолчанию, минимум должно быть 3 одинаковых элемента
    public MatchPolicyLine3Plus() {
        this(3);
    }

    public MatchPolicyLine3Plus(int minRun) {
        if (minRun < 2) throw new IllegalArgumentException("minRun must be >= 2");
        this.minRun = minRun;
    }

    @Override
    public MatchResult findMatches(CommonField field) {
        List<MatchGroup> groups = new ArrayList<>();
        //нужно ли объяснять логику прохода?
        //горизонталь
        for (int i = 0; i < field.rows(); i++) {
            int runStart = 0;
            CommonElement prev = field.get(i, 0);
            for (int j = 1; j <= field.columns(); j++) {
                CommonElement cur = (j < field.columns()) ? field.get(i, j) : null;
                boolean same = (cur != null && prev != null && prev.isCorrectElement(cur));
                if (!same) {
                    int len = j - runStart;
                    if (len >= minRun) {
                        Set<Position> pos = new HashSet<>(len * 4 / 3 + 1);
                        for (int k = runStart; k < j; k++) pos.add(new Position(i, k));
                        groups.add(new MatchGroup(pos, MatchGroup.MatchShape.LINE_H));
                    }
                    runStart = j;
                }
                prev = cur;
            }
        }
        //вертикаль
        for (int c = 0; c < field.columns(); c++) {
            int runStart = 0;
            CommonElement prev = field.get(0, c);
            for (int r = 1; r <= field.rows(); r++) {
                CommonElement cur = (r < field.rows()) ? field.get(r, c) : null;
                boolean same = (cur != null && prev != null && prev.isCorrectElement(cur));
                if (!same) {
                    int len = r - runStart;
                    if (len >= minRun) {
                        Set<Position> pos = new HashSet<>(len * 4 / 3 + 1);
                        for (int k = runStart; k < r; k++) pos.add(new Position(k, c));
                        groups.add(new MatchGroup(pos, MatchGroup.MatchShape.LINE_V));
                    }
                    runStart = r;
                }
                prev = cur;
            }
        }
        return new MatchResult(groups);
    }
}
/**
 * Разбор класса:
 * MatchPolicyLine3Plus — правило поиска совпадений >3,
 * пробегает поле, находит все горизонтальные и вертикальные,
 * группирует их в MatchGroup и возвращает в MatchResult
 */