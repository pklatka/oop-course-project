package com.evolutiongenerator.handler;

import com.evolutiongenerator.constant.ISimulationConfigurationValue;
import javafx.scene.control.ChoiceBox;

/**
 * Wrapper class for ChoiceBox.
 *
 * @author Patryk Klatka
 */
public class ChoiceBoxHandler implements IConfigurationField {
    private final ChoiceBox<ISimulationConfigurationValue> choiceBox;

    /**
     * Constructor, sets choiceBox and fromStringFunction,
     * adds listener to choiceBox to reset exampleConfiguration value.
     *
     * @param choiceBox            ChoiceBox of strings.
     * @param exampleConfiguration ExampleConfiguration object.
     */
    public ChoiceBoxHandler(ChoiceBox<ISimulationConfigurationValue> choiceBox, ChoiceBox<String> exampleConfiguration) {
        this.choiceBox = choiceBox;

        choiceBox.setOnAction((event) -> {
            // Reset exampleConfiguration
            exampleConfiguration.setValue("");
        });
    }

    /**
     * Write value to ChoiceBox.
     *
     * @param value ISimulationConfigurationValue value to write.
     */
    @Override
    public void writeProperty(ISimulationConfigurationValue value) {
        if (choiceBox.getItems().contains(value)) {
            choiceBox.setValue(value);
        }
    }

    /**
     * Get value from ChoiceBox.
     *
     * @return ISimulationConfigurationValue value from ChoiceBox.
     * @throws IllegalArgumentException if value is not in ChoiceBox.
     */
    @Override
    public ISimulationConfigurationValue readProperty() throws IllegalArgumentException {
        try {
            return choiceBox.getValue();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wartość listy rozwijalnej jest niepoprawna");
        }
    }
}
