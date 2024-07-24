package com.skyfork.api.superskidder.modules;

import com.skyfork.api.cedo.drag.Dragging;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontDrawer;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.superskidder.ComboHandler;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.events.render.ShaderEvent;
import com.skyfork.client.value.impl.BooleanValue;
import com.skyfork.client.value.impl.NumberValue;
import com.skyfork.api.superblaubeere27.CPSCounter;

import java.awt.*;

public class Combo implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    private final BooleanValue colorText = new BooleanValue("彩虹色文字",true);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);
    private final Dragging pos = Access.getInstance().getDragManager().createDrag( this.getClass(),"combo", 150, 150);

    private final FontDrawer fontRenderer = FontManager.M18;


    @EventTarget
    public void draw(ShaderEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        if (backgroundValue.getValue() && blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        String text = String.format("Combo: %s", ComboHandler.combo);
        if (backgroundValue.getValue() && !blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
        pos.setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 0.5,-1);
        if (colorText.getValue()) {
            fontRenderer.drawGradientStringWithShadow("Combo", x, y + 0.5);
        }
    }
}
