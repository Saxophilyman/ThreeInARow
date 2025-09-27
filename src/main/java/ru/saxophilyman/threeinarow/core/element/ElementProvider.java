package ru.saxophilyman.threeinarow.core.element;

public interface ElementProvider {
    CommonElement randomBasic();
    CommonElement createBonus();
    CommonElement createStripeH();
    CommonElement createStripeV();
}
