package com.evolutiongenerator.handler;

import javafx.scene.control.ChoiceBox;

public class ChoiceBoxHandler implements IConfigurationField{
    private ChoiceBox<String> choiceBox;
    public ChoiceBoxHandler(ChoiceBox<String> choiceBox, ChoiceBox<String>exampleConfiguration){
        this.choiceBox = choiceBox;

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
    public String readProperty() {
        return choiceBox.getValue();
    }
}
