package cn.langya.elements.impls;

import cn.langya.elements.Element;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;


public class PotionInfo extends Element {
    private static final BooleanValue background = new BooleanValue("背景",true);
    private static final NumberValue customRadius = new NumberValue("自定义圆角值", 2,0,10,1);

    public PotionInfo() {
        super(10, 10);
    }

    @Override
    public void draw() {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        setWidth(50);
        setHeight(50);
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x - 50 ,y - 50 ,0);
        InventoryEffectRenderer.instance.drawActivePotionEffects(background.getValue(),customRadius.getValue().intValue());
        GlStateManager.popMatrix();
    }
}
