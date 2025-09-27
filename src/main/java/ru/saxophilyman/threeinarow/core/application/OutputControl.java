package ru.saxophilyman.threeinarow.core.application;

import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.history.GameStats;

public final class OutputControl {

    public void printState(CommonField field, int score) {
        System.out.print(FieldRenderer.render(field));
        System.out.println("Score: " + score);
    }

    public void printHelp() {
        System.out.println("Команды и форматы:");
        System.out.println("  • A2 B3         — ход (буква = колонка, число = строка; A1 — верхний левый)");
        System.out.println("  • r1 c1 r2 c2   — ход числовым форматом (пример: 1 2 1 3)");
        System.out.println("  • r | restart   — новая партия");
        System.out.println("  • q | quit      — выход");
        System.out.println("  • h | help | ?  — подсказка");
        System.out.println();
    }

    public void printEnd(GameStats stats) {
        System.out.println("\n=== Итоги игры ===");
        System.out.println("Очки: " + stats.totalScore());
        System.out.println("Ходы: " + stats.movesCount());
    }

    public void printError(String msg) {
        System.out.println("Ошибка: " + msg);
    }

    public void printNoMoves() {
        System.out.println("Ходов нет. Игра окончена.");
    }

    public void printInvalidMove() {
        System.out.println("Недопустимый ход.\n");
    }

    public void prompt() {
        System.out.print("Ход (A2 B3) или (r1 c1 r2 c2), q — выход: ");
    }
}
