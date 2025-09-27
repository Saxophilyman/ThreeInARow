package ru.saxophilyman.threeinarow.core.history;

public class StatsPrinter {
    private StatsPrinter() {}
    public static String summary(GameStats stats) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Итоги игры ===\n");
        sb.append("Очки: ").append(stats.totalScore()).append('\n');
        sb.append("Ходы: ").append(stats.movesCount()).append('\n');
        sb.append('\n');
        for (var m : stats.moves()) {
            sb.append("Ход #").append(m.numberOfMove()).append(": +").append(m.totalGained())
                    .append("  (").append(m.a().row()).append(',').append(m.a().column())
                    .append(" <-> ").append(m.b().row()).append(',').append(m.b().column()).append(")\n");
        }
        return sb.toString();
    }
}
