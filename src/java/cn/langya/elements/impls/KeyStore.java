package cn.langya.elements.impls;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.SmoothStepAnimation;
import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import cn.cedo.shader.RoundedUtil;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;

import java.awt.*;

/**
 * @author LangYa466
 * @since 2024/4/25 20:32
 */

public class KeyStore  extends Element implements Access.InstanceAccess {

  //  private final NumberValue x = new NumberValue("X", 0, 1000, 0, 20);
  //  private final NumberValue y = new NumberValue("Y", 0, 1000, 0, 20);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);

    public KeyStore() {
        super(150,150);
        resetButtonHover = new SmoothStepAnimation(250, 1);
        setWH(150,150);
    }

    Animation resetButtonHover;

    public void draw(float x, float y, float width, float height, int radius, KeyBinding key) {

        float alpha = (float) (.5f + (.5 * resetButtonHover.getOutput().floatValue()));

        resetButtonHover.setDirection(key.isKeyDown() ? Direction.FORWARDS : Direction.BACKWARDS);

        if (key.isKeyDown()) {
            RoundedUtil.drawRound(x, y, width, height, radius, new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), (int) (255 * alpha)));
        } else {
            RoundedUtil.drawRound(x, y, width, height, radius, new Color(0, 0, 0, 120));
        }
        FontManager.M14.drawStringWithShadow(Keyboard.getKeyName(key.getKeyCode()), x + 8, y + 8, -1);
    }

    @EventTarget
    private void onRender2D(Render2DEvent event) {
        if (blur.getValue()) return;
        /*
        draw(x.getValue().intValue() + 1F, y.getValue().intValue() + 60, 27.75F * 3.0F, 25, 5, mc.gameSettings.keyBindJump);

        draw(x.getValue().intValue() + 30, y.getValue().intValue(), 25, 25, 5, mc.gameSettings.keyBindForward);

        draw(x.getValue().intValue(), y.getValue().intValue() + 30, 25, 25, 5, mc.gameSettings.keyBindLeft);

        draw(x.getValue().intValue() + 30, y.getValue().intValue() + 30, 25, 25, 5, mc.gameSettings.keyBindBack);
        draw(x.getValue().intValue() + 60, y.getValue().intValue() + 30, 25, 25, 5, mc.gameSettings.keyBindRight);

         */

        draw(x + 1F, y + 60, 27.75F * 3.0F, 25, 5, mc.gameSettings.keyBindJump);

        draw(x + 30, y, 25, 25, 5, mc.gameSettings.keyBindForward);

        draw(x, y + 30, 25, 25, 5, mc.gameSettings.keyBindLeft);

        draw(y + 30, y + 30, 25, 25, 5, mc.gameSettings.keyBindBack);
        draw(x + 60, y + 30, 25, 25, 5, mc.gameSettings.keyBindRight);
    }

    @EventTarget
    private void onShader(ShaderEvent event) {
        if (!blur.getValue()) return;
        draw(x + 1F, y + 60, 27.75F * 3.0F, 25, 5, mc.gameSettings.keyBindJump);

        draw(x + 30, y, 25, 25, 5, mc.gameSettings.keyBindForward);

        draw(x, y + 30, 25, 25, 5, mc.gameSettings.keyBindLeft);

        draw(y + 30, y + 30, 25, 25, 5, mc.gameSettings.keyBindBack);
        draw(x + 60, y + 30, 25, 25, 5, mc.gameSettings.keyBindRight);
        /*
        draw(x.getValue().intValue() + 1F, y.getValue().intValue() + 60, 27.75F * 3.0F, 25, 5, mc.gameSettings.keyBindJump);

        draw(x.getValue().intValue() + 30, y.getValue().intValue(), 25, 25, 5, mc.gameSettings.keyBindForward);

        draw(x.getValue().intValue(), y.getValue().intValue() + 30, 25, 25, 5, mc.gameSettings.keyBindLeft);

        draw(x.getValue().intValue() + 30, y.getValue().intValue() + 30, 25, 25, 5, mc.gameSettings.keyBindBack);
        draw(x.getValue().intValue() + 60, y.getValue().intValue() + 30, 25, 25, 5, mc.gameSettings.keyBindRight);

         */
    }
}