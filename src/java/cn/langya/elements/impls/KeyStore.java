package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import cn.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/4/25 20:32
 */

public class KeyStore extends Element implements Access.InstanceAccess {

    public KeyStore() {
        super(30, 30);
    }

    public void draw(float x, float y, float width, float height, int radius, KeyBinding key) {
        if (key.isKeyDown()) {
            RoundedUtil.drawRound(x, y, width, height, radius, new Color(Color.white.getRed(),Color.white.getGreen(),Color.white.getBlue(),160));
            FontManager.M14.drawStringWithShadow(Keyboard.getKeyName(key.getKeyCode()), x + 12, y + 10, 1);
        } else {
            RoundedUtil.drawRound(x, y, width, height, radius, new Color(0, 0, 0, 120));
            FontManager.M14.drawStringWithShadow(Keyboard.getKeyName(key.getKeyCode()), x + 8, y + 8, -1);
        }
    }

    @EventTarget
    private void onRender2D(Render2DEvent event) {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
        } else {
            setWidth(100);
            setHeight(100);
            draw(x, y, 25, 25, 5, mc.gameSettings.keyBindForward);

            draw(x - 30, y + 30, 25, 25, 5, mc.gameSettings.keyBindLeft);

            draw(x, y + 30, 25, 25, 5, mc.gameSettings.keyBindBack);
            draw(x + 30, y + 30, 25, 25, 5,mc.gameSettings.keyBindRight);
            draw(x - 30, y + 60, 30 * 3, 25, 5, mc.gameSettings.keyBindJump);
        }
    }

}