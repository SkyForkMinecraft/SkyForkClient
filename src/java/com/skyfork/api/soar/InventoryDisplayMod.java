package com.skyfork.api.soar;

import com.skyfork.api.cedo.drag.Dragging;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.event.ShaderType;
import com.skyfork.api.langya.font.FontManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.events.render.ShaderEvent;
import com.skyfork.client.value.impl.BooleanValue;
import com.skyfork.client.value.impl.ComboValue;
import com.skyfork.client.value.impl.NumberValue;

import java.awt.*;

public class InventoryDisplayMod implements Access.InstanceAccess{
    public ComboValue textMode = new ComboValue("显示文本", "Inventory", "Inventory", "背包");
    private static final NumberValue customAplha = new NumberValue("自定义不透明度",80,0,255,5);
    private static final NumberValue customRadius = new NumberValue("自定义圆角值", 2,0,10,1);
    private final Dragging drag = Access.getInstance().getDragManager().createDrag(this.getClass(), "inventorydisplay", 150, 150);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);

    @EventTarget
	public void onRender2D(Render2DEvent event) {
        float startX = drag.getXPos() + 4;
        float startY = drag.getYPos() + 20;
        int curIndex = 0;
        
        RoundedUtil.drawRound(drag.getXPos(), drag.getYPos(), 185, 79,customRadius.getValue().intValue(),new Color(0,0,0,customAplha.getValue().intValue()));
        RoundedUtil.drawRound(drag.getXPos(), drag.getYPos() + 16, 185, 1, 0, -1);
        
        FontManager.M22.drawString(textMode.getValue(), drag.getXPos() + 4.5F, drag.getYPos() + 4.5F, -1);
        
        for(int i = 9; i < 36; ++i) {
            ItemStack slot = mc.thePlayer.inventory.mainInventory[i];
            if(slot == null) {
                startX += 20;
                curIndex += 1;

                if(curIndex > 8) {
                    curIndex = 0;
                    startY += 20;
                    startX = drag.getXPos() + 4;
                }

                continue;
            }

            this.drawItemStack(slot, (int) startX, (int) startY);
            startX += 20;
            curIndex += 1;
            if(curIndex > 8) {
                curIndex = 0;
                startY += 20;
                startX = drag.getXPos() + 4;
            }
        }
        
        drag.setWidth(185);
        drag.setHeight(79);
	}
	
	@EventTarget
	public void onShader(ShaderEvent event) {
        if (!blur.getValue() && event.getType() != ShaderType.Shadow) return;
		RoundedUtil.drawRound(drag.getXPos(), drag.getYPos(), drag.getWidth(), drag.getHeight(),-1);
	}
	
	
    private void drawItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, null);
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}