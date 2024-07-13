package cn.langya.irc;

import anti_leak.Native;
import net.minecraft.client.gui.GuiMainMenu;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.update.TickEvent;
import org.union4dev.base.util.ChatUtil;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Native
public class IRCManager implements Access.InstanceAccess {

    public static BufferedReader in = null;
    public static PrintWriter out = null;
    public static boolean verified = false;
    public static String inputLine;
    public IRCManager() {
    }

    @NativeObfuscation
    public void init() {
        if (true) return;

            String hostName = "socket.skyclient.lol";
            int portNumber = 520;

            Socket echoSocket;
            try {
                echoSocket = new Socket(hostName, portNumber);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                out = new PrintWriter(echoSocket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        new Thread(() -> {
            try {
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("MESSAGE")) {
                        ChatUtil.info("[IRC] " + inputLine.replace("MESSAGE",""));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

      //  EventManager.register(this);
    }


}