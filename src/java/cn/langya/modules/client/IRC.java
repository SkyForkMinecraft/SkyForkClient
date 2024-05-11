package cn.langya.modules.client;

import cn.langya.utils.EncryptUtil;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.update.UpdateEvent;
import org.union4dev.base.util.ChatUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author LangYa466
 * @date 2024/5/6 19:58
 */

public class IRC {
    private static PrintWriter out;
    private static boolean inited;

    @Enable
    static void enable() {
        init();
    }

    public static void init() {
        try {
            Socket socket = new Socket("127.0.0.1", 466);
            ChatUtil.success(" [IRC] 连接成功");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        ChatUtil.info(" [IRC] 有新的消息: " + EncryptUtil.decrypt(message));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inited = true;
    }

    public static boolean checkMessage(String message) {
        if(!inited) init();

        if(message.startsWith(".irc") && !message.replace(".irc ","").isEmpty()) {
            out.println(EncryptUtil.encrypt(message.replace(".irc ","")));
            ChatUtil.success(String.format(" [IRC] 消息 [%s] 发送成功 ",message.replace(".irc ","")));
            return true;
        } else {
            return false;
        }
    }

}
