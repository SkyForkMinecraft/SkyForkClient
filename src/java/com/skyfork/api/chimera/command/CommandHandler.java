package com.skyfork.api.chimera.command;


import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.misc.ChatEvent;
import com.skyfork.client.util.ChatUtil;

/**
 * @author chimera
 */
public class CommandHandler {
    private final String prefix = ".";

    @EventTarget
    private void onChat(ChatEvent event) {
        String rawMessage = event.getMessage();
        if (!rawMessage.startsWith(this.prefix)) {
            return;
        } else {
            event.setCancelled(true);
        }

        boolean safe = rawMessage.length() > 1;
        if (safe) {
            String beheaded = rawMessage.substring(1);
            String[] args = beheaded.split(" ");
            Command command = Access.getInstance().getCommandManager().getCommand(args[0]);

            if (command != null) {
                if (!command.run(args)) {
                    ChatUtil.info(command.usage());
                }
            } else {
                ChatUtil.info("请输入 .help");
            }
        } else {
            ChatUtil.info("请输入 .help");
        }
    }



}
