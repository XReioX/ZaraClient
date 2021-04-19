package zaraclient;

public class MathUtils {

    public static double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return Math.round(val * one) / one;
    }
}
