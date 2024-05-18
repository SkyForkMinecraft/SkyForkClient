package cn.langya.modules.render;

import cn.langya.elements.impls.keystore.KeyButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.value.impl.NumberValue;

/**
 * @author LangYa466
 * @date 2024/4/25 20:32
 */

public class KeyStore implements Access.InstanceAccess {

    public NumberValue x = new NumberValue("X",80,0,1020,10);
    public NumberValue y = new NumberValue("Y",80,0,1980,10);

    @EventTarget
    private void onRender2D(Render2DEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(30,0,0);
        new KeyButton(x.getValue().intValue(),y.getValue().intValue(),30,30,3, Keyboard.KEY_W);
        new KeyButton(x.getValue().intValue() - 30,y.getValue().intValue() + 30,30,30,3, Keyboard.KEY_A);
        new KeyButton(x.getValue().intValue(),y.getValue().intValue() + 30,30,30,3, Keyboard.KEY_S);
        new KeyButton(x.getValue().intValue() + 30,y.getValue().intValue() + 30,30,30,3, Keyboard.KEY_D);
        new KeyButton(x.getValue().intValue() - 30,y.getValue().intValue() + 60, 30 * 3,30,3, Keyboard.KEY_SPACE);
        GlStateManager.popMatrix();
    }

}