package com.skyfork.api.dxg.config.configs;

import com.skyfork.api.dxg.ColorValue;
import com.skyfork.api.dxg.config.Config;
import com.google.gson.*;
import com.skyfork.client.Access;
import com.skyfork.client.module.handlers.ModuleHandle;
import com.skyfork.client.value.AbstractValue;
import com.skyfork.client.value.impl.BooleanValue;
import com.skyfork.client.value.impl.ComboValue;
import com.skyfork.client.value.impl.NumberValue;

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