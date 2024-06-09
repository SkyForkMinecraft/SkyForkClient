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

	private static boolean init;

	@IRCEvent
	public static void onMessage(String message) {
		ChatUtil.info(String.format("[IRC] %s",message));
	}

	@EventTarget
	public static void onChatSendMessage(ChatEvent e) {
		IRCClient client = null;
		if (!init) {
			client = new IRCClient("ws://218.93.206.109:79", "skyffffffffffffffffork");;
			IRCClient.registerClass(IRC.class);
			init = true;
		}

		if (client == null) {
			ChatUtil.info("[IRC] 连接失败!!!");
			return;
		}

		if (e.getMessage().startsWith("-")) {
			e.setCancelled(true);
			ChatUtil.success("[IRC] 消息发送成功!");
			client.sendMessage(mc.thePlayer.getDisplayName() + e.getMessage().replace("-"," : "));
		}

	}

	public IRC() {
		init = false;
	}


}