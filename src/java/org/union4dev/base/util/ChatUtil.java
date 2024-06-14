package org.union4dev.base.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.union4dev.base.Access;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ChatUtil {
    public static final String PRIMARY_COLOR = EnumChatFormatting.BLUE.toString();
    public static final String SECONDARY_COLOR = EnumChatFormatting.GRAY.toString();
    private static final String PREFIX = PRIMARY_COLOR + "[" + SECONDARY_COLOR + Access.CLIENT_NAME + PRIMARY_COLOR + "] ";

    private static void send(final String message) {
        if (Minecraft.getMinecraft().thePlayer == null) return;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void success(String s) {
        info(s);
    }

    public static void info(String s) {
        send(PREFIX + s);
    }
}
