package ru.saxophilyman.threeinarow;

import org.junit.jupiter.api.Test;
import ru.saxophilyman.threeinarow.core.element.ElementFactory;
import ru.saxophilyman.threeinarow.core.element.ElementProvider;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.field.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FillRandomSmokeTest {
    @Test
    void fillRandomFillsAllCells() {
        ElementProvider provider = new ElementFactory();
        BaseField f = new BaseField(8, 8, provider);

        f.fillRandom();

        for (int r = 0; r < f.rows(); r++) {
            for (int c = 0; c < f.columns(); c++) {
                assertNotNull(f.get(new Position(r, c)), "Cell is null at ("+r+","+c+")");
            }
        }
    }
}
