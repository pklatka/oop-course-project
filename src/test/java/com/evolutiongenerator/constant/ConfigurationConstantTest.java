package com.evolutiongenerator.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationConstantTest {

    @Test
    public void testGetTypeMapWidth(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.MAP_WIDTH.getType());
    }

    @Test
    public void testGetTypeMapHeight(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.MAP_HEIGHT.getType());
    }

    @Test
    public void testGetTypeAnimalStartNumber(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.ANIMAL_START_NUMBER.getType());
    }

    @Test
    public void testGetTypeGenotypeLength(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.GENOTYPE_LENGTH.getType());
    }

    @Test
    public void testGetTypeAnimalStartEnergy(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.ANIMAL_START_ENERGY.getType());
    }

    @Test
    public void testGetTypeAnimalReproductionEnergy(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY.getType());
    }

    @Test
    public void testGetTypeAnimalReproductionEnergyCost(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.ANIMAL_REPRODUCTION_ENERGY_COST.getType());
    }

    @Test
    public void testGetTypePlantStartNumber(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.PLANT_START_NUMBER.getType());
    }

    @Test
    public void testGetTypePlantEnergy(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.PLANT_ENERGY.getType());
    }

    @Test
    public void testGetTypePlantSpawnNumber(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.PLANT_SPAWN_NUMBER.getType());
    }

    @Test
    public void testGetTypeMinimumMutationNumber(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.MINIMUM_MUTATION_NUMBER.getType());
    }

    @Test
    public void testGetTypeMaximumMutationNumber(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.MAXIMUM_MUTATION_NUMBER.getType());
    }

    @Test
    public void testGetTypeSimulationCounter(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.INTEGER, ConfigurationConstant.SIMULATION_COUNTER.getType());
    }

    @Test
    public void testGetTypeStatisticsFilePath(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.PATH, ConfigurationConstant.STATISTICS_FILE_PATH.getType());
    }

    @Test
    public void testGetTypeMapVariant(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.MAP_VARIANT, ConfigurationConstant.MAP_VARIANT.getType());
    }

    @Test
    public void testGetTypeAnimalBehaviourVariant(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.ANIMAL_BEHAVIOUR_VARIANT, ConfigurationConstant.ANIMAL_BEHAVIOUR_VARIANT.getType());
    }

    @Test
    public void testGenTypePlantGrowthVariant(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.PLANT_GROWTH_VARIANT, ConfigurationConstant.PLANT_GROWTH_VARIANT.getType());
    }

    @Test
    public void testGenTypeMutationVariant(){
        assertEquals(ConfigurationConstant.ConfigurationConstantType.MUTATION_VARIANT, ConfigurationConstant.MUTATION_VARIANT.getType());
    }
}
