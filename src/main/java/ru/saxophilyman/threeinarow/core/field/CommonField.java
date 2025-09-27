package ru.saxophilyman.threeinarow.core.field;


import ru.saxophilyman.threeinarow.core.element.CommonElement;

public interface CommonField {
    // размеры поля
    int rows();
    int columns();

    //доступ к клеткам из позиции
    CommonElement get(Position p); //может вернуть null

    void set(Position p, CommonElement e); //можно установить null

    //сахар, чтобы не писать каждый раз new Position
    default CommonElement get(int x, int y) {
        return get(new Position(x, y));
    }

    default void set(int x, int y, CommonElement e) {
        set(new Position(x, y), e);
    }

    //Проверка выхода за границу
    //Проверка «в пределах поля» по Position как перегрузка
    default boolean inBounds(Position p) {
        return inBounds(p.row(), p.column());
    }
    //непосредственная проверка
    default boolean inBounds(int r, int c) {
        return r >= 0 && r < rows() && c >= 0 && c < columns();
    }

    // --- Базовая операция перестановки двух клеток (совет AI) ---
    /**
     * Поменять местами элементы в клетках a и b.
     * Перед перестановкой проверяет, что обе позиции внутри поля.
     * Если любая из позиций вне поля — бросает IndexOutOfBoundsException (явная ошибка использования API).
     *
     * Важно: метод не делает «валидации хода по правилам игры» (это задача MoveValidation).
     * Здесь только безопасная перестановка содержимого двух ячеек.
     */
    default void swap(Position a, Position b) {
        if (!inBounds(a) || !inBounds(b)) throw new IndexOutOfBoundsException();
        CommonElement tmp = get(a);
        set(a, get(b));
        set(b, tmp);
    }

}
