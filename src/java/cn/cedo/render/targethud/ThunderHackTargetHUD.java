package cn.cedo.render.targethud;

import cn.cedo.misc.MathUtils;
import cn.cedo.render.RenderUtil;
import cn.cedo.render.StencilUtil;
import cn.cedo.shader.RoundedUtil;
import cn.dxg.AnimationUtil;
import cn.dxg.GlowUtils;
import cn.dxg.ParticleRender;
import cn.langya.font.FontManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;

import java.awt.*;

public class ThunderHackTargetHUD extends TargetHUD {

    public ThunderHackTargetHUD() {
        super("ThunderHack");
    }

    @Override
    public void render(float x, float y, float alpha, EntityLivingBase target) {
        final double healthPercentage = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0.0f, 1.0f);
        this.setWidth(170);
        this.setHeight(50);
        RoundedUtil.drawRound(x, y, 70.0f, 50.0f, 6.0f, new Color(0, 0, 0, 139));
        RoundedUtil.drawRound(x + 50.0f, y, 100.0f, 50.0f, 6.0f, new Color(0, 0, 0, 255));
        RenderUtil.drawImageRound(new ResourceLocation("client/thud.png"), x + 30.0f, y - 1.0f, 150.0, 80.0, new Color(255, 255, 255, 150).getRGB(), () -> RoundedUtil.drawRound(x + 50.0f, y, 100.0f, 50.0f, 6.0f, new Color(0, 0, 0, 255)));
        GlStateManager.resetColor();
        if (target instanceof AbstractClientPlayer) {
            ParticleRender.render(x + 2.0f, y + 2.0f, target);
            StencilUtil.initStencilToWrite();
            RenderUtil.renderRoundedRect(x + 3, y + 3, 45, 45, 4, -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.color(-1, alpha);
            renderPlayer2D(x + 2.0f, y + 3.0f, 45, 45, (AbstractClientPlayer)target);
            StencilUtil.uninitStencilBuffer();
            GlStateManager.disableBlend();
        }
        float animatedHealthBar = 1F;
        FontManager.M18.drawString(target.getName(), x + 54.0f, y + 6.0f, -1);
        final float f = (float)(92.0 * healthPercentage);
        animatedHealthBar = AnimationUtil.animate(animatedHealthBar, f, 0.1f);
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.8, 0.8, 0.8);
        if (target != null) {
            RenderUtil.drawEquippedShit2((int)(x + 42.0f) / 0.8, (int)(y + 18.0f) / 0.8, target);
        }
        GlStateManager.popMatrix();
        GlowUtils.drawGlow(x + 54.0f, y + 36.0f, animatedHealthBar, 8.0f, 16, Access.CLIENT_COLOR, () -> RoundedUtil.drawRound(x + 54.0f, y + 36.0f, target.animatedHealthBar, 8.0f, 2.0f, Access.CLIENT_COLOR));
        RoundedUtil.drawRound(x + 54.0f, y + 36.0f, animatedHealthBar, 8.0f, 2.0f, Access.CLIENT_COLOR);
        FontManager.M14.drawCenteredString(String.valueOf(MathUtils.round(target.getHealth(), 1)), x + 100.0f, y + 38.0f, -1);
    }


    @Override
    public void renderEffects(float x, float y, float alpha, boolean glow) {
        RoundedUtil.drawRound(x, y, 70.0f, 50.0f, 6.0f, new Color(0, 0, 0, 255));
        RoundedUtil.drawRound(x + 50.0f, y, 10.0f, 50.0f, 6.0f, new Color(0, 0, 0, 255));
    }

}
