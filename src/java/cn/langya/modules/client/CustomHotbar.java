package cn.langya.modules.client;

import net.minecraft.client.gui.ScaledResolution;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/5/7 17:05
 */

public class CustomHotbar {
    private static final ComboValue colorMode = new ComboValue("颜色模式","客户端","客户端","自定义","彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色",0,0,255,5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色",0,0,255,5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色",0,0,255,5);
    private static final NumberValue hotbarRadius = new NumberValue("自定义圆角值", 2,0,10,1);
    public static BooleanValue mcHotbar = new BooleanValue("原版", false);
    private static Color color;
    public static void drawCustomHotbar(ScaledResolution sr) {

        switch (colorMode.getValue()) {
            case "客户端" : color = Access.CLIENT_COLOR; break;
            case "自定义" : color = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue()); break;
            case "彩虹" : color = new Color(Color.HSBtoRGB((float)(Access.InstanceAccess.mc.thePlayer.ticksExisted / 50.0 + Math.sin(0.032)) % 1.0f, 0.5f, 1.0f)); break;
        }

        RoundedUtil.drawRound(sr.getScaledWidth() / 2f - 90, sr.getScaledHeight() - 22, 180, 20, 3, new Color(0, 0, 0, 80));
        RoundedUtil.drawRound(sr.getScaledWidth() / 2f - 90, sr.getScaledHeight() - 22F, 180, 1, hotbarRadius.getValue().intValue(), color);
    }
}
