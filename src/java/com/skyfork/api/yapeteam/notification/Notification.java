package com.skyfork.api.yapeteam.notification;

import com.skyfork.api.cedo.render.RenderUtil;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.yapeteam.util.animation2.Easing;
import com.skyfork.api.yapeteam.util.animation2.EasingAnimation;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * @author yuxiangll
 * @since 2024/1/8 04:58
 * IntelliJ IDEA
 */
@Getter
public class Notification {
    private final String content;
    private final EasingAnimation animationX, animationY, animationProcess;
    private final NotificationType type;
    private final Color color;
    private final long begin_time, duration;
    private boolean initialized = false;
    private final float height = 20;

    public Notification(String content, Easing easingX, Easing easingY, long duration, NotificationType type) {
        this.content = content;
        this.animationX = new EasingAnimation(easingX, (long) (duration * 0.2), 0);
        this.animationY = new EasingAnimation(easingY, (long) (duration * 0.2), 0);
        this.animationProcess = new EasingAnimation(Easing.EASE_OUT_QUART, (long) (duration * 0.8), 0);
        this.type = type;
        switch (type) {
            case INIT:
                color = new Color(161, 161, 161);
                break;
            case SUCCESS:
                color = new Color(0, 255, 42);
                break;
            case FAILED:
                color = new Color(255, 0, 30);
                break;
            case WARNING:
                color = new Color(255, 251, 0);
                break;
            default:
                color = new Color(-1);
        }
        begin_time = System.currentTimeMillis();
        this.duration = duration;
    }

    public boolean isDone() {
        return System.currentTimeMillis() >= begin_time + duration;
    }

    public void render(ScaledResolution sr, int index) {
        val font = FontManager.M18;

        float width = (float) (font.getStringWidth(content) + 5 * 2);
        float targetY = sr.getScaledHeight() - (height + 2) * (index + 1);
        if (!initialized) {
            animationX.setStartValue(sr.getScaledWidth());
            animationY.setStartValue(targetY);
            initialized = true;
        }
        float targetX = sr.getScaledWidth() - width - 2;
        if (System.currentTimeMillis() >= begin_time + duration * 0.8) {
            targetX = sr.getScaledWidth() + 2;
            animationX.setDuration((long) (duration * 0.2));
        }

        float x = (float) animationX.getValue(targetX), y = (float) animationY.getValue(targetY);
        RenderUtil.drawRect(x, y, x + width, y + height, new Color(0,0,0,80).getRGB());
        RenderUtil.drawRect(x, y, x + width * animationProcess.getValue(1), y + ( height / 8), color.getRGB());
        font.drawString(content, x + 5, y + (height - font.getHeight()) / 2f, type == null ? 0 : -1);
    }
}