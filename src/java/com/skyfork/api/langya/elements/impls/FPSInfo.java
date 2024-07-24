package com.skyfork.api.langya.elements.impls;

import com.skyfork.api.cedo.drag.Dragging;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontDrawer;
import com.skyfork.api.langya.font.FontManager;
import net.minecraft.client.Minecraft;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.events.render.ShaderEvent;
import com.skyfork.client.value.impl.BooleanValue;
import com.skyfork.client.value.impl.ComboValue;
import com.skyfork.client.value.impl.NumberValue;
import com.skyfork.api.cedo.misc.ColorUtil;

import java.awt.*;

public class FPSInfo {

    public ComboValue textMode = new ComboValue("显示文本", "FPS", "帧率", "FPS");
    private static final ComboValue colorMode = new ComboValue("颜色", "客户端", "客户端", "自定义", "彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色", 0, 0, 255, 5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色", 0, 0, 255, 5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色", 0, 0, 255, 5);
    private final BooleanValue colorText = new BooleanValue("彩虹色文字",true);

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);
    private final Dragging pos = Access.getInstance().getDragManager().createDrag( this.getClass(),"fpsinfo", 150, 150);


    private String text;

    private final FontDrawer fontRenderer = FontManager.M18;

    @EventTarget
    public void onShader(ShaderEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        if (backgroundValue.getValue() && blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));

    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        if (backgroundValue.getValue() && !blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));

        String logoText = textMode.getValue();
        text = String.format("[%s: %s]",logoText, Minecraft.getDebugFPS());
        Color c = Access.CLIENT_COLOR;
        switch (colorMode.getValue()) {
            case "客户端":
                c = Access.CLIENT_COLOR;
                break;
            case "自定义":
                c = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue());
                break;
            case "彩虹":
                c = new Color(ColorUtil.getColor(-(1 + 5 * 1.7f), 0.7f, 1));
        }
        if (colorText.getValue()) {
            FontManager.M18.drawGradientStringWithShadow(text, x,y);
        } else {
            FontManager.M18.drawStringWithShadow(text, x,y, c.getRGB());
        }
        pos.setWidth(FontManager.M18.getStringWidth(text));
        pos.setHeight(FontManager.M18.getHeight());
    }
}
