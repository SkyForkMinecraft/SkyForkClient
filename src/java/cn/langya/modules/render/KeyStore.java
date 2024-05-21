package cn.langya.modules.render;

import cn.langya.font.FontManager;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.value.impl.NumberValue;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/4/25 20:32
 */

public class KeyStore implements Access.InstanceAccess {

    public NumberValue x = new NumberValue("X",80,0,1020,10);
    public NumberValue y = new NumberValue("Y",80,0,1980,10);

    public void draw(float x,float y,float width,float height,int radius,int keyCode) {


        if(Keyboard.getEventKey() == keyCode) RoundedUtil.drawRound(x,y,width,height,radius,new Color(0,0,0,160));
        else RoundedUtil.drawRound(x,y,width,height,radius,new Color(0,0,0,80));

        FontManager.M14.drawStringWithShadow(Keyboard.getKeyName(keyCode),x + 12,y + 10,-1);

    }
    
    @EventTarget
    private void onRender2D(Render2DEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(30,0,0);
        draw(x.getValue().intValue(),y.getValue().intValue(),30,30,3, Keyboard.KEY_W);
        draw(x.getValue().intValue() - 30,y.getValue().intValue() + 30,30,30,3, Keyboard.KEY_A);
        draw(x.getValue().intValue(),y.getValue().intValue() + 30,30,30,3, Keyboard.KEY_S);
        draw(x.getValue().intValue() + 30,y.getValue().intValue() + 30,30,30,3, Keyboard.KEY_D);
        draw(x.getValue().intValue() - 30,y.getValue().intValue() + 60, 30 * 3,30,3, Keyboard.KEY_SPACE);
        GlStateManager.popMatrix();
    }

}