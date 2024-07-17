package cn.dxg.config.configs;

import cn.cedo.drag.Dragging;
import cn.dxg.config.Config;
import com.google.gson.*;
import org.union4dev.base.Access;

public class HudConfig extends Config
{
    public HudConfig() {
        super("hud.json");
    }
    
    public JsonObject saveConfig() {
        final JsonObject object = new JsonObject();
        for (final Dragging hud : Access.getInstance().getDragManager().getDraggable().values()) {
            final JsonObject hudObject = new JsonObject();
            hudObject.addProperty("x", hud.getXPos());
            hudObject.addProperty("y", hud.getYPos());
            object.add(hud.getName(), hudObject);
        }
        return object;
    }
    
    public void loadConfig(final JsonObject object) {
        for (final Dragging hud : Access.getInstance().getDragManager().getDraggable().values()) {
            if (object.has(hud.getName())) {
                final JsonObject hudObject = object.get(hud.getName()).getAsJsonObject();
                hud.setXPos(hudObject.get("x").getAsFloat());
                hud.setYPos(hudObject.get("y").getAsFloat());
            }
        }
    }
}