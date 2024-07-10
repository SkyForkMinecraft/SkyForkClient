package cn.langya.elements.impls;

import cn.cedo.drag.Dragging;
import cn.cedo.shader.RoundedUtil;
import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;
import superblaubeere27.CPSCounter;

import java.awt.*;

public class CPSInfo {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    private final BooleanValue colorText = new BooleanValue("彩虹色文字",true);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);
    FontDrawer fontRenderer = FontManager.M18;
    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(), "cpsinfo", 150, 150);


    @EventTarget
    public void draw(ShaderEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        if (backgroundValue.getValue() && blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
    }

    @EventTarget
    public void draw(Render2DEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        if (backgroundValue.getValue() && !blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
        pos.setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 0.5,-1);
        if (colorText.getValue()) {
            fontRenderer.drawGradientStringWithShadow("CPS", x, y + 0.5);
        }
    }
}
