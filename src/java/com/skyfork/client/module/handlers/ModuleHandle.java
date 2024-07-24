package com.skyfork.client.module.handlers;

import com.skyfork.api.yapeteam.notification.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.module.Disable;
import com.skyfork.client.annotations.module.Enable;
import com.skyfork.client.events.EventManager;
import com.skyfork.client.module.Category;
import com.skyfork.client.value.AbstractValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
public final class ModuleHandle {

    @Getter
    private final String name;

    @Getter
    private final Category category;

    private final Object object;

    public float optionAnim;
    public float optionAnimNow;

    @Setter
    @Getter
    private ArrayList<AbstractValue<?>> values;

    private boolean state;

    @Getter
    @Setter
    private boolean visible;

    @Getter
    @Setter
    private String suffix;

    @Getter
    @Setter
    private int key;

    public ModuleHandle(String name, Category category, Object object) {
        this.state = false;
        this.name = name;
        this.category = category;
        this.object = object;
        this.values = new ArrayList<>();
        this.visible = true;
        this.suffix = "";
        this.key = Keyboard.KEY_NONE;
    }

    public void setEnable(boolean state) {
        if (state == this.state) return;
        this.state = state;
        if (state) {
            EventManager.register(object);
            invokeMethodsAnnotationPresent(Enable.class);
            if (Access.getInstance().getNotificationManager()!= null && this.getName() != "功能管理页面") Access.getInstance().getNotificationManager().post(name + " 已开启",NotificationType.SUCCESS);
        } else {
            EventManager.unregister(object);
            invokeMethodsAnnotationPresent(Disable.class);
            if (Access.getInstance().getNotificationManager()!= null  && this.getName() != "功能管理页面") Access.getInstance().getNotificationManager().post(name + " 已关闭",NotificationType.FAILED);
        }
    }

    private void invokeMethodsAnnotationPresent(Class<? extends Annotation> anno) {
        for (Method method : this.object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(anno)) {
                try {
                    method.invoke(object);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }
    }


    public boolean isEnabled() {
        return state;
    }
}
