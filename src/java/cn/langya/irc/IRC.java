package cn.langya.irc;

import com.yumegod.simpleirc.IRCClient;
import com.yumegod.simpleirc.IRCEvent;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.misc.ChatEvent;
import org.union4dev.base.util.ChatUtil;


@Startup
public class IRC implements Access.InstanceAccess {

    @IRCEvent
    public static void onMessage(String message) {
        ChatUtil.info("[IRC] " + message);
    }

    private boolean loaded;
    @EventTarget
    public void onC(ChatEvent event) {
        IRCClient client = null;
        if (!loaded) {
            client = new IRCClient("ws://222.186.160.201:45808", "SkyForkPassWord");
            IRCClient.registerClass(this.getClass());
            loaded = true;
        }
        if (client == null) {
            loaded = false;
        }
        if (event.getMessage().startsWith("-") && event.getMessage().replace("-","") != "" && mc.thePlayer != null) {
            client.sendMessage(String.format("%s : %s",mc.thePlayer.getDisplayName().getFormattedText(),event.getMessage().replace("-","")));
        }
    }

}