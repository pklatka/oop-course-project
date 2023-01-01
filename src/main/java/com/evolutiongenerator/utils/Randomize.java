package com.evolutiongenerator.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that provides random values.
 *
 * @author Paweł Motyka
 */
public class Randomize {

    public static int generateInt(int max, int min) {
        return ThreadLocalRandom.current().nextInt(max + 1 - min) + min;
    }

    public static boolean generateBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * Generates boolean with given probability.
     *
     * @param probabilityTrue number between 0 and 1 telling about probability of true.
     * @return boolean value from a given probability distribution.
     */
    public static boolean generateBooleanWithProbability(double probabilityTrue) {
        return ThreadLocalRandom.current().nextDouble() >= 1.0 - probabilityTrue;
    }
}
