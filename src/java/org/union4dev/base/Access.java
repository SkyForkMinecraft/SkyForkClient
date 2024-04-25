package org.union4dev.base;

import cn.langya.elements.ElementManager;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.union4dev.base.command.CommandManager;

import org.union4dev.base.gui.click.ClickGuiScreen;
import org.union4dev.base.gui.font.FontManager;
import org.union4dev.base.module.ModuleManager;
import unknow.WebUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Client Entry
 * The main class where the client is loaded up.
 * Anything related to the client will start from here and managers etc instances will be stored in this class.
 *
 * @author cubk
 */
public final class Access {

    public static String CLIENT_NAME = "SkyClient-Fork";

    /**
     * Client Instance, access managers with this
     */
    private static Access INSTANCE;

    /**
     * ModuleManager Instance, access modules here
     */
    private final ModuleManager moduleManager;

    /**
     * CommandManager Instance, access commands here
     */
    private final CommandManager commandManager;

    /**
     * ClickGui Instance
     */
    private final ClickGuiScreen clickGui;

    /**
     * Font renderers
     */
    private final FontManager fontManager;

    @Getter
    private ElementManager elementManager;

    /**
     * Entry point
     */
    @SneakyThrows
    public Access() {
        INSTANCE = this;

        Display.setTitle(CLIENT_NAME + "Loading...");

        if (!WebUtils.get("https://gitee.com/langya1337/sky-client/raw/master/version.txt").contains("1.0")) {
            Desktop.getDesktop().browse(new URL("https://qm.qq.com/q/qH7jTDrJcI").toURI());
            Runtime.getRuntime().exit(0);
        }

        // Initialize managers
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        fontManager = new FontManager();
        clickGui = new ClickGuiScreen();
        elementManager = new ElementManager();

        // Finished Initialization
        Display.setTitle(CLIENT_NAME);
    }


    /**
     * Get client instance
     *
     * @return
     */
    public static Access getInstance() {
        return INSTANCE;
    }

    /**
     * Get module manager instance
     *
     * @return {@link ModuleManager}
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    /**
     * Get command manager instance
     *
     * @return {@link CommandManager}
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Get ClickGui Instance
     *
     * @return {@link ClickGuiScreen}
     */
    public ClickGuiScreen getClickGui() {
        return clickGui;
    }

    /**
     * Get FontManager Instance
     *
     * @return {@link FontManager}
     */
    public FontManager getFontManager() {
        return fontManager;
    }


    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainsChinese(String str) {
        if (str == null) return false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * Implement this class for access instances
     *
     * @author cubk
     */
    public interface InstanceAccess {
        Minecraft mc = Minecraft.getMinecraft();
        Access access = getInstance();

        default void setSuffix(String suffix, Object object) {
            access.getModuleManager().getHandle(object).setSuffix(suffix);
        }

        default void setEnable(Object object, boolean state) {
            access.getModuleManager().getHandle(object).setEnable(state);
        }

        default void setEnable(Class<?> module, boolean state) {
            access.getModuleManager().setEnable(module, state);
        }

        default boolean isEnabled(Class<?> module) {
            return access.getModuleManager().isEnabled(module);
        }
    }

}
