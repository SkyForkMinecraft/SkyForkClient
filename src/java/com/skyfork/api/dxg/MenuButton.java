//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package com.skyfork.api.dxg;

import com.skyfork.api.cedo.animations.Animation;
import com.skyfork.api.cedo.animations.Direction;
import com.skyfork.api.cedo.animations.impl.DecelerateAnimation;
import com.skyfork.api.cedo.misc.ColorUtil;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.langya.utils.MouseUtil;

import java.awt.*;

public class MenuButton implements Screen
{
    public final String text;
    private Animation hoverAnimation;
    public float x;
    public float y;
    public float width;
    public float height;
    public Runnable clickAction;
    
    public MenuButton(final String text) {
        this.text = text;
    }
    
    @Override
    public void initGui() {
        this.hoverAnimation = new DecelerateAnimation(500, 1.0);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final boolean hovered = MouseUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
        Color rectColor = new Color(35, 37, 43, 102);
        rectColor = ColorUtil.interpolateColorC(rectColor, ColorUtil.brighter(rectColor, 0.4f), this.hoverAnimation.getOutput().floatValue());
        RoundedUtil.drawRoundOutline(this.x, this.y, this.width, this.height, 12.0f, 1.0f, rectColor, new Color(30, 30, 30, 100));
        FontManager.M20.drawCenteredString(this.text, this.x + this.width / 2.0f, this.y + FontManager.M20.getMiddleOfBox(this.height) + 2.0f, -1);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean hovered = MouseUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (hovered) {
            this.clickAction.run();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
