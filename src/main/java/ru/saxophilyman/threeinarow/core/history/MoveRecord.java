package ru.saxophilyman.threeinarow.core.history;

import ru.saxophilyman.threeinarow.core.field.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//при необходимости можно расширить другими данными
public class MoveRecord {
    private final int numberOfMove;
    private final Position a, b;
    private final List<CascadeWave> waves = new ArrayList<>();
    private int totalGained; //набранные очки в этом ходу

    public MoveRecord(int index, Position a, Position b) {
        this.numberOfMove = index;
        this.a = a;
        this.b = b;
    }

    public void addWave(CascadeWave w) {
        waves.add(w);
        totalGained += w.gainedPoints();
    }

    public int numberOfMove() {
        return numberOfMove;
    }

    public Position a() {
        return a;
    }

    public Position b() {
        return b;
    }

    public List<CascadeWave> waves() {
        return Collections.unmodifiableList(waves);
    }

    public int totalGained() {
        return totalGained;
    }
}
