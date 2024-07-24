package com.skyfork.api.langya;

import com.skyfork.api.langya.font.FontManager;
import net.minecraft.client.gui.GuiScreen;
import com.skyfork.api.cedo.render.RenderUtil;

import javax.swing.*;

/**
 * @author LangYa466
 * @since 2024/5/9 16:59
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
        }
    }

}
