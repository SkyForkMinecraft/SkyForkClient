package com.skyfork.api.cedo.render.targethud;

import com.skyfork.api.cedo.animations.ContinualAnimation;
import com.skyfork.api.cedo.misc.ColorUtil;
import com.skyfork.api.cedo.misc.MathUtils;
import com.skyfork.api.cedo.render.RenderUtil;
import com.skyfork.api.cedo.render.StencilUtil;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class TenacityTargetHUD extends TargetHUD {

    public TenacityTargetHUD() {
        super("Tenacity");
    }

    private final ContinualAnimation animatedHealthBar = new ContinualAnimation();

    @Override
    public void render(float x, float y, float alpha, EntityLivingBase target) {
        int textColor = ColorUtil.applyOpacity(-1, alpha);
        setWidth(Math.max(155, FontManager.MB20.getStringWidth(target.getName()) + 75));
        setHeight(50);
        RenderUtil.setAlphaLimit(0);

        float colorAlpha = .8f * alpha;
        RoundedUtil.drawGradientRound(x, y, getWidth(), getHeight(), 6,
                ColorUtil.applyOpacity(colorWheel.getColor1(), colorAlpha),
                ColorUtil.applyOpacity(colorWheel.getColor4(), colorAlpha),
                ColorUtil.applyOpacity(colorWheel.getColor2(), colorAlpha),
                ColorUtil.applyOpacity(colorWheel.getColor3(), colorAlpha));


        float size = 38;
        if (target instanceof AbstractClientPlayer) {
            StencilUtil.initStencilToWrite();
            RenderUtil.circleNoSmoothRGB(x + 10, y + (getHeight() / 2f) - (size / 2f), size, -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.resetColor();
            RenderUtil.setAlphaLimit(0);

            RenderUtil.color(textColor);
            renderPlayer2D(x + 10, y + (getHeight() / 2f) - (size / 2f), size, size, (AbstractClientPlayer) target);
            StencilUtil.uninitStencilBuffer();
        } else {
            FontManager.MB30.drawCenteredStringWithShadow("?", x + 30, y + 25 - FontManager.MB30.getHeight() / 2f, textColor);
        }

        FontManager.MB20.drawCenteredString(target.getName(), x + 10 + size + ((getWidth() - (10 + size)) / 2f), y + 10, textColor);

        float healthPercentage = (target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount());

        float healthBarWidth = getWidth() - (size + 30);

        float newHealthWidth = (healthBarWidth) * healthPercentage;
        animatedHealthBar.animate(newHealthWidth, 18);


        RoundedUtil.drawRound(x + 20 + size, y + 25, healthBarWidth, 4, 2, ColorUtil.applyOpacity(Color.BLACK, .3f * alpha));
        RoundedUtil.drawRound(x + 20 + size, y + 25, animatedHealthBar.getOutput(), 4, 2, ColorUtil.applyOpacity(Color.WHITE, alpha));

        String healthText = MathUtils.DF_0.format(healthPercentage * 100) + "%";

        FontManager.M18.drawCenteredString(healthText + " - " + Math.round(target.getDistanceToEntity(mc.thePlayer)) + "m",
                x + 10 + size + ((getWidth() - (10 + size)) / 2f), y + 35, textColor);
    }

    @Override
    public void renderEffects(float x, float y, float alpha, boolean glow) {
        if (glow) {
            RoundedUtil.drawGradientRound(x, y, getWidth(), getHeight(), 6,
                    ColorUtil.applyOpacity(colorWheel.getColor1(), alpha),
                    ColorUtil.applyOpacity(colorWheel.getColor4(), alpha),
                    ColorUtil.applyOpacity(colorWheel.getColor2(), alpha),
                    ColorUtil.applyOpacity(colorWheel.getColor3(), alpha));
        } else {
            RoundedUtil.drawRound(x, y, getWidth(), getHeight(), 6, ColorUtil.applyOpacity(Color.BLACK, alpha));
        }
    }
}
