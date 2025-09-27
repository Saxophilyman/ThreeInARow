package ru.saxophilyman.threeinarow.core.application;

import ru.saxophilyman.threeinarow.core.field.Position;
import ru.saxophilyman.threeinarow.core.application.Command.*;

public final class InputControl {
    public Command parse(String line) {
        String s = CellAddress.normalize(line);

        // унарные команды
        if (isQuit(s))    return new Quit();
        if (isHelp(s))    return new Help();
        if (isRestart(s)) return new Command.Restart();

        String[] parts = s.split(" ");
        if (parts.length == 2) {
            // формат "A2 B3"
            Position a = CellAddress.parseCell(parts[0]); // 1-based → 0-based внутри
            Position b = CellAddress.parseCell(parts[1]);
            return (a != null && b != null)
                    ? new Move(a, b)
                    : new Invalid("ожидаются ячейки вида A2 B3");
        }

        if (parts.length == 4) {
            // формат "r1 c1 r2 c2" — ЧИСЛА 1-based, приводим к 0-based
            try {
                int r1 = Integer.parseInt(parts[0]) - 1;
                int c1 = Integer.parseInt(parts[1]) - 1;
                int r2 = Integer.parseInt(parts[2]) - 1;
                int c2 = Integer.parseInt(parts[3]) - 1;
                if (r1 < 0 || c1 < 0 || r2 < 0 || c2 < 0) {
                    return new Invalid("строки/колонки должны быть ≥ 1 (числовой формат — 1-based)");
                }
                return new Move(new Position(r1, c1), new Position(r2, c2));
            } catch (NumberFormatException e) {
                return new Invalid("координаты должны быть числами (r1 c1 r2 c2) или ячейками вида A2 B3");
            }
        }

        return new Invalid("ожидаю: A2 B3  или  r1 c1 r2 c2  (q — выход, help — помощь)");
    }

    private boolean isQuit(String s) {
        return s.equals("q") || s.equals("quit") || s.equals("exit");
    }

    private boolean isHelp(String s) {
        return s.equals("h") || s.equals("help") || s.equals("?");
    }

    private boolean isRestart(String s) {
        return s.equals("r") || s.equals("restart");
    }
}
