package org.union4dev.base.module.handlers;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.annotations.module.Disable;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.module.Category;
import org.union4dev.base.value.AbstractValue;

import javax.swing.*;
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
        } else {
            EventManager.unregister(object);
            invokeMethodsAnnotationPresent(Disable.class);
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
