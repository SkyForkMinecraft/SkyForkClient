package cn.langya.modules.client;

import cn.langya.utils.EncryptUtil;
import com.alibaba.fastjson.JSONObject;
import lol.tgformat.irc.ClientMain;
import net.minecraft.client.Minecraft;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.misc.ChatEvent;
import org.union4dev.base.events.update.UpdateEvent;
import org.union4dev.base.util.ChatUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author LangYa466
 * @date 2024/5/6 19:58
 */

public class IRC {
    private final Minecraft mc = Minecraft.getMinecraft();
    private String lastUserName = "";
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (!Objects.equals(lastUserName, mc.thePlayer.getDisplayName().getUnformattedText())){
            lastUserName = mc.thePlayer.getDisplayName().getUnformattedText();
            JSONObject ign = new JSONObject();
            ign.put("type", "IGN");
            ign.put("data", lastUserName);
            send(ign.toJSONString());
        }
    }
    @EventTarget
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith(".irc")) {
            event.setCancelled(true);
            JSONObject msg = new JSONObject();
            msg.put("type", "msg");
            msg.put("data", lastUserName + ": " + event.getMessage().substring(4));
            send(msg.toJSONString());
        }
    }
    private void send(Object obj) {
        try {
            ClientMain.sendObject(obj);
        } catch (IOException e) {
            ChatUtil.info(" ERROR: " + EncryptUtil.decrypt(e.getMessage()));
        }
    }


}
