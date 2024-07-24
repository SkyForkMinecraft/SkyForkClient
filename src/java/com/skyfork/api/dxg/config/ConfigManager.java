package com.skyfork.api.dxg.config;

import com.skyfork.api.dxg.config.configs.HudConfig;
import com.skyfork.api.dxg.config.configs.ModuleConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import com.skyfork.client.Access;

public class ConfigManager implements Access.InstanceAccess {
    public static final List<Config> configs = new ArrayList<>();
    public static final File dir = new File(mc.mcDataDir, "SkyFork");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static File accountFile = new File(dir,"account.data");

    public ConfigManager() {
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (accountFile.exists()) {
            try {
                accountFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        configs.add(new ModuleConfig());
        configs.add(new HudConfig());
    }

    public void loadConfig(String name) {
        File file = new File(dir, name);
        if (file.exists()) {
            System.out.println("Loading config: " + name);
            for (Config config : configs) {
                if (!config.getName().equals(name)) continue;
//                try {
//                   // config.loadConfig(JsonParser.parseReader(new FileReader(file)).getAsJsonObject());
//                }
//                catch (FileNotFoundException e) {
//                    System.out.println("Failed to load config: " + name);
//                    e.printStackTrace();
//                }
                break;
            }
        } else {
            System.out.println("Config " + name + " doesn't exist, creating a new one...");
            this.saveConfig(name);
        }
    }

    public void loadUserConfig(String name) {
        File file = new File(dir, name);
        if (file.exists()) {
            System.out.println("Loading config: " + name);
            for (Config config : configs) {
                if (!config.getName().equals("modules.json")) continue;
//                try {
//                //    config.loadConfig(JsonParser.parseReader(new FileReader(file)).getAsJsonObject());
//                }
//                catch (FileNotFoundException e) {
//                    System.out.println("Failed to load config: " + name);
//                    e.printStackTrace();
//                }
                break;
            }
        } else {
            System.out.println("Config " + name + " doesn't exist, creating a new one...");
            this.saveUserConfig(name);
        }
    }

    public void saveConfig(String name) {
        File file = new File(dir, name);
        try {
            System.out.println("Saving config: " + name);
            file.createNewFile();
            for (Config config : configs) {
                if (!config.getName().equals(name)) continue;
                FileUtils.writeByteArrayToFile(file, gson.toJson(config.saveConfig()).getBytes(StandardCharsets.UTF_8));
                break;
            }
        }
        catch (IOException e) {
            System.out.println("Failed to save config: " + name);
        }
    }

    public void saveUserConfig(String name) {
        File file = new File(dir, name);
        try {
            System.out.println("Saving config: " + name);
            file.createNewFile();
            for (Config config : configs) {
                if (!config.getName().equals("modules.json")) continue;
                FileUtils.writeByteArrayToFile(file, gson.toJson(config.saveConfig()).getBytes(StandardCharsets.UTF_8));
                break;
            }
        }
        catch (IOException e) {
            System.out.println("Failed to save config: " + name);
        }
    }

    public void loadAllConfig() {
        System.out.println("Loading all configs...");
        configs.forEach(it -> this.loadConfig(it.getName()));
    }

    public void saveAllConfig() {
        System.out.println("Saving all configs...");
        configs.forEach(it -> this.saveConfig(it.getName()));
    }
}
