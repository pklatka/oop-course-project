package com.evolutiongenerator.application;

import com.evolutiongenerator.constant.AnimalBehaviourVariant;
import com.evolutiongenerator.constant.MutationVariant;
import com.evolutiongenerator.model.map.IWorldMap;
import com.evolutiongenerator.model.map.RectangularMap;
import com.evolutiongenerator.model.mapObject.Animal.Animal;
import com.evolutiongenerator.model.mapObject.Animal.Genes;
import com.evolutiongenerator.model.ui.MapVisualizer;
import com.evolutiongenerator.stage.SimulationConfigurationStage;
import com.evolutiongenerator.utils.Vector2d;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Application.launch(SimulationConfigurationStage.class);
    }
}