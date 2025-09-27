package ru.saxophilyman.threeinarow.core.element;

import java.util.Random;

public final class ElementFactory implements ElementProvider {
    //поля
    private final Random rnd;
    private static final ElementType[] BASIC = {
            ElementType.A, ElementType.B, ElementType.C, ElementType.D, ElementType.E
    };

    //конструкторы
    public ElementFactory() {
        this(new Random());
    }

    public ElementFactory(Random rnd) {
        if (rnd == null) throw new IllegalArgumentException("Random must not be null");
        this.rnd = rnd;
    }

    @Override
    public CommonElement randomBasic() {
        return BaseElement.of(BASIC[rnd.nextInt(BASIC.length)]);
    }

    // оставляем дефолт
    @Override
    public CommonElement createBonus() {
        return new BonusElement(BonusKind.STRIPE_H);
    }

    @Override
    public CommonElement createStripeH() {
        return new BonusElement(BonusKind.STRIPE_H);
    }

    @Override
    public CommonElement createStripeV() {
        return new BonusElement(BonusKind.STRIPE_V);
    }
}
