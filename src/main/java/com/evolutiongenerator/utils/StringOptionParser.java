package com.evolutiongenerator.utils;

import com.evolutiongenerator.model.mapObject.MoveDirection;

public class StringOptionParser {
    public MoveDirection[] parse(String[] strArr) throws IllegalArgumentException {

        int moveDirectionArrLength = 0;
        for (String str : strArr) {
            switch (str) {
                case "f", "forward", "b", "backward", "l", "left", "r", "right" -> moveDirectionArrLength += 1;
                default -> throw new IllegalArgumentException(str + " is not legal move specification");
            }
        }

        MoveDirection[] mapDirArr = new MoveDirection[moveDirectionArrLength];

        int i = 0;
        for (String str : strArr) {
            switch (str) {
                case "f", "forward" -> {
                    mapDirArr[i] = MoveDirection.FORWARD;
                    i += 1;
                }
                case "b", "backward" -> {
                    mapDirArr[i] = MoveDirection.BACKWARD;
                    i += 1;
                }
                case "l", "left" -> {
                    mapDirArr[i] = MoveDirection.LEFT;
                    i += 1;
                }
                case "r", "right" -> {
                    mapDirArr[i] = MoveDirection.RIGHT;
                    i += 1;
                }
            }
        }
        return mapDirArr;
    }
}
