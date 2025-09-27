package ru.saxophilyman.threeinarow;

import ru.saxophilyman.threeinarow.core.GameRunner;
import ru.saxophilyman.threeinarow.core.field.BaseField;
import ru.saxophilyman.threeinarow.core.element.ElementFactory;

import java.util.Random;


public class ThreeInARowApplication {

	public static void main(String[] args) {
		new GameRunner().run();

	}

}
//mvn -q -DskipTests package   # сборка
//mvn -q test                  # тесты
//# Запуск main — из IDE (просто Run) или:
//		# java -cp target/ThreeInARow-0.0.1-SNAPSHOT.jar <MainClass>  (если сделаешь uber-jar через shade)