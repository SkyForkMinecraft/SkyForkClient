package cn.langya.screen;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class GuiCustomBackground extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 绘制背景
        drawClientBackground();
        buttonList.add(new GuiButton(114514,50,50,"自定义壁纸（填API）"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 114514)
        {
            url = JOptionPane.showInputDialog("输入您的获取壁纸api");   {
            try {
                d = new DynamicTexture(ImageIO.read(new URL(url)));
            } catch (IOException ignored) {
            }
        }

        }
    }
}
