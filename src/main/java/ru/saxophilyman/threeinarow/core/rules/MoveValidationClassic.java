package ru.saxophilyman.threeinarow.core.rules;


import ru.saxophilyman.threeinarow.core.element.ElementType;
import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;

public class MoveValidationClassic implements MoveValidationStrategy {
    @Override
    public boolean isValid(CommonField field, Position a, Position b, MatchPolicyStrategy matchPolicy) {
        if (!field.inBounds(a) || !field.inBounds(b)) return false;

        //манхэттенское расстояние, рекомендуемый расчёт
        int dr = Math.abs(a.row() - b.row());
        int dc = Math.abs(a.column() - b.column());
        if (dr + dc != 1) return false;


        var ea = field.get(a);
        var eb = field.get(b);
        // не позволяем двигать пустые клетки
        if (ea == null || eb == null) return false;

        // если участвует бонус — ход валиден (активация произойдёт после свапа)
        if (ea.type() == ElementType.BONUS || eb.type() == ElementType.BONUS) {
            return true;
        }

        // смотрим, появились ли совпадения, если нет - откат
        field.swap(a, b);
        boolean ok;
        try {
            ok = !matchPolicy.findMatches(field).isEmpty();
        } finally {
            field.swap(a, b); // откат даже при исключении
        }
        return ok;
    }
}
/**
 * Разбор класса:
 * смотрим можно ли после перестановки элементов собрать группу три в ряд
 * стандартный вариант - горизонталь/вертикаль
 */