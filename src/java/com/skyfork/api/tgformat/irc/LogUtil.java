package com.skyfork.api.tgformat.irc;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

/**
 * @author TG_format
 * @since 2024/5/25 14:19
 */
public class LogUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String prefix = "[" + "SkyForkClient" + "]";

    public static void print(Object message) {
        System.out.println(prefix + " " + message);
    }

    public static void addChatMessage(String message) {
        if(mc.thePlayer == null)return;
        mc.thePlayer.addChatMessage(new ChatComponentText(prefix + " " + message));
    }
    public static void addIRCMessage(String message) {
        if(mc.thePlayer == null)return;
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

}
