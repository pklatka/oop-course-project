package com.evolutiongenerator.utils;

import java.util.Random;

public class Randomize {
    static Random random = new Random();



    public static int generateInt(int max, int min){
        return random.nextInt(max + 1 - min) + min;
    }

    public static boolean generateBoolean(){
        return random.nextBoolean();
    }

}
