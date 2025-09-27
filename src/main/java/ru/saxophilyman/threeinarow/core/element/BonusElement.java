package ru.saxophilyman.threeinarow.core.element;

public final class BonusElement implements CommonElement {
    private final BonusKind kind;

    public BonusElement(BonusKind kind) {
        this.kind = kind;
    }

    public BonusKind kind() { return kind; }

    @Override
    public ElementType type() { return ElementType.BONUS; }

    @Override
    public boolean isCorrectElement(CommonElement other) {
        // бонусы не участвуют в обычных линиях
        return false;
    }
}
