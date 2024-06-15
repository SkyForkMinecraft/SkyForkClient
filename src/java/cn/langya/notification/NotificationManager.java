package cn.langya.notification;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    @Getter
    public final List<Notification> notifications = new CopyOnWriteArrayList<>();

    public void add(String text) {
        Notification notification = new Notification(text);
        notifications.add(notification);
    }

}
