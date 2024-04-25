package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.utils.AnimationUtil;
import lombok.Getter;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.union4dev.base.Access;
import skid.cedo.render.RenderUtil;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;


/**
 * @author LangYa466
 * @date 2024/4/18 22:02
 */

public class TargetHUD extends Element {

    public TargetHUD() {
        super("TargetHUD",800, 800);
    }

    public static void drawBigHead(float x, float y, float width, float height, AbstractClientPlayer player) {
        double offset = -(player.hurtTime * 23);
        RenderUtil.color(new Color(255, (int)(255.0 + offset), (int)(255.0 + offset)).getRGB());
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        mc.getTextureManager().bindTexture(player.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    int width, height;

    @Getter
    AbstractClientPlayer target = mc.thePlayer;

    @Override
    public void draw() {

        if (mc.theWorld == null || mc.thePlayer == null) return;

        if (target.hurtTime <= 0 && !(mc.currentScreen instanceof GuiChat)) return;

        width = (int) (Access.getInstance().getFontManager().F18.getWidth(target.getName()) + target.getMaxHealth() * 5);
        height = 50;

        this.setWidth(width);
        this.setHeight(height);

        GlStateManager.popMatrix();
        GL11.glPointSize(0.8F);

        // draw
        RoundedUtil.drawRound(getX(), getY(), width, 50, 5, new Color(0, 0, 0, 80));
        RoundedUtil.drawRound(getX() + 50, getY() + 35, AnimationUtil.animate(target.getHealth() * 5, target.getHealth() * 5, 0.2), 10, 5, new Color(231, 61, 61));
        drawBigHead(getX() + 2, getY() + 2, 45, 45, target);

        Access.getInstance().getFontManager().F18.drawStringWithShadow(target.getDisplayName().getFormattedText(), getX() + 50, getY() + 5, -1);
        Access.getInstance().getFontManager().F16.drawStringWithShadow(String.valueOf(((int) target.getHealth())), getX() + 50, getY() + 35, -1);

        GlStateManager.pushMatrix();

    }
}
