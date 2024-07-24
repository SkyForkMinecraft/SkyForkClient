package com.skyfork.api.langya.irc;

import com.skyfork.client.Access;
import com.skyfork.client.util.ChatUtil;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IRCManager implements Access.InstanceAccess {

    public static BufferedReader in = null;
    public static PrintWriter out = null;
    public static boolean verified = false;
    public static String inputLine;
    public IRCManager() {
    }

    @com.yumegod.obfuscation.Native
    @NativeObfuscation
    public void init() {
        if (true) return;

            String hostName = "?";
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