package cn.langya.irc;

import com.yumegod.obfuscation.Native;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.misc.ChatEvent;
import org.union4dev.base.util.ChatUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Native
@Startup
public class IRC implements Access.InstanceAccess {

	public static List<String> userList = new CopyOnWriteArrayList<>();


	@EventTarget
	private void onCC(ChatEvent e) {
		/*
		if (e.getMessage().startsWith("/")) return;

		if (e.getMessage().startsWith("-")) {
			if (e.getMessage().replace("-","") == "") return;
			e.setCancelled(true);
			try (Socket socket = new Socket("61.147.247.11",11451)) {
				OutputStream output = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);

				writer.println("SkyForkIIIRRRCCC___" + mc.thePlayer.getDisplayName().getFormattedText() + e.getMessage().replace("-", " : "));
				// ChatUtil.success("[IRC]" + mc.thePlayer.getDisplayName().getFormattedText() + e.getMessage().replace("-", " : "));
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}


		 */

	}


}