package com.skyfork.api.cedo.drag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class DragManager {
    private HashMap<String, Dragging> draggable = new HashMap<>();

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public Dragging createDrag(Class<?> clazz,String name, float x, float y) {
        draggable.put(name, new Dragging(clazz,name, x, y));
        return draggable.get(name);
    }

}
