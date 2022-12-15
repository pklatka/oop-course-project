package com.evolutiongenerator.handler;

import com.evolutiongenerator.constant.ISimulationValue;
import javafx.scene.control.ChoiceBox;

/**
 * Wrapper class for ChoiceBox
 *
 * @author Patryk Klatka
 */
public class ChoiceBoxHandler implements IConfigurationField{
    private final ChoiceBox<ISimulationValue> choiceBox;

    /**
     * Constructor, sets choiceBox and fromStringFunction,
     * adds listener to choiceBox to reset exampleConfiguration value.
     *
     * @author Patryk Klatka
     * @param choiceBox ChoiceBox of strings
     * @param exampleConfiguration ExampleConfiguration object
     */
    public ChoiceBoxHandler(ChoiceBox<ISimulationValue> choiceBox, ChoiceBox<String>exampleConfiguration) {
        this.choiceBox = choiceBox;

        choiceBox.setOnAction((event)->{
            // Reset exampleConfiguration
            exampleConfiguration.setValue("");
        });
    }

    /**
     * Write value to ChoiceBox
     *
     * @author Patryk Klatka
     * @param value ISimulationValue value to write
     */
    @Override
    public void writeProperty(ISimulationValue value) {
        if(choiceBox.getItems().contains(value)){
            choiceBox.setValue(value);
        }
    }

    /**
     * Get value from ChoiceBox
     *
     * @author Patryk Klatka
     * @return ISimulationValue value from ChoiceBox
     */
    @Override
    public ISimulationValue readProperty() throws IllegalArgumentException {
        try{
            return choiceBox.getValue();
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("ChoiceBox value is not valid");
        }
    }
}
