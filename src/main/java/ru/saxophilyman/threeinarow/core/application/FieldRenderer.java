package ru.saxophilyman.threeinarow.core.application;

import ru.saxophilyman.threeinarow.core.element.CommonElement;
import ru.saxophilyman.threeinarow.core.element.ElementType;
import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;

public final class FieldRenderer {
    private FieldRenderer() { }

    public static String render(CommonField f) {
        StringBuilder sb = new StringBuilder();

        // Заголовок: колонки буквами A, B, ..., Z, AA, AB, ...
        sb.append("    ");
        for (int c = 0; c < f.columns(); c++) {
            sb.append(colLabel(c)).append(' ');
        }
        sb.append('\n');
        sb.append("   ").append("-".repeat(f.columns() * 2)).append('\n');

        // Строки: 1-based метки слева
        for (int r = 0; r < f.rows(); r++) {
            sb.append(String.format("%2d| ", r + 1));
            for (int c = 0; c < f.columns(); c++) {
                CommonElement e = f.get(new Position(r, c));
                sb.append(toChar(e)).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Преобразует 0-based индекс колонки в метку: 0→A, 25→Z, 26→AA, 27→AB, ...
    private static String colLabel(int c) {
        StringBuilder s = new StringBuilder();
        int x = c;
        do {
            int rem = x % 26;
            s.insert(0, (char) ('A' + rem));
            x = x / 26 - 1;
        } while (x >= 0);
        return s.toString();
    }

    private static char toChar(CommonElement e) {
        if (e == null) return '.';
        ElementType t = e.type();
        return switch (t) {
            case A -> 'A';
            case B -> 'B';
            case C -> 'C';
            case D -> 'D';
            case E -> 'E';
            case BONUS -> '*';
        };
    }
}
