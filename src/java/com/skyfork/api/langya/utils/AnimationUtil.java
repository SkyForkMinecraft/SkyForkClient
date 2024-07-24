package com.skyfork.api.langya.utils;

import com.skyfork.api.langya.modules.misc.particles.TimerUtils;

public class AnimationUtil {


    private static TimerUtils timer;
    public static float animate(float target, float current, float speed) {
        if (current > target) return current;
        timer = new TimerUtils();
        if (timer.delay(1000 / speed)) {
            if (current > target) return current;
            current += speed;
        }
        return current;
    }

}
