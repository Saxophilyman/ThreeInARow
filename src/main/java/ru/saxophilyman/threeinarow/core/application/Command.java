package ru.saxophilyman.threeinarow.core.application;

import ru.saxophilyman.threeinarow.core.field.Position;

public sealed interface Command
        permits Command.Help, Command.Invalid, Command.Move, Command.Quit, Command.Restart {

    /* Ходы */
    record Move(Position a, Position b) implements Command { }

    /* Выход */
    record Quit() implements Command { }

    /* Помощь */
    record Help() implements Command { }

    /* Неверная команда */
    record Invalid(String error) implements Command { }
    record Restart() implements Command { }
}