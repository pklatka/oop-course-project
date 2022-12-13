package com.evolutiongenerator.constant;

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

    // Variants
    MAP_VARIANT,
    ANIMAL_BEHAVIOUR_VARIANT,
    PLANT_GROWTH_VARIANT,
    MUTATION_VARIANT;


    public Class getConstantType() throws IllegalArgumentException{
        return switch (this) {
            case MAP_WIDTH, MAP_HEIGHT, ANIMAL_START_NUMBER, GENOTYPE_LENGTH,
                    ANIMAL_START_ENERGY, ANIMAL_REPRODUCTION_ENERGY,
                    ANIMAL_REPRODUCTION_ENERGY_COST, PLANT_START_NUMBER,
                    PLANT_ENERGY, PLANT_SPAWN_NUMBER, MINIMUM_MUTATION_NUMBER,
                    MAXIMUM_MUTATION_NUMBER ->
                    Integer.class;
            case STATISTICS_FILE_PATH -> String.class;
            case MAP_VARIANT -> MapVariant.class;
            case ANIMAL_BEHAVIOUR_VARIANT -> AnimalBehaviourVariant.class;
            case PLANT_GROWTH_VARIANT -> PlantGrowthVariant.class;
            case MUTATION_VARIANT -> MutationVariant.class;
        };
    }
}
