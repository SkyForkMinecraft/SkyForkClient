package com.skyfork.api.langya.screen;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class GuiCustomBackground extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawClientBackground();
    }


    @Override
    public void initGui() {
        {
            url = JOptionPane.showInputDialog("输入您的获取壁纸api");
            {
                try {
                    d = new DynamicTexture(ImageIO.read(new URL(url)));
                } catch (IOException ignored) {
                }
            }
        }
    }
}
