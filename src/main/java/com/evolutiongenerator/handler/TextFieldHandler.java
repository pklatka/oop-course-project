package com.evolutiongenerator.handler;

import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import com.evolutiongenerator.utils.IntegerValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Wrapper class for TextField.
 *
 * @author Patryk Klatka
 */
public class TextFieldHandler implements IConfigurationField {
    private final TextField textField;

    /**
     * Constructor, sets textField and fromStringFunction,
     * adds listener to textField to reset exampleConfiguration value.
     *
     * @param textField            TextField.
     * @param exampleConfiguration ExampleConfiguration object.
     */
    public TextFieldHandler(TextField textField, ChoiceBox<String> exampleConfiguration) {
        this.textField = textField;

        // Validate if textField contains number
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Reset exampleConfiguration
                exampleConfiguration.setValue("");

                if (newValue.equals("")) {
                    textField.setText("");
                    return;
                }

                Integer.parseInt(newValue);
                textField.setText(newValue);
            } catch (NumberFormatException e) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Write value to TextField.
     *
     * @param value String value to write.
     */
    @Override
    public void writeProperty(ISimulationConfigurationValue value) {
        textField.setText(value.toString());
    }

    /**
     * Get value from TextField.
     *
     * @return ISimulationConfigurationValue value from TextField.
     * @throws IllegalArgumentException if value is not in TextField.
     */
    @Override
    public ISimulationConfigurationValue readProperty() throws IllegalArgumentException {
        try {
            return new IntegerValue(textField.getText());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Błąd wartości w polu tekstowym: " + e.getMessage());
        }
    }
}
