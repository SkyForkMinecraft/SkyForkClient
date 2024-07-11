package cn.langya.files;

import cn.langya.files.configs.ModuleConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.union4dev.base.Access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LangYa
 * @ClassName ConfigManager
 * @since 2024/1/7 下午 03:28
 * @Version 1.0
 */

public class ConfigManager {
    @Getter
    private final List<Config> configs = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final File fold = new File(Minecraft.getMinecraft().mcDataDir, "Skyfork");
    public File dir = new File(fold, "config");
    public File moduleConfigFile = new File(dir, "modules.json");
    public static File accountFile = new File(new File(fold, "config"), "account.json");
    public ModuleConfig moduleConfig;
    @Getter
    public boolean friststart;

    public ConfigManager() {
        if (!fold.exists()) {
            friststart = true;
            fold.mkdir();
        }
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!moduleConfigFile.exists()) {
            try {
                moduleConfigFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!accountFile.exists()) {
            try {
                accountFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        moduleConfig = new ModuleConfig("modules.json");
        configs.add(moduleConfig);
    }

    public void loadConfig(String name) {
        new Thread(() -> {
            File file = new File(dir, name);
            if (file.exists()) {
                Access.logger.info("Loading config: {}", name);
                for (Config config : configs) {
                    if (config.name.equals(name)) {
                        try {
                            config.load(new JsonParser().parse(new FileReader(file)).getAsJsonObject());
                            break;
                        } catch (FileNotFoundException e) {
                            Access.logger.error("Failed to load config: {}", name);
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            } else {
                Access.logger.error("Config {} doesn't exist, creating a new one...", name);
                saveConfig(name);
            }
        }).start();
    }

    public void saveConfig(String name) {
        new Thread(() -> {
            File file = new File(dir, name);
            try {
                Access.logger.info("Saving config: {}", name);
                file.createNewFile();
                for (Config config : configs) {
                    if (config.name.equals(name)) {
                        FileUtils.writeByteArrayToFile(file, gson.toJson(config.save()).getBytes(StandardCharsets.UTF_8));
                        break;
                    }
                }
            } catch (IOException e) {
                Access.logger.error("Failed to save config: {}", name);
            }
        }).start();
    }
}
