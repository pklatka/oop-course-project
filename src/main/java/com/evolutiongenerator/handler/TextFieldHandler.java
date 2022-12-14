package com.evolutiongenerator.handler;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.function.Function;

/**
 * Wrapper class for TextField
 *
 * @author Patryk Klatka
 */
public class TextFieldHandler implements IConfigurationField {
    private final TextField textField;
    private final Function<String, Object> fromStringFunction;
    /**
     * Constructor, sets textField and fromStringFunction,
     * adds listener to textField to reset exampleConfiguration value.
     *
     * @author Patryk Klatka
     * @param textField TextField
     * @param exampleConfiguration ExampleConfiguration object
     * @param fromStringFunction Function to convert String to Object (e.g. Integer::parseInt)
     */
    public TextFieldHandler(TextField textField, ChoiceBox<String>exampleConfiguration, Function<String, Object> fromStringFunction){
        this.textField = textField;
        this.fromStringFunction = fromStringFunction;

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

    /**
     * Write value to TextField
     *
     * @author Patryk Klatka
     * @param text String value to write
     */
    @Override
    public void writeProperty(String text) {
        textField.setText(text);
    }

    /**
     * Get value from TextField
     *
     * @author Patryk Klatka
     * @return Integer value from TextField
     */
    @Override
    public Object readProperty() {
        return fromStringFunction.apply(textField.getText().trim());
    }
}
