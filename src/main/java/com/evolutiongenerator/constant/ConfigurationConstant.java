package com.evolutiongenerator.constant;

/**
 * Configuration constants
 *
 * @author Patryk Klatka
 */
public enum ConfigurationConstant {
    SIMULATION_COUNTER,
    MAP_WIDTH,
    MAP_HEIGHT,
    ANIMAL_START_NUMBER,
    GENOTYPE_LENGTH,
    ANIMAL_START_ENERGY,
    ANIMAL_REPRODUCTION_ENERGY,
    ANIMAL_REPRODUCTION_ENERGY_COST,
    PLANT_START_NUMBER,
    PLANT_ENERGY,
    PLANT_SPAWN_NUMBER,
    MINIMUM_MUTATION_NUMBER,
    MAXIMUM_MUTATION_NUMBER,
    STATISTICS_FILE_PATH,

    // ******** Variants ********
    MAP_VARIANT,
    ANIMAL_BEHAVIOUR_VARIANT,
    PLANT_GROWTH_VARIANT,
    MUTATION_VARIANT;


    /**
     * Configuration constants types. Used to determine the type of the value.
     *
     * @author Patryk Klatka
     */
    public enum ConfigurationConstantType {
        INTEGER,
        PATH,
        ANIMAL_BEHAVIOUR_VARIANT,
        MAP_VARIANT,
        PLANT_GROWTH_VARIANT,
        MUTATION_VARIANT;

        /**
         * Returns a parsed from string ISimulationConfigurationValue value
         *
         * @return ISimulationConfigurationValue value
         */
        public ISimulationConfigurationValue getValueFromString(String value) throws IllegalArgumentException {
            try {
                return switch (this) {
                    case INTEGER -> IntegerValue.fromString(value);
                    case PATH -> PathValue.fromString(value);
                    case ANIMAL_BEHAVIOUR_VARIANT -> AnimalBehaviourVariant.fromString(value);
                    case MAP_VARIANT -> MapVariant.fromString(value);
                    case PLANT_GROWTH_VARIANT -> PlantGrowthVariant.fromString(value);
                    case MUTATION_VARIANT -> MutationVariant.fromString(value);
                };
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Nie udało się przekonwertować wartości " + value + " do typu "
                        + this + ". Czy na pewno jest ona poprawna?");
            }
        }
    }


    /**
     * Returns a ConfigurationConstantType of constant
     *
     * @return ConfigurationConstantType of constant
     */
    public ConfigurationConstantType getType() throws IllegalArgumentException {
        return switch (this) {
            case MAP_WIDTH, MAP_HEIGHT, ANIMAL_START_NUMBER, GENOTYPE_LENGTH,
                    ANIMAL_START_ENERGY, ANIMAL_REPRODUCTION_ENERGY,
                    ANIMAL_REPRODUCTION_ENERGY_COST, PLANT_START_NUMBER,
                    PLANT_ENERGY, PLANT_SPAWN_NUMBER, MINIMUM_MUTATION_NUMBER,
                    MAXIMUM_MUTATION_NUMBER, SIMULATION_COUNTER -> ConfigurationConstantType.INTEGER;
            case STATISTICS_FILE_PATH -> ConfigurationConstantType.PATH;
            case MAP_VARIANT -> ConfigurationConstantType.MAP_VARIANT;
            case ANIMAL_BEHAVIOUR_VARIANT -> ConfigurationConstantType.ANIMAL_BEHAVIOUR_VARIANT;
            case PLANT_GROWTH_VARIANT -> ConfigurationConstantType.PLANT_GROWTH_VARIANT;
            case MUTATION_VARIANT -> ConfigurationConstantType.MUTATION_VARIANT;
        };
    }
}
