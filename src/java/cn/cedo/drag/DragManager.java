package cn.cedo.drag;

import cn.langya.files.ConfigManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.union4dev.base.module.handlers.ModuleHandle;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

@Getter
public class DragManager {
    private HashMap<String, Dragging> draggable = new HashMap<>();

    private final File DRAG_DATA = new File(ConfigManager.fold, "Drag.json");
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public Dragging createDrag(String name, float x, float y) {
        draggable.put(name, new Dragging(name, x, y));
        return draggable.get(name);
    }

    public void saveDragData() {
        if (!DRAG_DATA.exists()) {
            DRAG_DATA.getParentFile().mkdirs();
        }
        try {
            Files.write(DRAG_DATA.toPath(), GSON.toJson(draggable.values()).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to save draggable");
        }
    }

    public void loadDragData() {
        if (!DRAG_DATA.exists()) {
            System.out.println("No drag data found");
            return;
        }

        Dragging[] draggings;
        try {
            draggings = GSON.fromJson(new String(Files.readAllBytes(DRAG_DATA.toPath()), StandardCharsets.UTF_8), Dragging[].class);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to load draggables");
            return;
        }

        for (Dragging dragging : draggings) {
            if (!draggable.containsKey(dragging.getName())) continue;
            Dragging currentDrag = draggable.get(dragging.getName());
            currentDrag.setXPos(dragging.getXPos());
            currentDrag.setYPos(dragging.getYPos());
            draggable.put(dragging.getName(), currentDrag);
        }
    }

}
