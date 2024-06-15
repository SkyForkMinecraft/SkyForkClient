package cn.langya.elements.impls;

import cn.cedo.render.RenderUtil;
import cn.cedo.shader.GradientUtil;
import cn.cedo.shader.RoundedUtil;
import cn.langya.elements.Element;
import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;
import superblaubeere27.CPSCounter;

import java.awt.*;

public class CPSInfo extends Element {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    private final BooleanValue colorText = new BooleanValue("彩虹色文字",true);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);
    FontDrawer fontRenderer = FontManager.M18;
    public CPSInfo() {
        super(250, 250);
    }


    @Override
    public void draw(boolean shader) {
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        if (backgroundValue.getValue() && blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
    }

    @Override
    public void draw() {
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        if (backgroundValue.getValue() && !blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
        setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 0.5,-1);
        if (colorText.getValue()) {
            fontRenderer.drawGradientStringWithShadow("CPS", x, y + 0.5);
        }
    }
}
