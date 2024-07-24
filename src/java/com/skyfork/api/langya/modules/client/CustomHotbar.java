package com.skyfork.api.langya.modules.client;

import com.skyfork.api.cedo.misc.MathUtils;
import com.skyfork.api.langya.utils.StopWatch;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.ShaderEvent;
import com.skyfork.api.cedo.misc.ColorUtil;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.client.module.render.HUD;
import com.skyfork.client.value.impl.BooleanValue;

import java.awt.*;

import static com.skyfork.api.cedo.shader.RoundedUtil.reAlpha;
import static com.skyfork.client.Access.InstanceAccess.mc;

/**
 * @author LangYa466
 * @since 2024/5/7 17:05
 */
public class CustomHotbar {
    private final BooleanValue blur = new BooleanValue("模糊背景",true);
    private static float rPosX = 0f;
    private static StopWatch stopwatch = new StopWatch();

    public CustomHotbar() {
        rPosX = 0f;
        stopwatch = new StopWatch();
    }

    @EventTarget
    public void onShaderEvent(ShaderEvent event) {
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

            RoundedUtil.drawThemeColorRound((float) (i - 91), (float) (sr.getScaledHeight() - ((22 + 4) + 15 * GuiChat.openingAnimation.getOutput())), 182f, 22f, 4f,
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
