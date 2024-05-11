package cn.langya;

import cn.langya.utils.MouseUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.union4dev.base.gui.font.FontManager;
import skid.cedo.render.RenderUtil;

import javax.swing.*;
import java.io.IOException;

/**
 * @author LangYa466
 * @date 2024/5/9 16:59
 */

public class GuiHuaYuTing extends GuiScreen {

    private final BetterButton joinButton = new BetterButton("Join HuaYuTing", width / 2.0F - 65, width / 2.0 -  FontManager.F50.getWidth("Skyfork") / 2.0 + FontManager.F50.getHeight() * 2.0F, FontManager.F18);
    private boolean join;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        double startX = width / 2.0 -  FontManager.F50.getWidth("Skyfork") / 2.0;
        double startY = height / 3.5;
        FontManager.F50.drawStringWithShadow("Skyfork", startX,startY, -1);
        joinButton.draw();
        joinButton.isHover = MouseUtil.isHovering(joinButton.x,joinButton.y,joinButton.width,joinButton.height,mouseX,mouseY);
        if (join) {
            RenderUtil.drawLoadingCircle(width / 2F, height / 4F + 70);
            JOptionPane.showInputDialog("验证没写完");
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "814478.skyclient.lol", 25565));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (joinButton.isHover && mouseButton == 0) {
            join = true;
        }
    }
}
