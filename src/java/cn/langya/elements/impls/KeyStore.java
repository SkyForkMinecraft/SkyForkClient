package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import cn.langya.modules.misc.particles.TimerUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/4/25 20:32
 */

public class KeyStore extends Element implements Access.InstanceAccess {

    public KeyStore() {
        super(30, 30);
    }

    public void draw(float x, float y, float width, float height, int radius, int keyCode) {
        if(Keyboard.getEventKey() == keyCode) {
            RoundedUtil.drawRound(x, y, width, height, radius, new Color(0, 0, 0, 160));
        } else RoundedUtil.drawRound(x,y,width,height,radius,new Color(0,0,0,80));

        FontManager.M14.drawStringWithShadow(Keyboard.getKeyName(keyCode),x + 12,y + 10,-1);

    }

    @EventTarget
    private void onRender2D(Render2DEvent event) {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
        } else {
            setWidth(100);
            setHeight(100);
            draw(x, y, 30, 30, 3, Keyboard.KEY_W);
            draw(x - 30, y + 30, 30, 30, 3, Keyboard.KEY_A);
            draw(x, y + 30, 30, 30, 3, Keyboard.KEY_S);
            draw(x + 30, y + 30, 30, 30, 3, Keyboard.KEY_D);
            draw(x - 30, y + 60, 30 * 3, 30, 3, Keyboard.KEY_SPACE);
        }
    }

}