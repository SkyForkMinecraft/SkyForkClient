package cn.langya.elements.impls;

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

    public CPSInfo() {
        super(250, 250);
    }

    @Override
    public void draw() {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        FontDrawer fontRenderer = FontManager.M18;
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        if (backgroundValue.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
        fontRenderer.drawStringWithShadow(text, x, y + 0.5,-1);
    }

}
