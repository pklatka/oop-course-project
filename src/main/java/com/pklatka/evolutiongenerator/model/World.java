package com.pklatka.evolutiongenerator.model;


/*
 * Uwaga: Rozwiązanie renderuje całą siatkę od nowa -> nie wydajne rozwiązanie, może zacinać aplikację
 *   dla np. obrazów z dużymi rozmiarami
 *
 * Uwaga 2: Dodano Hashmapę do klasy GuiElementBox, aby szybciej ładować zdjęcia
 *
 * */

import com.pklatka.evolutiongenerator.gui.App;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {
        try {
            Application.launch(App.class, args);

//            MoveDirection[] directions = new OptionsParser().parse(args);
//            IWorldMap map = new GrassField(10);
//            // IWorldMap map = new RectangularMap(10, 5);
//            Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 4)};
//            //  Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2, 2)};
//            // f b r l f f r r f f f f f f f f
//            IEngine engine = new SimulationEngine(directions, map, positions);
//            engine.run();
//            // SimulationEngine engine = new SimulationEngine(directions, map, positions);
//            // engine.runAnimation();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
