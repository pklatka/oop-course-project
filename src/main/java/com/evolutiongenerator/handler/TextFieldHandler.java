package com.evolutiongenerator.handler;

import com.evolutiongenerator.constant.ISimulationValue;
import com.evolutiongenerator.constant.IntegerValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Wrapper class for TextField
 *
 * @author Patryk Klatka
 */
public class TextFieldHandler implements IConfigurationField {
    private final TextField textField;

    /**
     * Constructor, sets textField and fromStringFunction,
     * adds listener to textField to reset exampleConfiguration value.
     *
     * @author Patryk Klatka
     * @param textField TextField
     * @param exampleConfiguration ExampleConfiguration object
     */
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

    /**
     * Write value to TextField
     *
     * @author Patryk Klatka
     * @param value String value to write
     */
    @Override
    public void writeProperty(ISimulationValue value) {
        textField.setText(value.toString());
    }

    /**
     * Get value from TextField
     *
     * @author Patryk Klatka
     * @return ISimulationValue value from TextField
     */
    @Override
    public ISimulationValue readProperty() throws IllegalArgumentException {
        try{
            return new IntegerValue(textField.getText());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e);
        }
    }
}
