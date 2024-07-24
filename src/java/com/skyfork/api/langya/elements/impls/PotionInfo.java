package com.skyfork.api.langya.elements.impls;

import com.skyfork.api.cedo.drag.Dragging;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.value.impl.BooleanValue;
import com.skyfork.client.value.impl.NumberValue;


public class PotionInfo implements Access.InstanceAccess {
    private static final BooleanValue background = new BooleanValue("背景",true);
    private static final NumberValue customRadius = new NumberValue("自定义圆角值", 2,0,10,1);

    private final Dragging drag = Access.getInstance().getDragManager().createDrag(this.getClass(),"potioninfo", 300, 300);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        drag.setWidth(50);
        drag.setHeight(50);
        InventoryEffectRenderer.instance.drawActivePotionEffects((int) drag.getXPos(), (int) drag.getYPos(),background.getValue(),customRadius.getValue().intValue());
    }
}
