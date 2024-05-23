package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.event.ShaderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.misc.AttackEvent;
import org.union4dev.base.events.render.Render2DEvent;
import cn.langya.font.FontManager;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;
import skid.cedo.render.RenderUtil;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author LangYa466
 * @date 2024/4/25 20:29
 */

public class TargetHUD extends Element implements Access.InstanceAccess {

    private final DecimalFormat df = new DecimalFormat("0.0");
    private final BooleanValue blur = new BooleanValue("模糊背景",true);

    private EntityPlayer target;
    private float width;

    public TargetHUD() {
        super(50, 50);
    }

    @EventTarget
    void onA(AttackEvent e) {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) return;
        if (e.getTarget() instanceof EntityPlayer) target = (EntityPlayer) e.getTarget();
    }

    @EventTarget
    void onS(ShaderEvent s) {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) return;
        if (blur.getValue() && s.getType() == ShaderType.Blur && target != null)   RoundedUtil.drawRound(x, y, width, 55, 5, new Color(0, 0, 0, 180));
    }

    @EventTarget
    private void onRender2D(Render2DEvent event) {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
        } else {
            if (mc.currentScreen instanceof GuiChat && target == null) target = mc.thePlayer;
            if (target == null) {
                setSuffix("无目标", this);
                return;
            }
            if (target.isDead || target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > 5) {
                target = null;
            }
            if (!(mc.currentScreen instanceof GuiChat) && target == mc.thePlayer) {
                target = null;
                return;
            }

            width = FontManager.M18.getStringWidth(target.getDisplayName().getFormattedText()) + target.getMaxHealth() * 4;
            setWidth(width);
            setHeight(55);

            setSuffix(String.format("剩余血量 : %s", df.format(target.getHealth())), this);
            RoundedUtil.drawRound(x, y, width, 55, 5, new Color(0, 0, 0, 180));
            RoundedUtil.drawRound(x + 2, y + 44.5F, target.getHealth() * 5, 8, 3, new Color(188, 43, 43));
            FontManager.M20.drawStringWithShadow(target.getDisplayName().getFormattedText(), x + 40, y + 5, -1);
            FontManager.M16.drawStringWithShadow(String.format("剩余血量 : %s", df.format(target.getHealth())), x + 40, y + 20, -1);
            FontManager.M16.drawStringWithShadow(String.format("目标距离 : %s", df.format(target.getDistanceToEntity(mc.thePlayer))), x + 40, y + 30, -1);
            drawBigHead(x + 2, y + 4, 35, 35, (AbstractClientPlayer) target);
        }
    }

    private void drawBigHead(float x, float y, float width, float height, AbstractClientPlayer player) {
        double offset = -(player.hurtTime * 23);
        RenderUtil.color(new Color(255, (int)(255.0 + offset), (int)(255.0 + offset)).getRGB());
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        Minecraft.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

}
