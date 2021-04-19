package zaraclient;

import java.util.Random;

public class RandomUtils {

    public static int random(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static float randomFloat(float min, float max){
        Random random = new Random();
        return min + random.nextFloat() * (max-min);

    }

    public static double randomDouble(double min, double max){
        Random random = new Random();
        return min + random.nextDouble() * (max-min);

    }
}
