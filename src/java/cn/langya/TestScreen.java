package cn.langya;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import cn.langya.font.FontManager;
import skid.cedo.render.RenderUtil;

import javax.swing.*;

/**
 * @author LangYa466
 * @date 2024/5/9 16:59
 */

public class TestScreen extends GuiScreen {

    private boolean join;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        double startX = width / 2.0 -  FontManager.M50.getStringWidth("Skyfork") / 2.0;
        double startY = height / 3.5;
        FontManager.M50.drawStringWithShadow("Skyfork", (float) startX, (float) startY, -1);
        if (join) {
            RenderUtil.drawLoadingCircle(width / 2F, height / 4F + 70);
            JOptionPane.showInputDialog("验证没写完");
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "814478.skyclient.lol", 25565));
        }
    }

}
