package ru.saxophilyman.threeinarow.core.rules;

import ru.saxophilyman.threeinarow.core.field.CommonField;
import ru.saxophilyman.threeinarow.core.field.Position;

import java.util.Optional;

public class PossibleMovesSimple implements PossibleMovesStrategy {
    @Override
    public boolean anyMoveExists(CommonField field, MoveValidationStrategy moveValidation, MatchPolicyStrategy matchPolicy) {
        return findAny(field, moveValidation, matchPolicy).isPresent();
    }

    @Override
    public Optional<Swap> findAny(CommonField field, MoveValidationStrategy moveValidation, MatchPolicyStrategy matchPolicy) {
        for (int r = 0; r < field.rows(); r++) {
            for (int c = 0; c < field.columns(); c++) {
                Position p = new Position(r, c);
                Position right = new Position(r, c + 1);
                Position down = new Position(r + 1, c);
                if (field.inBounds(right) && moveValidation.isValid(field, p, right, matchPolicy))
                    return Optional.of(new Swap(p, right));
                if (field.inBounds(down) && moveValidation.isValid(field, p, down, matchPolicy))
                    return Optional.of(new Swap(p, down));
            }
        }
        return Optional.empty();
    }
}
