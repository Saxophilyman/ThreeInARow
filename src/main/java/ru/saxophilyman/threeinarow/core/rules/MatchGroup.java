package ru.saxophilyman.threeinarow.core.rules;

import ru.saxophilyman.threeinarow.core.field.Position;

import java.util.Set;

public record MatchGroup(Set<Position> positions, MatchShape shape){
    public enum MatchShape { LINE_H, LINE_V, OTHER }
    public int size() { return positions.size(); }
}


/**
Разбор класса:
MatchGroup — одна найденная группа совпавших клеток (например, горизонтальная линия A A A).
positions — множество координат клеток, которые входят в эту группу.
shape — тип группы: горизонтальная линия (LINE_H), вертикальная (LINE_V) или «другая» форма (OTHER) — задел на будущее
size() — сколько клеток в группе
Set<Position> - для исключения, где одна клетка может попасть в две группы (H и V)
*/