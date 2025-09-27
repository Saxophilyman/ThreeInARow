package ru.saxophilyman.threeinarow.core.element;

public interface CommonElement {
    ElementType type(); //все виды элементов. У нас сейчас стандартные A-E и bonus

    // соответствует ли тип типу (если будут другие типы элементов)
    default boolean isCorrectElement(CommonElement other) {
        return other != null && this.type() == other.type();
    }
}
