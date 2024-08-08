package com.skyfork.api.cedo.render.targethud;

import com.skyfork.api.cedo.animations.ContinualAnimation;
import com.skyfork.api.cedo.misc.ColorUtil;
import com.skyfork.api.cedo.misc.MathUtils;
import com.skyfork.api.cedo.render.RenderUtil;
import com.skyfork.api.cedo.render.StencilUtil;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.client.module.render.HUD;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

// author: BzdHyp
public class RiseTargetHUD extends TargetHUD {
    private final ContinualAnimation animatedHealthBar = new ContinualAnimation();

    public RiseTargetHUD() {
        super("Rise");
    }

    @Override
    public void render(float x, float y, float alpha, EntityLivingBase target) {
        float width;
        float height;
        height = 32;
        width = Math.max(120.0F, FontManager.MB18.getStringWidth(target.getName()) + 50.0F);
        this.setWidth(width);
        this.setHeight(height);
        //background
        RoundedUtil.drawRound(x, y, width, getHeight(), 4, new Color(0, 0, 0, 120));
        //health bar
        float healthPercent = (target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount());
        float var = (getWidth() - 37.5f) * healthPercent;
        animatedHealthBar.animate(var, 18);
        RoundedUtil.drawGradientHorizontal(x + 34f, (y + getHeight() - 13), width - 37.2F, 8, 1, new Color(0, 0, 0, 150), new Color(0, 0, 0, 85));
        RoundedUtil.drawGradientHorizontal(x + 34f, (y + getHeight() - 13), animatedHealthBar.getOutput(), 8, 1, HUD.getClientColors().getFirst(), HUD.getClientColors().getSecond());
        //render playerface
        final int scaleOffset = (int) (target.hurtTime * 0.7f);
        if (target instanceof AbstractClientPlayer) {
            StencilUtil.initStencilToWrite();
            RenderUtil.renderRoundedRect(x + 2, y + 2, 28, 28, 2, -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.color(-1, alpha);
            renderPlayer2D(x + 2 + scaleOffset / 2f, y + 2 + scaleOffset / 2f, 29 - scaleOffset, 29 - scaleOffset, (AbstractClientPlayer) target);
            //renderPlayer2D(x + (float)1.5, y + (float)1.5, 29, 29, (AbstractClientPlayer) target);
            StencilUtil.uninitStencilBuffer();
            GlStateManager.disableBlend();
        }
        //target name
        FontManager.MB18.drawString(target.getName(), x + 33f, (float) (y + 3.5) + 1f, Color.WHITE.getRGB());
        //health text
        String healthText = (int) MathUtils.round(healthPercent * 100, .01) + ".0%";
        FontManager.MB18.drawString(healthText, x + 59, y + 18.5f, Color.WHITE.getRGB());
    }

    @Override
    public void renderEffects(float x, float y, float alpha, boolean glow) {
        RoundedUtil.drawRound(x, y, getWidth(), getHeight(), 4, ColorUtil.applyOpacity(Color.BLACK, 100));
    }
}