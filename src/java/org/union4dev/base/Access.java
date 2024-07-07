package org.union4dev.base;

import cn.cedo.drag.DragManager;
import cn.chimera.command.CommandManager;
import cn.dxg.RotationUtil;
import cn.langya.MemoryManager;
import cn.langya.RankManager;
import cn.langya.TargetManager;
import cn.langya.elements.ElementManager;
import cn.langya.files.ConfigManager;
import cn.langya.irc.IRCManager;
import cn.langya.verify.Verify;
import cn.superskidder.ComboHandler;
import cn.yapeteam.notification.NotificationManager;
import de.florianmichael.viamcp.ViaMCP;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.gui.click.ClickGuiScreen;
import org.union4dev.base.module.ModuleManager;

import javax.imageio.ImageIO;
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

    public static final String CLIENT_VERSION = "4.1";
    public static String CLIENT_NAME = "SkyFork-Client";
    public static Color CLIENT_COLOR = new Color(205,189,255);
    public static boolean loaded;
    public static final Logger logger = LogManager.getLogger(CLIENT_NAME);
    public static final ResourceLocation logo = new ResourceLocation("client/logo.png");

    /**
     * Client Instance, access managers with this
     */
    private static Access INSTANCE;

    /**
     * ModuleManager Instance, access modules here
     * -- GETTER --
     *  Get module manager instance
     *
     * @return {@link ModuleManager}

     */
    @Getter
    private final ModuleManager moduleManager;

    /**
     * ConfigManager instance for access config
     */
    @Getter
    private final ConfigManager configManager;

    /**
     * ClickGui Instance
     * -- GETTER --
     *  Get ClickGui Instance
     *
     * @return {@link ClickGuiScreen}

     */
    @Getter
    @Setter
    private GuiScreen clickGui;

    @Getter
    private final ElementManager elementManager;
    @Getter
    private final IRCManager ircManager;
    @Getter
    private final NotificationManager notificationManager;
    @Getter
    private final CommandManager commandManager;
    @Getter
    private final DragManager dragManager;

    public static void displayTray(String title, String text, TrayIcon.MessageType type) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        try {
            tray.add(trayIcon);
            trayIcon.displayMessage(title, text, type);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }
    }

    /**
     * Entry point
     */
    @SneakyThrows
    public Access() {
        INSTANCE = this;

//        Verify.verify();

        // Initialize managers
        dragManager = new DragManager();
        dragManager.loadDragData();
        ircManager = new IRCManager();
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();
        configManager.getConfigs().forEach(config -> configManager.loadConfig(config.name));
        clickGui = new ClickGuiScreen();
        elementManager = new ElementManager();
        notificationManager = new NotificationManager();
        commandManager = new CommandManager();

        // new ClientMain("38.12.30.171", 11451).start();
        // Init ViaMCP
        try {
            ViaMCP.create();

            // In case you want a version slider like in the Minecraft options, you can use this code here, please choose one of those:

            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
            ViaMCP.INSTANCE.initAsyncSlider(5, 0, 110, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Finished Initialization
        Display.setTitle(CLIENT_NAME + " " + CLIENT_VERSION + " - " + Verify.user.getDisplayName());

        GuiScreen.d = new DynamicTexture(ImageIO.read(new URL(GuiScreen.url)));

        EventManager.register(new RankManager());
        EventManager.register(new TargetManager());
        EventManager.register(new MemoryManager());
        EventManager.register(new RotationUtil(0,0));
        EventManager.register(new ComboHandler());
        // EventManager.register(new PacketManager());

        loaded = true;
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
