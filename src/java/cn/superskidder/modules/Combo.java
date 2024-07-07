package cn.superskidder.modules;

import cn.cedo.shader.RoundedUtil;
import cn.langya.elements.Element;
import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import cn.superskidder.ComboHandler;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;

public class Combo extends Element implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    private final BooleanValue colorText = new BooleanValue("彩虹色文字",true);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);

    public Combo() {
        super(4, 4);
    }

    @Override
    public void draw() {
        String text = String.format("Combo: %s", ComboHandler.combo);
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        FontDrawer fontRenderer = FontManager.M18;
        if (backgroundValue.getValue() && !blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
        setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 0.5,-1);
        if (colorText.getValue()) {
            fontRenderer.drawGradientStringWithShadow("Combo", x, y + 0.5);
        }
    }
}
