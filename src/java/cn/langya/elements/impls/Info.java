package cn.langya.elements.impls;

import cn.langya.elements.Element;
import net.minecraft.client.Minecraft;
import org.union4dev.base.Access;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/4/11 19:05
 */

public class Info extends Element {

    public Info() {
        super("Info",50, 50);
    }

    @Override
    public void draw() {
        double bps = Math.hypot(
                mc.thePlayer.posX - mc.thePlayer.prevPosX,
                mc.thePlayer.posZ - mc.thePlayer.prevPosZ
            ) * mc.timer.timerSpeed * 20;
        String str = String.format("SkyClient | FPS: %s | BPS: %s", Minecraft.getDebugFPS(), Math.round(bps * 100.0) / 100.0);

        // draw
        RoundedUtil.drawRound(getX() - 1,getY(),getWidth(),getHeight(),2,new Color(0,0,0,100));
        Access.getInstance().getFontManager().F18.drawStringWithShadow(
                str,
                getX(),
                getY(),
                -1
        );

        this.setWidth(Access.getInstance().getFontManager().F18.getWidth(str) + 6);
        this.setHeight(Access.getInstance().getFontManager().F14.getHeight());
    }

}
