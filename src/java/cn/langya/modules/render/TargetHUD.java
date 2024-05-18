package cn.langya.modules.render;

import cn.langya.utils.AnimationUtil;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import cn.langya.font.FontManager;
import org.union4dev.base.value.impl.NumberValue;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

import static cn.langya.elements.impls.TargetHUD.drawBigHead;

/**
 * @author LangYa466
 * @date 2024/4/25 20:29
 */

public class TargetHUD implements Access.InstanceAccess {

    public NumberValue x = new NumberValue("X",80,0,1020,10);
    public NumberValue y = new NumberValue("Y",80,0,1980,10);

    @EventTarget
    private void onRender2D(Render2DEvent event) {
        int width = (int) (FontManager.M18.getStringWidth(mc.thePlayer.getDisplayName().getFormattedText()) + mc.thePlayer.getMaxHealth() * 5);
        int height = 50;
        setSuffix(String.valueOf( (int) mc.thePlayer.getHealth()), this);
        RoundedUtil.drawRound(x.getValue().intValue(), y.getValue().intValue(), width, 50, 5, new Color(0, 0, 0, 80));
        RoundedUtil.drawRound(x.getValue().intValue() + 50, y.getValue().intValue() + 35, AnimationUtil.animate(mc.thePlayer.getHealth() * 5, mc.thePlayer.getHealth() * 5, 0.2), 10, 5, new Color(231, 61, 61));
        drawBigHead(x.getValue().intValue() + 2, y.getValue().intValue() + 2, 45, 45, mc.thePlayer);

        FontManager.M18.drawStringWithShadow(mc.thePlayer.getDisplayName().getFormattedText(), x.getValue().intValue() + 50, y.getValue().intValue() + 5, -1);
        FontManager.M16.drawStringWithShadow(String.valueOf(((int) mc.thePlayer.getHealth())), x.getValue().intValue() + 50, y.getValue().intValue() + 35, -1);

    }

}
