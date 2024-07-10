package cn.langya.irc;

import anti_leak.Native;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.misc.ChatEvent;


@Native
@Startup
public class IRC implements Access.InstanceAccess {

    @EventTarget
    public void onC(ChatEvent event) {
        if (event.getMessage().startsWith("-") && event.getMessage().replace("-","") != "" && mc.thePlayer != null) {
          IRCManager.out.println("MESSAGE" + event.getMessage().replace("-",""));
          event.setCancelled(true);
        }
    }

}