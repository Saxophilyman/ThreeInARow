package ru.saxophilyman.threeinarow.core;

import ru.saxophilyman.threeinarow.core.rules.MatchGroup;
import ru.saxophilyman.threeinarow.core.rules.MatchResult;

public class Scoring {
    private final int baseScore;
    private final int bonus4;
    private final int bonus5plus;

    public Scoring() { this(10, 5, 15); }
    public Scoring(int basePerCell, int bonus4, int bonus5plus) {
        this.baseScore = basePerCell;
        this.bonus4 = bonus4;
        this.bonus5plus = bonus5plus;
    }

    public int scoreFor(MatchResult result) {
        int sum = 0;
        for (MatchGroup g : result.groups()) {
            int len = g.size();
            sum += len * baseScore;
            if (len == 4) sum += bonus4;
            else if (len >= 5) sum += bonus5plus;
        }
        return sum;
    }
}
