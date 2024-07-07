package cn.langya.elements.impls;

import cn.cedo.drag.Dragging;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;


public class PotionInfo implements Access.InstanceAccess {
    private static final BooleanValue background = new BooleanValue("背景",true);
    private static final NumberValue customRadius = new NumberValue("自定义圆角值", 2,0,10,1);

    private final Dragging drag = Access.getInstance().getDragManager().createDrag("potioninfo", 300, 300);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        drag.setWidth(50);
        drag.setHeight(50);
        InventoryEffectRenderer.instance.drawActivePotionEffects((int) drag.getXPos(), (int) drag.getYPos(),background.getValue(),customRadius.getValue().intValue());
    }
}
