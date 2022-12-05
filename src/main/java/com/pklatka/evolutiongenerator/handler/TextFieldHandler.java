package com.pklatka.evolutiongenerator.handler;

import javafx.scene.control.TextField;

public class TextFieldHandler implements IConfigurationField {
    TextField textField;
    public TextFieldHandler(TextField textField){
        this.textField = textField;
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
