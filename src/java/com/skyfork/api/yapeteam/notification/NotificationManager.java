package com.skyfork.api.yapeteam.notification;

import com.skyfork.api.yapeteam.util.animation2.Easing;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.EventManager;
import com.skyfork.client.events.render.Render2DEvent;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yuxiangll
 * @since 2024/1/8 04:59
 * IntelliJ IDEA
 */
@SuppressWarnings("unused")
public class NotificationManager {
    private final CopyOnWriteArrayList<Notification> notificationArrayList;

    public NotificationManager() {
        notificationArrayList = new CopyOnWriteArrayList<>();
        EventManager.register(this);
    }

    public void clearAll() {
        notificationArrayList.clear();
    }

    public void post(Notification notification) {
        notificationArrayList.add(notification);
    }

    public void post(String text,NotificationType type) {
        notificationArrayList.add(new Notification(text, Easing.EASE_IN_OUT_QUAD,
                Easing.EASE_IN_OUT_QUAD,
                2500, type));
    }

    @EventTarget
    public void onRender(final Render2DEvent event) {
        val sr = new ScaledResolution(Minecraft.getMinecraft());
        int pre_size = notificationArrayList.size();
        for (int j = 0; j < pre_size; j++)
            for (int i = 0; i < notificationArrayList.size(); i++)
                if (notificationArrayList.get(i) != null && notificationArrayList.get(i).isDone()) {
                    notificationArrayList.remove(notificationArrayList.get(i));
                    i--;
                }
        for (int i = 0; i < notificationArrayList.size(); i++)
            notificationArrayList.get(i).render(sr, i);
    }
}