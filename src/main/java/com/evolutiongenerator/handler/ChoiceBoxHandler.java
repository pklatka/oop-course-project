package com.evolutiongenerator.handler;

import javafx.scene.control.ChoiceBox;

import java.util.function.Function;

/**
 * Wrapper class for ChoiceBox
 *
 * @author Patryk Klatka
 */
public class ChoiceBoxHandler implements IConfigurationField{
    private final ChoiceBox<String> choiceBox;
    private final Function<String, Object> fromStringFunction;

    /**
     * Constructor, sets choiceBox and fromStringFunction,
     * adds listener to choiceBox to reset exampleConfiguration value.
     *
     * @author Patryk Klatka
     * @param choiceBox ChoiceBox of strings
     * @param exampleConfiguration ExampleConfiguration object
     * @param fromStringFunction Function to convert String to Object (e.g. Integer::parseInt)
     */
    public ChoiceBoxHandler(ChoiceBox<String> choiceBox, ChoiceBox<String>exampleConfiguration, Function<String, Object> fromStringFunction) {
        this.choiceBox = choiceBox;
        this.fromStringFunction = fromStringFunction;

        choiceBox.setOnAction((event)->{
            // Reset exampleConfiguration
            exampleConfiguration.setValue("");
        });
    }

    /**
     * Write value to ChoiceBox
     *
     * @author Patryk Klatka
     * @param text String value to write
     */
    @Override
    public void writeProperty(String text) {
        if(choiceBox.getItems().contains(text)){
            choiceBox.setValue(text);
        }
    }

    /**
     * Get converted value (String to specific object) from ChoiceBox
     *
     * @author Patryk Klatka
     * @return Object value from ChoiceBox
     */
    @Override
    public Object readProperty() {
        return fromStringFunction.apply(choiceBox.getValue());
    }
}
