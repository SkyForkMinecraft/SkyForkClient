package cn.langya.utils;

public class AnimationUtil {
    public static float animate(float target, float current, double speed) {
        boolean larger = target > current;

        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = (float) ((double) dif * speed);

        current = larger ? current + factor : current - factor;
        return current;
    }

    public static double animate(double target, double current, double speed) {
        boolean larger = target > current;

        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;

        if (factor < 0.10000000149011612D) {
            factor = 0.10000000149011612D;
        }

        current = larger ? current + factor : current - factor;
        return current;
    }
}
