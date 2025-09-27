package ru.saxophilyman.threeinarow.core.rules;

import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;

import java.util.Optional;

public interface  PossibleMovesStrategy {
    boolean anyMoveExists(CommonField field, MoveValidationStrategy moveValidation, MatchPolicyStrategy matchPolicy);
    Optional<Swap> findAny(CommonField field, MoveValidationStrategy moveValidation, MatchPolicyStrategy matchPolicy);
    record Swap(Position a, Position b) {}
}
/**
 * Разбор класса:
 * оставляю по аналогии с другими стратегиями
 * излишняя гибкость здесь необязательна, но и не критично
 */