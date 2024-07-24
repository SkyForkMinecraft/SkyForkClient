package com.skyfork.api.langya.irc;

import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.annotations.module.Startup;
import com.skyfork.client.events.misc.ChatEvent;


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