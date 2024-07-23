package cn.langya.modules.client;

import cn.cedo.misc.MathUtils;
import cn.langya.utils.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.ShaderEvent;
import cn.cedo.misc.ColorUtil;
import cn.cedo.shader.RoundedUtil;
import org.union4dev.base.module.render.HUD;
import org.union4dev.base.value.impl.BooleanValue;

import java.awt.*;

import static cn.cedo.shader.RoundedUtil.reAlpha;
import static org.union4dev.base.Access.InstanceAccess.mc;

/**
 * @author LangYa466
 * @since 2024/5/7 17:05
 */
public class CustomHotbar {
    private final BooleanValue blur = new BooleanValue("模糊背景",true);
    private static float rPosX;
    private static StopWatch stopwatch;

    public CustomHotbar() {
        rPosX = 0f;
        stopwatch = new StopWatch();
    }

    @EventTarget
    public void onShaderEvent(ShaderEvent event) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (!blur.getValue()) return;
        renderEffect(new ScaledResolution(mc));
    }

    public static void renderEffect(ScaledResolution sr) {
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            if (Access.getInstance().getModuleManager().getHandle(CustomHotbar.class).isEnabled()) {
                for (long time = 0; time < stopwatch.getElapsedTime(); time++) {
                    rPosX = (float) MathUtils.lerp(rPosX, (i - 91 - 1 + entityPlayer.inventory.currentItem * 20), 0.055
                    );
                }
                stopwatch.reset();
            } else {
                rPosX = (i - 91 - 1 + entityPlayer.inventory.currentItem * 20);
            }

            RoundedUtil.drawThemeColorRound((float) (i - 91), (float) (sr.getScaledHeight() - ((22 + 4) + 15 * GuiChat.openingAnimation.getOutput())), 182f, 22f, 5f,
                    ColorUtil.applyOpacity(reAlpha(HUD.colorWheel.getColor1(),60), 60),
                    ColorUtil.applyOpacity(reAlpha(HUD.colorWheel.getColor4(),60), 60),
                    ColorUtil.applyOpacity(reAlpha(HUD.colorWheel.getColor2(),60), 60),
                    ColorUtil.applyOpacity(reAlpha(HUD.colorWheel.getColor3(),60), 60),
                    new Color(30,30,30,100));
            RoundedUtil.drawRound(rPosX + 2, (float) (sr.getScaledHeight() - ((22 + 4) + 15 * GuiChat.openingAnimation.getOutput())), 20f, 22f,
                    4f, new Color(255, 255, 255, 150)
            );
        }
    }
}
