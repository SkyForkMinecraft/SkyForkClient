package com.skyfork.api.langya;

import com.skyfork.api.cedo.render.GLUtil;
import com.skyfork.api.cedo.render.RenderUtil;
import com.skyfork.api.cedo.MenuButton;
import com.skyfork.api.cedo.TextButton;
import com.skyfork.api.cedo.render.StencilUtil;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.cedo.shader.blur.GaussianBlur;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.langya.irc.IRCManager;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import com.skyfork.client.Access;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author LangYa466
 * @date 2024/3/16 19:42
 */

public class GuiLogin extends GuiScreen {

    private final List<MenuButton> buttons = new ArrayList() {{
        add(new MenuButton("Login"));
    }};
    InputField username = new InputField(true);
    InputField password = new InputField(false);
    private final List<InputField> inputs = new ArrayList() {{
        add(username);
        add(password);
    }};

    private final List<TextButton> textButtons = new ArrayList() {{
        add(new TextButton("GetHWID"));
    }};

    private final ResourceLocation backgroundResource = new ResourceLocation("client/funny.png");
    private final ResourceLocation blurredRect = new ResourceLocation("client/rect-test.png");

    @Override
    public void initGui() {
        buttons.forEach(MenuButton::initGui);
        inputs.forEach(InputField::initGui);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        width = sr.getScaledWidth();
        height = sr.getScaledHeight();


        GlStateManager.resetColor();
        RenderUtil.drawImage(backgroundResource, 0, 0, width, height);

        float rectWidth = 277;
        float rectHeight = 275.5f;

        GaussianBlur.startBlur();
        RoundedUtil.drawRound(width / 2f - rectWidth / 2f, height / 2f - rectHeight / 2f,
                rectWidth, rectHeight, 10, Color.WHITE);
        GaussianBlur.endBlur(40, 2);


        float outlineImgWidth = 688 / 2f;
        float outlineImgHeight = 681 / 2f;
        GLUtil.startBlend();
        RenderUtil.color(-1);
        RenderUtil.drawImage(blurredRect, width / 2f - outlineImgWidth / 2f, height / 2f - outlineImgHeight / 2f,
                outlineImgWidth, outlineImgHeight);

        GL11.glEnable(GL11.GL_BLEND);


        StencilUtil.initStencilToWrite();

        RenderUtil.setAlphaLimit(13);
        buttons.forEach(MenuButton::drawOutline);

        RenderUtil.setAlphaLimit(0);
        StencilUtil.readStencilBuffer(1);


        float circleW = 174 / 2f;
        float circleH = 140 / 2f;
        ResourceLocation rs = new ResourceLocation("client/circle-funny.png");
        mc.getTextureManager().bindTexture(rs);
        GLUtil.startBlend();
        RenderUtil.drawImage(rs, mouseX - circleW / 2f, mouseY - circleH / 2f, circleW, circleH);

        StencilUtil.uninitStencilBuffer();


        float buttonWidth = 140;
        float buttonHeight = 25;


        int count = 0;

        for (InputField inputField : inputs) {
            inputField.x = width / 2f - buttonWidth / 2f;
            inputField.y = ((height / 2f - buttonHeight / 2f) - 25) + count;
            inputField.width = buttonWidth;
            inputField.height = buttonHeight;
            inputField.drawScreen(mouseX, mouseY);
            count += buttonHeight + 5;
        }

        for (MenuButton button : buttons) {
            button.x = width / 2f - buttonWidth / 2f;
            button.y = ((height / 2f - buttonHeight / 2f) - 25) + count;
            button.width = buttonWidth;
            button.height = buttonHeight;
            button.clickAction = () -> {

                switch (button.text) {
                    case "Login":
                        Access.getInstance().getIrcManager().init();
                        IRCManager.out.println(String.format(".verify %s %s %s", username.text, password.text, HWIDUtil.getHWID()));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (IRCManager.inputLine == null) {
                            IRCManager.verified = false;
                            Access.displayTray("SkyFork", "登录失败!", TrayIcon.MessageType.ERROR);
                        } else {
                            IRCManager.verified = true;
                            Access.displayTray("SkyFork", "登录成功!", TrayIcon.MessageType.INFO);
                            mc.displayGuiScreen(new GuiMainMenu());
                        }
                }

                };
            button.drawScreen(mouseX, mouseY);
            count += buttonHeight + 5;
        }


        float buttonCount = 0;
        float buttonsWidth = (float) textButtons.stream().mapToDouble(TextButton::getWidth).sum();
        int buttonsSize = textButtons.size();
        buttonsWidth += FontManager.M16.getStringWidth(" | ") * (buttonsSize - 1);

        int buttonIncrement = 0;
        for (TextButton button : textButtons) {
            button.x = width / 2f - buttonsWidth / 2f + buttonCount;
            button.y = (height / 2f) + 120;
            switch (button.text) {
                case "GetHWID":
                    button.clickAction = () -> {
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        Transferable ts = new StringSelection(HWIDUtil.getHWID());
                        clipboard.setContents(ts, null);
                        Access.displayTray("SkyFork", "已复制机器码到剪切板", TrayIcon.MessageType.INFO);
                    };
                    break;
            }

            button.addToEnd = (buttonIncrement != (buttonsSize - 1));

            button.drawScreen(mouseX, mouseY);


            buttonCount += button.getWidth() + FontManager.M14.getStringWidth(" | ");
            buttonIncrement++;
        }

        FontManager.M50.drawCenteredGradientStringWithShadow(Access.CLIENT_NAME, width / 2f, height / 2f - 110);
        FontManager.M30.drawGradientString(Access.CLIENT_VERSION, width / 1.8f + FontManager.M50.getStringWidth(Access.CLIENT_NAME) / 2f - (FontManager.M50.getStringWidth(Access.CLIENT_NAME) / 2f), height / 2f - 120);
        FontManager.M18.drawCenteredString("edited by LangYa", width / 2f, height / 2f - 68, Color.WHITE.getRGB());

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        inputs.forEach(inputs -> inputs.keyTyped(typedChar, keyCode));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        textButtons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        inputs.forEach(inputs -> inputs.mouseClicked(mouseX, mouseY, mouseButton));
    }


}