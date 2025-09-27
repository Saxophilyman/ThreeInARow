package ru.saxophilyman.threeinarow.core.rules;

import ru.saxophilyman.threeinarow.core.field.Position;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchResult {
    private final List<MatchGroup> groups;
    private Set<Position> cachedUnion;

    public MatchResult(List<MatchGroup> groups) {
        this.groups = List.copyOf(groups);
    }

    public List<MatchGroup> groups() { return groups; }

    /** Все уникальные клетки из всех групп. */
    public Set<Position> union() {
        if (cachedUnion == null) {
            Set<Position> u = new HashSet<>();
            for (var g : groups) u.addAll(g.positions());
            cachedUnion = Collections.unmodifiableSet(u);
        }
        return cachedUnion;
    }

    public boolean isEmpty() { return groups.isEmpty(); }
    public int totalCells() { return union().size(); }
}

/**
Разбор класса:
List<MatchGroup> groups - список найденных групп (MatchGroup)
Для:
начисления очков по длине каждой линии;
порождения бонусов «из четвёрок/пятёрок» с учётом направления (LINE_H/LINE_V);

union() — множество всех уникальных клеток, входящих хоть в одну группу.
Для удаления клеток перед каскадом (все разом);

private Set<Position> cachedUnion; — чтобы не пересчитывать каждый раз(потребуется для начисления очков и удаления клеток и т.д.).
this.groups = List.copyOf(groups); — защитная копия: не работаем с реальными элементами.
groups() — геттер (вернёт ту самую иммутабельную копию).

union() - совет от AI:
если кеш пуст — строим HashSet из позиций всех групп;
оборачиваем в Collections.unmodifiableSet(...) (чтобы никто не поменял);
сохраняем в cachedUnion и возвращаем.
*/