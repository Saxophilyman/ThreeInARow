package ru.saxophilyman.threeinarow.core.rules;

import ru.saxophilyman.threeinarow.core.field.CommonField;

//идёт как паттерн Стратегия
public interface MatchPolicyStrategy {
    MatchResult findMatches(CommonField field);
}
