package ru.saxophilyman.threeinarow.core.rules;

import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;

//правила допустимости хода, можно гибко настраивать
public interface MoveValidationStrategy {
    boolean isValid(CommonField field, Position a, Position b, MatchPolicyStrategy matchPolicy);
}
