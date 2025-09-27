package ru.saxophilyman.threeinarow.core.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Общий счёт и вся история ходов
public class GameStats {
    private final List<MoveRecord> moves = new ArrayList<>();
    private int totalScore;

    public void addMove(MoveRecord move) {
        moves.add(move);
        totalScore += move.totalGained();
    }

    public int totalScore() {
        return totalScore;
    }

    public int movesCount() {
        return moves.size();
    }

    public List<MoveRecord> moves() {
        return Collections.unmodifiableList(moves);
    }
}