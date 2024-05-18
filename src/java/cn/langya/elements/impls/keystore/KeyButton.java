package cn.langya.elements.impls.keystore;

import org.lwjgl.input.Keyboard;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.misc.KeyInputEvent;
import cn.langya.font.FontManager;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/4/19 20:30
 */

public class KeyButton {

    float x,y,width,height,radius;
    int keyCode;
    boolean use;

    public KeyButton(float x, float y, float width, float height, float radius, int keyCode) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.keyCode = keyCode;
        draw();
        EventManager.register(this);
    }

    @EventTarget
    void onK(KeyInputEvent e) {
        use = (keyCode == e.getKey());
    }

    public void draw() {


        if(use) RoundedUtil.drawRound(x,y,width,height,radius,new Color(0,0,0,160));
        else RoundedUtil.drawRound(x,y,width,height,radius,new Color(0,0,0,80));

        FontManager.M14.drawStringWithShadow(Keyboard.getKeyName(keyCode),x + 12,y + 10,-1);

    }

}
