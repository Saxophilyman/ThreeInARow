package ru.saxophilyman.threeinarow.core.history;

import ru.saxophilyman.threeinarow.core.rules.MatchResult;

public record CascadeWave(int gainedPoints, MatchResult result) { }
// Одна волна каскада: сколько очков дала и какие именно совпадения были
// При необходимости будущей статистики