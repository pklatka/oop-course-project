package com.evolutiongenerator.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randomize {

    public static int generateInt(int max, int min){
        return ThreadLocalRandom.current().nextInt(max + 1 - min) + min;
    }

    public static boolean generateBoolean(){
        return ThreadLocalRandom.current().nextBoolean();
    }

}
