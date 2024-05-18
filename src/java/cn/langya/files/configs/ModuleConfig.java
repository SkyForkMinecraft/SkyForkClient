package cn.langya.files.configs;

import cn.langya.files.Config;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.union4dev.base.Access;
import org.union4dev.base.value.AbstractValue;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

public class ModuleConfig extends Config {
    public ModuleConfig(String name) {
        super(name);
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();
        Access.getInstance().getModuleManager().getCModules().values().forEach(module -> {
            JsonObject moduleObj = new JsonObject();
            moduleObj.addProperty("Enable", module.isEnabled());
            moduleObj.addProperty("KeyCode", module.getKey());

            JsonObject valueObj = new JsonObject();
            moduleObj.add("Values", valueObj);
            for (AbstractValue<?> value : module.getValues()) {
                if (value instanceof NumberValue) {
                    valueObj.addProperty(value.getName(), (value).getValue().toString());
                } else if (value instanceof BooleanValue) {
                    valueObj.addProperty(value.getName(), ((BooleanValue) value).getValue());
                } else if (value instanceof ComboValue) {
                    valueObj.addProperty(value.getName(), ((ComboValue) value).getValue());
                }
            }
            object.add(module.getName(), moduleObj);
        });

        return object;
    }

    @Override
    public void load(JsonObject object) {
        Access.getInstance().getModuleManager().getCModules().values().forEach(module -> {
            if (object.has(module.getName())) {
                JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();
                if (moduleObject.has("Enable")) module.setEnable(moduleObject.get("Enable").getAsBoolean());
                if (moduleObject.has("KeyCode")) module.setKey(moduleObject.get("KeyCode").getAsInt());
                if (moduleObject.has("Values")) {
                    JsonObject valuesObject = moduleObject.get("Values").getAsJsonObject();
                    for (AbstractValue<?> value : module.getValues()) {
                        if (valuesObject.has(value.getName())) {
                            JsonElement theValue = valuesObject.get(value.getName());
                            if (value instanceof NumberValue) {
                                ((NumberValue) value).setValue(theValue.getAsDouble());
                            } else if (value instanceof BooleanValue) {
                                ((BooleanValue) value).setValue(theValue.getAsBoolean());
                            } else if (value instanceof ComboValue) {
                                ((ComboValue) value).setValue(theValue.getAsString());
                            }
                        }
                    }
                }
            }
        });
    }
}
