package cn.langya.files.configs;

import cn.langya.files.Config;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.union4dev.base.Access;
import org.union4dev.base.value.AbstractValue;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;

public class ModuleConfig extends Config {
    public ModuleConfig(String name) {
        super(name);
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();
        Access.getInstance().getModuleManager().getCModules().values().forEach(module -> {
            JsonObject moduleObj = new JsonObject();
            moduleObj.addProperty("开启状态", module.isEnabled());
            moduleObj.addProperty("绑定按键", module.getKey());

            JsonObject valueObj = new JsonObject();
            moduleObj.add("设置", valueObj);
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
                if (moduleObject.has("开启状态")) module.setEnable(moduleObject.get("开启状态").getAsBoolean());
                if (moduleObject.has("绑定按键")) module.setKey(moduleObject.get("绑定按键").getAsInt());
                if (moduleObject.has("设置")) {
                    JsonObject valuesObject = moduleObject.get("设置").getAsJsonObject();
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
