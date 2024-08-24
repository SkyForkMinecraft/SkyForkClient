package com.skyfork.client;

import com.skyfork.api.cedo.drag.DragManager;
import com.skyfork.api.chimera.command.CommandManager;
import com.skyfork.api.dxg.config.ConfigManager;
import com.skyfork.api.dxg.hytprotocol.packet.VexViewPacket;
import com.skyfork.api.langya.MemoryManager;
import com.skyfork.api.langya.RankManager;
import com.skyfork.api.langya.TargetManager;
import com.skyfork.api.langya.irc.IRCManager;
import com.skyfork.api.langya.verify.Verify;
import com.skyfork.api.superskidder.ComboHandler;
import com.skyfork.api.yapeteam.notification.NotificationManager;
import com.skyfork.api.florianmichael.viamcp.ViaMCP;
import com.yumegod.obfuscation.Rename;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import com.skyfork.client.events.EventManager;
import com.skyfork.client.gui.click.ClickGuiScreen;
import com.skyfork.client.module.ModuleManager;
import com.skyfork.api.soar.account.AccountManager;

import java.awt.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Client Entry
 * The main class where the client is loaded up.
 * Anything related to the client will start from here and managers etc instances will be stored in this class.
 *
 * @author cubk
 */
@Rename
public final class Access {

    public static final String CLIENT_VERSION = "4.7.8";
    public static final File DIRECTORY = new File(Minecraft.getMinecraft().mcDataDir, "SkyForkClient");
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
    private ModuleManager moduleManager;

    @Getter
    private VexViewPacket vexviewPacket;
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
    private IRCManager ircManager;
    @Getter
    private NotificationManager notificationManager;
    @Getter
    private CommandManager commandManager;
    @Getter
    private DragManager dragManager;
    @Getter
    private AccountManager accountManager;

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
        init();

//        Verify.verify();

    }

    @com.yumegod.obfuscation.Native
    public void init() {
        if (!DIRECTORY.exists()) DIRECTORY.mkdir();
        Verify.verify();
        dragManager = new DragManager();
        moduleManager = new ModuleManager();
        Access.getInstance().getModuleManager().loadConfig("module.json");
        clickGui = new ClickGuiScreen();
        notificationManager = new NotificationManager();
        commandManager = new CommandManager();
        accountManager = new AccountManager();
        accountManager.load();

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

        /*
        try {
            GuiScreen.d = new DynamicTexture(ImageIO.read(new URL(GuiScreen.url)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */

        EventManager.register(new RankManager());
        EventManager.register(new TargetManager());
        EventManager.register(new MemoryManager());
        EventManager.register(new ComboHandler());
        // EventManager.register(new PacketManager());

        ircManager = new IRCManager();

        vexviewPacket = new VexViewPacket();

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
