package ru.saxophilyman.threeinarow.core;

import ru.saxophilyman.threeinarow.core.element.ElementProvider;
import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;
import ru.saxophilyman.threeinarow.core.rules.MatchPolicyStrategy;
import ru.saxophilyman.threeinarow.core.rules.MatchResult;

import java.util.HashSet;
import java.util.Set;

public class CascadeEngine {
    private final MatchPolicyStrategy matchPolicy;
    private final ElementProvider provider;
    private final Scoring scoring;

    public CascadeEngine(MatchPolicyStrategy matchPolicy, ElementProvider provider, Scoring scoring) {
        this.matchPolicy = matchPolicy;
        this.provider = provider;
        this.scoring = scoring;
    }

    /**
     * Выполняет полный каскад до стабилизации; возвращает суммарные очки за каскад.
     */
    public int cascade(CommonField field) {
        int total = 0;
        while (true) {
            MatchResult mr = matchPolicy.findMatches(field);
            if (mr.isEmpty()) break;

            total += scoring.scoreFor(mr);

            // --- рождение бонусов для групп >= 4 ---
            Set<Position> keep = new HashSet<>(); // позиции, которые НЕ нужно очищать (там поставим бонус)
            mr.groups().forEach(g -> {
                if (g.size() >= 4) {
                    // возьмём первую позицию группы как "pivot"
                    var it = g.positions().iterator();
                    if (it.hasNext()) {
                        Position pivot = it.next();
                        // создаём полоску по направлению группы
                        switch (g.shape()) {
                            case LINE_H -> field.set(pivot, provider.createStripeH());
                            case LINE_V -> field.set(pivot, provider.createStripeV());
                            default     -> field.set(pivot, provider.createStripeH());
                        }
                        keep.add(pivot);
                    }
                }
            });
            // 1) очистить
            for (var p : mr.union()) {
                if (!keep.contains(p)) field.set(p, null);
            }

            // 2) обвалить вниз по каждому столбцу
            collapseDown(field);

            // 3) долить сверху базовыми элементами
            refillFromTop(field);
        }
        return total;
    }

    private void collapseDown(CommonField f) {
        for (int c = 0; c < f.columns(); c++) {
            int write = f.rows() - 1;
            for (int r = f.rows() - 1; r >= 0; r--) {
                var e = f.get(r, c);
                if (e != null) {
                    if (r != write) {
                        f.set(write, c, e);
                        f.set(r, c, null);
                    }
                    write--;
                }
            }
        }
    }

    private void refillFromTop(CommonField f) {
        for (int c = 0; c < f.columns(); c++) {
            for (int r = 0; r < f.rows(); r++) {
                if (f.get(r, c) == null) {
                    f.set(r, c, provider.randomBasic());
                }
            }
        }
    }
    public void settle(CommonField field) {
        // обвалить и долить без поиска матчей и начисления очков
        collapseDown(field);
        refillFromTop(field);
    }
}
/**
 * Разбор класса:
 * последовательный алгоритм при перестановке элементов
 * Что делает:
 * Ищет совпадения: matchPolicy.findMatches(field)
 * Получает MatchResult со списком групп и объединением всех клеток, которые нужно убрать.
 * Проверяется окончание: если пусто — выходим из цикла.
 * Начисляем очки: total += scoring.scoreFor(mr) *
 * Очищаем клетки:
 * Проходим по mr.union() и ставим null — означает пустую клетку.
 * Обваливаем элементы вниз: collapseDown(field)
 * Генерируем новые элементы сверху: refillFromTop(field)
 * Проходим столбец сверху вниз и в каждую пустую клетку кладём базовый элемент
 * После цикл повторяется: если сверху сложилась новая тройка — тогда волна повторится, очки добавятся, и так до стабилизации.
 *
 * collapseDown - общий алгоритм:
 * Идём по всем столбцам
 * Проходим этот столбец снизу вверх
 * Нужны только непустые
 * Если выше есть пустоты, переносим элемент на позицию write и очищаем старую клетку.
 * Следующий найденный непустой элемент должен встать на строку выше относительно низа.
 *
 * refillFromTop
 * Находим пустые клетки и кладём новый базовый элемент
 *
 * можно рассмотреть вариант с заполнением не рандомно, где могут снова возникнуть совпадения
 * а в соответствии с "политикой"
 */