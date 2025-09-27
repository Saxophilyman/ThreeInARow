package ru.saxophilyman.threeinarow.core.application;

import ru.saxophilyman.threeinarow.core.field.Position;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CellAddress {
    private CellAddress() {
    }

    // буквы + цифры, регистр неважен
    private static final Pattern CELL = Pattern.compile("^([a-zA-Z]+)(\\d+)$");

    /**
     * Нормализует строку: lower, заменяет ,;:- на пробел, схлопывает пробелы.
     */
    static String normalize(String s) {
        if (s == null) return "";
        s = s.trim().toLowerCase();
        s = s.replaceAll("[,;:-]", " ");
        return s.replaceAll("\\s+", " ");
    }

    /**
     * Парсит "a2", "H8", "aa10" в 0-based Position. Возвращает null при ошибке.
     */
    static Position parseCell(String token) {
        if (token == null || token.isEmpty()) return null;
        Matcher m = CELL.matcher(token.trim());
        if (!m.matches()) return null;

        String letters = m.group(1);
        String digits = m.group(2);

        Integer col = lettersToIndex(letters);
        if (col == null) return null;

        try {
            int row1 = Integer.parseInt(digits);
            if (row1 <= 0) return null;
            return new Position(row1 - 1, col);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * A→0, B→1, ..., Z→25, AA→26, AB→27, ...
     */
    private static Integer lettersToIndex(String letters) {
        int col = 0;
        for (int i = 0; i < letters.length(); i++) {
            char ch = Character.toUpperCase(letters.charAt(i));
            if (ch < 'A' || ch > 'Z') return null;
            int val = ch - 'A' + 1;   // A=1..Z=26
            col = col * 26 + val;
        }
        return col - 1;               // 0-based
    }
}
