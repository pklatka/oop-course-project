package com.evolutiongenerator.handler;

import javafx.scene.control.ChoiceBox;

import java.util.function.Function;

public class ChoiceBoxHandler implements IConfigurationField{
    private ChoiceBox<String> choiceBox;
    private Function<String, Object> fromStringFunction;
    public ChoiceBoxHandler(ChoiceBox<String> choiceBox, ChoiceBox<String>exampleConfiguration, Function<String, Object> fromStringFunction) {
        this.choiceBox = choiceBox;
        this.fromStringFunction = fromStringFunction;
        choiceBox.setOnAction((event)->{
            // Reset exampleConfiguration
            exampleConfiguration.setValue("");
        });
    }

    @Override
    public void writeProperty(String text) {
        if(choiceBox.getItems().contains(text)){
            choiceBox.setValue(text);
        }
    }

    @Override
    public Object readProperty() {
        return fromStringFunction.apply(choiceBox.getValue());
    }
}
