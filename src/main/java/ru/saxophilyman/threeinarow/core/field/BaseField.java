package ru.saxophilyman.threeinarow.core.field;

import ru.saxophilyman.threeinarow.core.element.CommonElement;
import ru.saxophilyman.threeinarow.core.element.ElementProvider;

import java.util.Objects;

/**
 * Простейшая реализация прямоугольного поля (по умолчанию будем использовать 8×8).
 * Без лишнего
 */
public class BaseField implements CommonField {
    private final int minSize = 5;
    //Поля количество колонок и столбцов
    private final int rows;
    private final int columns;
    //Массив для сетки элементов(состоит из наших элементов)
    private final CommonElement[][] field;
    //для генерации базовых элементов
    private final ElementProvider elementProvider;

    //конструктор
    public BaseField(int rows, int columns, ElementProvider elementProvider) {
        //проверка на минимальные значения (не используем "магические" числа)
        if (rows < minSize || columns < minSize) {
            throw new IllegalArgumentException("rows and columns must be >=5 ");
        }
        this.rows = rows;
        this.columns = columns;
        this.field = new CommonElement[rows][columns];
        //защита от null
        this.elementProvider = Objects.requireNonNull(elementProvider, "elementProvider");
    }

    //переопределяем методы из интерфейса
    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int columns() {
        return columns;
    }

    @Override
    public CommonElement get(Position p) {
        return field[p.row()][p.column()];
    }

    @Override
    public void set(Position p, CommonElement e) {
        field[p.row()][p.column()] = e;
    }

    //Остальные методы оставляем с базовой реализацией по интерфейсу

    //специальные методы для поля

    //очистить поле (просто проход циклом)
    public void clear() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                field[i][j] = null;
            }
        }
    }

    //рандомное заполнение базовыми элементами
    public void fillRandom() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                set(i, j, elementProvider.randomBasic());
            }
        }
    }
}
