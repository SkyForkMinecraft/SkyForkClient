package com.skyfork.api.langya.modules.client;

import com.skyfork.api.langya.utils.EncryptUtil;
import com.alibaba.fastjson.JSONObject;
import com.skyfork.api.tgformat.irc.ClientMain;
import net.minecraft.client.Minecraft;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.misc.ChatEvent;
import com.skyfork.client.events.update.UpdateEvent;
import com.skyfork.client.util.ChatUtil;

import java.io.*;
import java.util.Objects;

/**
 * @author LangYa466
 * @since 2024/5/6 19:58
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
