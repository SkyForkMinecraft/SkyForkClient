package soar;

import java.awt.Color;

import cn.cedo.drag.Dragging;
import cn.cedo.shader.RoundedUtil;

import cn.langya.font.FontManager;
import net.minecraft.client.Minecraft;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;

public class ClientInfoMod implements Access.InstanceAccess {

	private final Dragging drag = Access.getInstance().getDragManager().createDrag(this.getClass(),"clientinfo", 5, 5);

	@EventTarget
	public void onRender2D(Render2DEvent event) {

		String text = "SkyFork | " + Minecraft.getDebugFPS() + "fps | " + mc.thePlayer.getName();
		RoundedUtil.drawRound(drag.getXPos(), drag.getYPos(), drag.getWidth() - drag.getXPos(), drag.getHeight() - drag.getYPos(), 2, new Color(0, 0, 0, 80));

		FontManager.M20.drawString(text, drag.getXPos() + 4, drag.getYPos() + 2, -1);

		drag.setWidth(FontManager.M20.getStringWidth(text) + 8);
		drag.setHeight(8 + (FontManager.M20.getHeight()));
	}

}
