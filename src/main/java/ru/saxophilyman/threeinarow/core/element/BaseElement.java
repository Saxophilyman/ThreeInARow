package ru.saxophilyman.threeinarow.core.element;

public record BaseElement(ElementType elementType) implements CommonElement{
    public BaseElement {
        if (elementType == null) {
            throw new IllegalArgumentException("elementType must not be null");
        }
        // Страховка: BaseElement должен быть только A..E (не BONUS)
        if (elementType == ElementType.BONUS) {
            throw new IllegalArgumentException("BaseElement cannot have type BONUS");
        }
    }

    @Override
    public ElementType type() { return elementType; }

    /** Утилита: удобнее писать BaseElement.of(A) */
    public static BaseElement of(ElementType t) { return new BaseElement(t); }
}
