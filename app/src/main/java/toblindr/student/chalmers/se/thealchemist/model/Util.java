package toblindr.student.chalmers.se.thealchemist.model;

import java.util.Random;

class Util {
    private static Random random = new Random();
    public static int getRandomNumber(int exclusiveMax) {
        return random.nextInt(exclusiveMax);
    }
}
