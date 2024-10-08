package com.skyfork.client.module;
import com.google.gson.*;
import com.skyfork.api.cedo.ScoreboardMod;
import com.skyfork.api.cedo.render.targethud.TargetHUDMod;
import com.skyfork.api.dxg.CheaterDetector;
import com.skyfork.api.dxg.Protocol;
import com.skyfork.api.imflowow.ChunkAnimator;
import com.skyfork.api.imflowow.EnchantEffect;
import com.skyfork.api.imflowow.LowFire;
import com.skyfork.api.langya.elements.impls.*;
import com.skyfork.api.langya.modules.client.*;
import com.skyfork.api.langya.modules.misc.*;
import com.skyfork.api.langya.modules.render.MotionBlur;
import com.skyfork.api.starx.MoBends;
import com.skyfork.api.starx.SkinLayers3D;
import com.skyfork.api.starx.WaveyCapes;
import com.skyfork.api.superskidder.BlockOverlay;
import com.skyfork.api.superskidder.modules.Combo;
import com.skyfork.api.superskidder.modules.OldAnimation;
import com.skyfork.api.superskidder.modules.TNTTimer;
import com.skyfork.api.tgformat.ESP;
import com.skyfork.client.Access;
import net.minecraft.util.EnumChatFormatting;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.annotations.module.*;
import com.skyfork.client.events.EventManager;
import com.skyfork.client.events.misc.KeyInputEvent;
import com.skyfork.client.module.handlers.*;
import com.skyfork.client.module.movement.*;
import com.skyfork.client.module.render.*;
import com.skyfork.client.value.AbstractValue;
import com.skyfork.client.value.impl.*;
import com.skyfork.api.canelex.DragonWings;
import com.skyfork.api.soar.ArmorStatusMod;
import com.skyfork.api.soar.GuiClickEffect;
import com.skyfork.api.soar.InventoryDisplayMod;
import com.skyfork.api.soar.PackDisplayMod;
import org.apache.commons.io.FileUtils;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Module Manager, Manage and access module.
 *
 * @author cubk
 */
public final class ModuleManager {
    /**
     * Index Maps
     */
    private final HashMap<Class<?>, ModuleHandle> modules = new HashMap<>();
    private final HashMap<String, Class<?>> nameMap = new HashMap<>();
    private final HashMap<Object, Class<?>> objectMap = new HashMap<>();

    /**
     * Initialize modules
     */
    public ModuleManager() {
        init();
    }

    @NativeObfuscation
    private void init() {
        // Register Event
        EventManager.register(this);

        // Register Movement
        register(Sprint.class, "自动疾跑", Category.Movement);

        // Register Render
        register(KeyStore.class,"按键显示",Category.Render);
        // register(TargetHUDMod.class,"PVP信息",Category.Render);
        register(ClientLogo.class,"客户端标识",Category.Render);
        register(com.skyfork.api.langya.elements.impls.ArrayList.class,"功能列表",Category.Render);
        register(FPSInfo.class,"帧率显示",Category.Render);
        register(PotionInfo.class,"药水显示",Category.Render);
        register(BlockOverlay.class,"填充方块",Category.Render);
        register(TNTTimer.class,"TNT时间",Category.Render);
        register(CPSInfo.class,"CPS显示",Category.Render);
        register(MotionBlur.class,"动态模糊",Category.Render);
        // register(MusicPlayerOverlay.class,"音乐歌词",Category.Render);
        register(AttackCircle.class,"攻击距离光环",Category.Render);
        register(TargetCircle.class,"目标光环",Category.Render);
        register(Combo.class,"连击显示",Category.Render);
        register(TargetHUDMod.class,"目标显示",Category.Render);
        // register(ClientInfoMod.class,"客户端信息",Category.Render);
        register(ESP.class,"自定义碰撞箱",Category.Render);
        register(ArmorStatusMod.class,"装备显示",Category.Render);
        register(InventoryDisplayMod.class,"背包显示",Category.Render);
        register(PackDisplayMod.class,"材质包显示",Category.Render);
        register(WaveyCapes.class,"真实的披风",Category.Render);
        register(MoBends.class,"真实的动作",Category.Render);
        register(SkinLayers3D.class,"真实的皮肤",Category.Render);

        // Register Misc
        register(MoreParticles.class, "更多攻击粒子", Category.Misc);
        register(CustomWorldTime.class, "自定义世界时间", Category.Misc);
        register(AttackParticles.class, "自定义攻击粒子", Category.Misc);
        register(KillEffectsMod.class,"击杀特效",Category.Misc);
        register(CheaterDetector.class,"外挂检测",Category.Misc);
        register(Fulbright.class,"夜视",Category.Misc);
        register(ChunkAnimator.class,"区块加载动画",Category.Misc);

        // Register Client
        register(ClientSettings.class,"客户端设置",Category.Render);
        register(ClickGui.class,"功能管理页面",Category.Render);
        register(HUD.class, "页面", Category.Client);
        register(CustomHotbar.class,"自定义背包",Category.Client);
        register(Cape.class,"自定义披风",Category.Client);
        register(NoHurtCam.class,"无受伤抖动",Category.Client);
        register(DragonWings.class,"龙翼",Category.Client);
        register(OldAnimation.class,"自定义动画",Category.Client);
        register(ItemPhysic.class,"物品物理掉落",Category.Client);
        register(NoHitClick.class,"无打击延迟",Category.Client);
        register(ScoreboardMod.class,"自定义记分版",Category.Client);
        register(GuiClickEffect.class,"页面点击特效",Category.Client);
        register(Protocol.class,"花雨庭协议",Category.Client);
        register(LowFire.class,"低火",Category.Client);
        register(EnchantEffect.class,"自定义附魔颜色",Category.Client);
        //   register(cn.langya.irc.IRC.class,"客户端聊天",Category.Client);

    }

    @EventTarget
    public void onKey(KeyInputEvent event) {
        for (Class<?> module : modules.keySet())
            if (getKey(module) == event.getKey())
                toggle(module);
    }

    /**
     * Register a module to manager
     *
     * @param clazz    Module Class
     * @param name     Name
     * @param category Module Category, see {@link Category}
     */
    private void register(Class<?> clazz, String name, Category category) {
        try {
            Object instance = clazz.newInstance();
            ModuleHandle module = new ModuleHandle(name, category, instance);

            nameMap.put(name.toLowerCase(), clazz);
            objectMap.put(instance, clazz);

            if (clazz.isAnnotationPresent(Startup.class)) {
                module.setEnable(true);
            }

            if (clazz.isAnnotationPresent(Binding.class)) {
                Binding binding = clazz.getAnnotation(Binding.class);
                module.setKey(binding.value());
            }

            for (final Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    final Object obj = field.get(instance);
                    if (obj instanceof AbstractValue<?>) {
                        module.getValues().add((AbstractValue<?>) obj);
                        sortValue(module);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            this.modules.put(clazz, module);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Register sub module
     *
     * @param bigFather 大爹
     * @param clazz     SubModule Class
     * @param name      SubModule Name
     * @return {@link SubModuleHandle}
     */
    SubModuleHandle registerSub(ModuleHandle bigFather,Class<?> clazz, String name) {
        try {
            Object instance = clazz.newInstance();
            SubModuleHandle module = new SubModuleHandle(bigFather,name, instance);

            for (final Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    final Object obj = field.get(instance);
                    if (obj instanceof AbstractValue<?>) {
                        AbstractValue<?> value = (AbstractValue<?>) obj;
                        value.addSupplier(() -> !bigFather.isEnabled());
                        bigFather.getValues().add(value);
                        sortValue(bigFather);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return module;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all modules in a category
     *
     * @param category Target Category
     * @return {@link List}<{@link Class}<{@link ?}>>
     */
    public List<Class<?>> getModulesByCategory(Category category) {
        ArrayList<Class<?>> mods = new ArrayList<>();
        for(Class<?> module : getModules()) {
            if(getCategory(module) == category)
                mods.add(module);
        }
        return mods;
    }

    /**
     * Get all modules in a category
     *
     * @param category Target Category
     * @return {@link List}<{@link Class}<{@link ?}>>
     */
    public List<ModuleHandle> getCModulesByCategory(Category category) {
        ArrayList<ModuleHandle> mods = new ArrayList<>();
        for(ModuleHandle module : getCModules().values()) {
            if(getCategory(module.getClass()) == category)
                mods.add(module);
        }
        return mods;
    }

    /**
     * Get module category
     *
     * @param module Module Class
     * @return {@link Category}
     */
    public Category getCategory(Class<?> module){
        return modules.get(module).getCategory();
    }

    /**
     * sort value
     *
     * @param mod module handle
     */
    private void sortValue(ModuleHandle mod) {
        ArrayList<AbstractValue<?>> sorted = new ArrayList<>();
        ArrayList<AbstractValue<?>> values = mod.getValues();

        for (AbstractValue<?> v : values) {
            if (v instanceof ComboValue)
                sorted.add(v);
        }
        for (AbstractValue<?> v : values) {
            if (v instanceof NumberValue)
                sorted.add(v);
        }

        for (AbstractValue<?> v : values) {
            if (v instanceof BooleanValue)
                sorted.add(v);
        }

        mod.setValues(sorted);
    }

    /**
     * Get a module class by name
     *
     * @param name Module Name
     * @return {@link Class}<{@link ?}>
     */
    public Class<?> getModuleClass(String name) {
        return nameMap.get(name.toLowerCase());
    }

    /**
     * Toggle module state
     *
     * @param module module class
     */
    public boolean toggle(Class<?> module) {
        modules.get(module).setEnable(!modules.get(module).isEnabled());
        return modules.get(module).isEnabled();
    }

    /**
     * Get a module name
     *
     * @param module Module Class
     * @return {@link String}
     */
    public String getName(Class<?> module) {
        return modules.get(module).getName();
    }


    /**
     * Get a module key bind
     *
     * @param module module class
     * @return key bind code
     */
    public int getKey(Class<?> module) {
        return modules.get(module).getKey();
    }

    /**
     * Set a module key bind
     *
     * @param module module class
     * @param key    key bind code
     */
    public void setKey(Class<?> module, int key) {
        modules.get(module).setKey(key);
    }

    /**
     * Get all module classes
     *
     * @return {@link Set}<{@link Class}<{@link ?}>>
     */
    public List<Class<?>> getModules() {
        return new ArrayList<>(modules.keySet());
    }

    /**
     * Get all module classes
     *
     * @return {@link Set}<{@link Class}<{@link ?}>>
     */
    public HashMap<Class<?>, ModuleHandle> getCModules() {
        return modules;
    }

    /**
     * format module to array display (+suffix)
     *
     * @param module module class
     * @return {@link String}
     */
    public String format(Class<?> module) {
        ModuleHandle m = modules.get(module);
        return m.getSuffix().isEmpty() ? m.getName() : String.format("%s %s%s", m.getName(), EnumChatFormatting.GRAY, m.getSuffix());
    }

    /**
     * Get handle
     *
     * @param module module class
     * @return {@link ModuleHandle}
     */
    public ModuleHandle getHandle(Class<?> module) {
        return modules.get(module);
    }

    /**
     * Get handle
     *
     * @param object module object
     * @return {@link ModuleHandle}
     */
    public ModuleHandle getHandle(Object object) {
        return modules.get(objectMap.get(object));
    }

    // langya
    public ModuleHandle getModule(ModuleHandle moduleHandle) { return modules.get(moduleHandle); }

    /**
     * Get all values for module
     *
     * @param module Module Class
     * @return {@link Iterable}<{@link AbstractValue}<{@link ?}>>
     */
    public Iterable<AbstractValue<?>> getValues(Class<?> module) {
        return new ArrayList<>(modules.get(module).getValues());
    }

    /**
     * Module has values?
     *
     * @param module Module Class
     * @return Has
     */
    public boolean hasValue(Class<?> module) {
        return !modules.get(module).getValues().isEmpty();
    }

    /**
     * Get a module enable status
     *
     * @param module Module Class
     * @return Status
     */
    public boolean isEnabled(Class<?> module) {
        return modules.get(module).isEnabled();
    }

    /**
     * Get a module visible status
     *
     * @param module Module Class
     * @return Status
     */
    public boolean isVisible(Class<?> module) {
        return modules.get(module).isVisible();
    }

    /**
     * Set a module enable status
     *
     * @param module Module Class
     * @param state  Status
     */
    public void setEnable(Class<?> module, boolean state) {
        this.modules.get(module).setEnable(state);
    }


    /**
     * Set a module visible status
     *
     * @param module Module Class
     * @param state  Status
     */
    public void setVisible(Class<?> module, boolean state) {
        this.modules.get(module).setVisible(state);
    }

    private static final File MODULE_DATA = new File(Access.DIRECTORY, "module.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonObject save() {
        JsonObject object = new JsonObject();
        for (ModuleHandle module : modules.values()){
            JsonObject moduleObj = new JsonObject();
            moduleObj.addProperty("Enable",module.isEnabled());
            moduleObj.addProperty("KeyCode",module.getKey());
            JsonObject valueObj = new JsonObject();
            moduleObj.add("Values",valueObj);
            for (AbstractValue<?> value : module.getValues()){
                if (value instanceof NumberValue) {
                    valueObj.addProperty(value.getName(), ((NumberValue) value).getValue());
                } else if (value instanceof BooleanValue) {
                    valueObj.addProperty(value.getName(), ((BooleanValue) value).getValue());
                } else if (value instanceof ComboValue) {
                    valueObj.addProperty(value.getName(), ((ComboValue) value).getValue());
                } else if (value instanceof ColorValue) {
                    valueObj.addProperty(value.getName(), ((ColorValue) value).getValue().getRGB());
                    //  valueObj.add(value.getName(), ((ColorValue) value).getRainbow().getJsonObject());
                }
            }
            object.add(module.getName(),moduleObj);
        }
        return object;
    }

    public void load(JsonObject object) {
        for (ModuleHandle module : modules.values()) {
            if (object.has(module.getName())) {
                JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();
                if (moduleObject.has("Enable")) {
                    module.setEnable(moduleObject.get("Enable").getAsBoolean());
                }
                if (moduleObject.has("KeyCode")) {
                    module.setKey(moduleObject.get("KeyCode").getAsInt());
                }
                if (moduleObject.has("Values")) {
                    JsonObject valuesObject = moduleObject.get("Values").getAsJsonObject();
                    for (AbstractValue<?> value : module.getValues()) {
                        if (valuesObject.has(value.getName())) {
                            JsonElement theValue = valuesObject.get(value.getName());
                            if (value instanceof NumberValue) {
                                ((NumberValue) value).setValue(theValue.getAsNumber().doubleValue());
                            } else if (value instanceof BooleanValue) {
                                ((BooleanValue) value).setValue(theValue.getAsBoolean());
                            } else if (value instanceof ComboValue) {
                                ((ComboValue) value).setValue(theValue.getAsString());
                            } else if (value instanceof ColorValue) {
                                ((ColorValue) value).setValue(new Color(theValue.getAsInt()));
                            }
                        }
                    }
                }
            }
        }
    }

    public void loadConfig(String name) {
        if (MODULE_DATA.exists()) {
            System.out.println("Loading config: " + name);
            try {
                load(new JsonParser().parse(new FileReader(MODULE_DATA)).getAsJsonObject());
            } catch (FileNotFoundException e) {
                System.out.println("Failed to load config: " + name);
                e.printStackTrace();
            }
        } else {
            System.out.println("Config " + name + " doesn't exist, creating a new one...");
            saveConfig("module.json");
        }
    }
    public void saveConfig(String name) {
        try {
            System.out.println("Saving config: " + name);
            MODULE_DATA.createNewFile();
            FileUtils.writeByteArrayToFile(MODULE_DATA, gson.toJson(save()).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Failed to save config: " + name);
        }
    }

    public AbstractValue getSetting(String moduleName, String valueName) {
        for (ModuleHandle value : getCModules().values()) {
            if (value.getName() != moduleName) continue;
            for (AbstractValue<?> valueValue : value.getValues()) {
                if (valueValue.getName() == valueName) {
                    return valueValue;
                }
            }
        }
        return null;
    }
}
