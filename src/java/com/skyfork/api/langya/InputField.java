package com.skyfork.api.langya;


import com.skyfork.api.cedo.animations.Animation;
import com.skyfork.api.cedo.animations.Direction;
import com.skyfork.api.cedo.animations.impl.DecelerateAnimation;
import com.skyfork.api.cedo.render.RenderUtil;
import com.skyfork.api.dxg.Screen;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.langya.utils.MouseUtil;
import net.minecraft.util.ResourceLocation;

public class InputField implements Screen {

    public String text = "";
    private Animation hoverAnimation;
    public float x, y, width, height;
    public boolean focused = false;
    public boolean canDisplayText;
    private static final ResourceLocation rs = new ResourceLocation("client/menu-rect.png");

    public InputField(boolean canDisplayText) {
        this.canDisplayText = canDisplayText;
        hoverAnimation = new DecelerateAnimation(200, 1);
    }

    @Override
    public void initGui() {
        hoverAnimation = new DecelerateAnimation(200, 1);    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (focused) {
            if (keyCode == 14 && text.length() > 0) { // Backspace key
                text = text.substring(0, text.length() - 1);
            } else if (Character.isLetterOrDigit(typedChar) || Character.isWhitespace(typedChar)) {
                text += typedChar;
            }
        }
    }
    @Override
    public void drawScreen(int mouseX, int mouseY) {
        boolean hovered = MouseUtil.isHovering(x, y, width, height, mouseX, mouseY);
        hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);

        RenderUtil.color(-1);
        RenderUtil.drawImage(rs, x, y, width, height);

        if (canDisplayText) {
            FontManager.M22.drawCenteredString(text, x + width / 2f, y + FontManager.M22.getMiddleOfBox(height), -1);
        } else {
            String displayText = "";
            for (int i = 0; i < text.length(); i++) {
                displayText += "*";
            }
            FontManager.M22.drawCenteredString(displayText, x + width / 2f, y + FontManager.M22.getMiddleOfBox(height), -1);
        }
        if (focused && System.currentTimeMillis() % 1000 < 500) {
            FontManager.M22.drawString("_", x + width / 2f + FontManager.M22.getStringWidth(text) / 2 + 2, y + FontManager.M22.getMiddleOfBox(height), -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        focused = MouseUtil.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}
