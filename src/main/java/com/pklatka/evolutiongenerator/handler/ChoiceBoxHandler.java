package com.pklatka.evolutiongenerator.handler;

import javafx.scene.control.ChoiceBox;

public class ChoiceBoxHandler implements IConfigurationField{
    ChoiceBox<String> choiceBox;
    public ChoiceBoxHandler(ChoiceBox<String> choiceBox){
        this.choiceBox = choiceBox;
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
