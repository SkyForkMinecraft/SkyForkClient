package cn.langya.files;

import com.google.gson.JsonObject;

public abstract class Config {
    public String name;
    public Config(String name) {
        this.name = name;
    }
    public abstract JsonObject save();
    public abstract void load(JsonObject object);
}
