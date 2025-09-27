package ru.saxophilyman.threeinarow.core;

import ru.saxophilyman.threeinarow.core.application.*;
import ru.saxophilyman.threeinarow.core.element.*;
import ru.saxophilyman.threeinarow.core.field.*;
import ru.saxophilyman.threeinarow.core.history.*;
import ru.saxophilyman.threeinarow.core.rules.*;
import ru.saxophilyman.threeinarow.core.application.Command.*;

import java.util.*;

public final class GameRunner {
    private final InputControl in = new InputControl();
    private final OutputControl out = new OutputControl();
    private CascadeEngine cascade;
    public void run() {
        ElementProvider provider = new ElementFactory(new Random());
        BaseField field = new BaseField(8, 8, provider);

        MatchPolicyStrategy match = new MatchPolicyLine3Plus();
        MoveValidationStrategy validator = new MoveValidationClassic();
        PossibleMovesStrategy possible = new PossibleMovesSimple();
        cascade   = new CascadeEngine(match, provider, new Scoring());

        // безопасная инициализация старта
        new SafeInitializer(provider, match, validator, possible).fill(field);

        GameStats stats = new GameStats();
        int score = 0, moveIndex = 0;

        var sc = new Scanner(System.in);
        out.printHelp();

        while (true) {
            out.printState(field, score);

            if (!possible.anyMoveExists(field, validator, match)) {
                out.printNoMoves();
                break;
            }

            out.prompt();
            String line = sc.nextLine();
            Command cmd = in.parse(line);

            if (cmd instanceof Quit) {
                out.printEnd(stats);
                return;
            } else if (cmd instanceof Help) {
                out.printHelp();
                continue;
            } else if (cmd instanceof Invalid i) {
                out.printError(i.error());
                continue;
            } else if (cmd instanceof Command.Restart) {
                // очистим счёт/историю и сгенерим новое поле
                stats = new GameStats();
                score = 0;
                moveIndex = 0;
                new SafeInitializer(provider, match, validator, possible).fill(field);
                continue;
            } else if (cmd instanceof Move m) {
                Position a = m.a(), b = m.b();
                if (!validator.isValid(field, a, b, match)) {
                    out.printInvalidMove();
                    continue;
                }
                field.swap(a, b);

                MoveRecord rec = new MoveRecord(++moveIndex, a, b);

                // 1) если участвует бонус — активируем удар
                int gained = 0;// суммарные очки за каскад
                gained += activateBonusIfAny(field, a, b);

                // 2) затем обычный каскад
                gained += cascade.cascade(field);

                rec.addWave(new CascadeWave(gained, null)); // без детализации волн
                stats.addMove(rec);
                score += gained;
            }
        }

        out.printEnd(stats);
    }

    private int activateBonusIfAny(BaseField field, Position a, Position b) {
        var opt = findBonus(field, a, b);
        if (opt.isEmpty()) return 0;

        var hit  = opt.get();
        var kill = collectStripeCells(field, hit);
        int gained = scoreAsSingleGroup(kill, hit.bonus().kind());

        clearPositions(field, kill);

        // вместо локальных методов — единая точка в движке
        cascade.settle(field);

        return gained;
    }
    private record BonusAt(Position pos, BonusElement bonus) {}

    private Optional<BonusAt> findBonus(BaseField f, Position a, Position b) {
        var ea = f.get(a);
        if (ea != null && ea.type() == ElementType.BONUS) {
            return Optional.of(new BonusAt(a, (BonusElement) ea));
        }
        var eb = f.get(b);
        if (eb != null && eb.type() == ElementType.BONUS) {
            return Optional.of(new BonusAt(b, (BonusElement) eb));
        }
        return Optional.empty();
    }

    private Set<Position> collectStripeCells(BaseField f, BonusAt at) {
        Set<Position> kill = new HashSet<>();
        if (at.bonus().kind() == BonusKind.STRIPE_H) {
            int r = at.pos().row();
            for (int c = 0; c < f.columns(); c++) kill.add(new Position(r, c));
        } else { // STRIPE_V
            int c = at.pos().column();
            for (int r = 0; r < f.rows(); r++) kill.add(new Position(r, c));
        }
        return kill;
    }

    private int scoreAsSingleGroup(Set<Position> cells, BonusKind kind) {
        var shape = (kind == BonusKind.STRIPE_H) ? MatchGroup.MatchShape.LINE_H
                : MatchGroup.MatchShape.LINE_V;
        var group  = new MatchGroup(cells, shape);
        var result = new MatchResult(java.util.List.of(group));
        return new Scoring().scoreFor(result);
    }

    private void clearPositions(BaseField f, Set<Position> cells) {
        for (var p : cells) f.set(p, null);
    }
}
