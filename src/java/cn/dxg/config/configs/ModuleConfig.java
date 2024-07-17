package cn.dxg.config.configs;

import cn.dxg.ColorValue;
import cn.dxg.config.Config;
import com.google.gson.*;
import org.union4dev.base.Access;
import org.union4dev.base.module.handlers.ModuleHandle;
import org.union4dev.base.value.AbstractValue;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;

public class ModuleConfig extends Config
{
    public ModuleConfig() {
        super("modules.json");
    }
    
    public JsonObject saveConfig() {
        final JsonObject object = new JsonObject();
        for (final ModuleHandle module : Access.getInstance().getModuleManager().getCModules().values()) {
            final JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("state", Boolean.valueOf(module.isEnabled()));
            moduleObject.addProperty("key", module.getKey());
            final JsonObject valuesObject = new JsonObject();
            for (final AbstractValue<?> value : module.getValues()) {
                if (value instanceof NumberValue) {
                    valuesObject.addProperty(value.getName(), ((NumberValue)value).getValue());
                }
                else if (value instanceof BooleanValue) {
                    valuesObject.addProperty(value.getName(), ((BooleanValue)value).getValue());
                }
                else if (value instanceof ComboValue) {
                    valuesObject.addProperty(value.getName(), ((ComboValue)value).getValue());
                }
                else {
                    if (!(value instanceof ColorValue)) {
                        continue;
                    }
                    valuesObject.addProperty(value.getName(), ((ColorValue)value).getColor());
                }
            }
            moduleObject.add("values", valuesObject);
            object.add(module.getName(), moduleObject);
        }
        return object;
    }
    
    public void loadConfig(final JsonObject object) {
        for (final ModuleHandle module : Access.getInstance().getModuleManager().getCModules().values()) {
            if (object.has(module.getName())) {
                final JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();
                if (moduleObject.has("state")) {
                    module.setEnable(moduleObject.get("state").getAsBoolean());
                }
                if (moduleObject.has("key")) {
                    module.setKey(moduleObject.get("key").getAsInt());
                }
                if (!moduleObject.has("values")) {
                    continue;
                }
                final JsonObject valuesObject = moduleObject.get("values").getAsJsonObject();
                for (final AbstractValue<?> value : module.getValues()) {
                    if (valuesObject.has(value.getName())) {
                        final JsonElement theValue = valuesObject.get(value.getName());
                        if (value instanceof NumberValue) {
                            ((NumberValue)value).setValue(theValue.getAsNumber().doubleValue());
                        }
                        else if (value instanceof BooleanValue) {
                            ((BooleanValue)value).setValue(theValue.getAsBoolean());
                        }
                        else if (value instanceof ComboValue) {
                            ((ComboValue)value).setValue(theValue.getAsString());
                        }
                        else {
                            if (!(value instanceof ColorValue)) {
                                continue;
                            }
                            final Color color = new Color(theValue.getAsInt());
                            ((ColorValue)value).setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()).getRGB());
                        }
                    }
                }
            }
        }
    }
}