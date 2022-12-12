package com.evolutiongenerator.handler;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class TextFieldHandler implements IConfigurationField {
    private TextField textField;

    public TextFieldHandler(TextField textField, ChoiceBox<String>exampleConfiguration){
        this.textField = textField;

        // Validate if textField contains number
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                // Reset exampleConfiguration
                exampleConfiguration.setValue("");

                if(newValue.equals("")){
                    textField.setText("");
                    return;
                }

                Integer.parseInt(newValue);
                textField.setText(newValue);
            }catch(NumberFormatException e){
                textField.setText(oldValue);
            }
        });
    }

    @Override
    public void writeProperty(String text) {
        textField.setText(text);
    }

    @Override
    public String readProperty() {
        return textField.getText().trim();
    }
}
