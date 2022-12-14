package com.evolutiongenerator.constant;

/**
 * Configuration constants
 *
 * @author Patryk Klatka
 */
public enum ConfigurationConstant {
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
     * Returns a ConfigurationConstantType of constant
     *
     * @author Patryk Klatka
     * @return ConfigurationConstantType of constant
     */
    public ConfigurationConstantType getType() throws IllegalArgumentException{
        return switch (this) {
            case MAP_WIDTH, MAP_HEIGHT, ANIMAL_START_NUMBER, GENOTYPE_LENGTH,
                    ANIMAL_START_ENERGY, ANIMAL_REPRODUCTION_ENERGY,
                    ANIMAL_REPRODUCTION_ENERGY_COST, PLANT_START_NUMBER,
                    PLANT_ENERGY, PLANT_SPAWN_NUMBER, MINIMUM_MUTATION_NUMBER,
                    MAXIMUM_MUTATION_NUMBER ->
                    ConfigurationConstantType.INTEGER;
            case STATISTICS_FILE_PATH -> ConfigurationConstantType.STRING;
            case MAP_VARIANT -> ConfigurationConstantType.MAP_VARIANT;
            case ANIMAL_BEHAVIOUR_VARIANT -> ConfigurationConstantType.ANIMAL_BEHAVIOUR_VARIANT;
            case PLANT_GROWTH_VARIANT -> ConfigurationConstantType.PLANT_GROWTH_VARIANT;
            case MUTATION_VARIANT -> ConfigurationConstantType.MUTATION_VARIANT;
        };
    }
}
