package cn.chimera.command;


import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.misc.ChatEvent;
import org.union4dev.base.util.ChatUtil;

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
