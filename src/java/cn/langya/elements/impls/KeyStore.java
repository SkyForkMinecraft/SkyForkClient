package cn.langya.elements.impls;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.DecelerateAnimation;
import cn.cedo.animations.impl.SmoothStepAnimation;
import cn.cedo.drag.Dragging;
import cn.cedo.misc.ColorUtil;
import cn.langya.RenderUtil;
import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import cn.cedo.shader.RoundedUtil;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;

/**
 * @author LangYa466,Bzdhyp
 * @since 2024/4/25 20:32
 */

public class KeyStore  implements Access.InstanceAccess {
    private final BooleanValue blur = new BooleanValue("模糊背景",true);
    private final NumberValue offsetValue = new NumberValue("间隔", 3, 2.5, 10, .5);
    private final NumberValue sizeValue = new NumberValue("大小", 25, 15, 35, 1);
    private static final NumberValue opacity = new NumberValue("透明度", 1, 0.5, 1, .05);
    private static final NumberValue radius = new NumberValue("圆角", 3, 1, 17.5, .5);

    private final Dragging dragging = Access.getInstance().getDragManager().createDrag(this.getClass(), "Keystrokes", 10, 300);

    private Button keyBindForward;
    private Button keyBindLeft;
    private Button keyBindBack;
    private Button keyBindRight;
    private Button keyBindJump;

    @EventTarget
    public void onShader(ShaderEvent e) {
        if (!blur.getValue()) return;

        float offset = offsetValue.getValue().floatValue();
        float x = dragging.getX(), y = dragging.getY(), width = dragging.getWidth(), height = dragging.getHeight(), size = sizeValue.getValue().floatValue();

        float increment = size + offset;
        keyBindForward.renderForEffects(x + width / 2f - size / 2f, y, size, e);
        keyBindLeft.renderForEffects(x, y + increment, size, e);
        keyBindBack.renderForEffects(x + increment, y + increment, size, e);
        keyBindRight.renderForEffects(x + (increment * 2), y + increment, size, e);
        keyBindJump.renderForEffects(x, y + increment * 2, width, size, e);
    }

    @EventTarget
    public void onRender(Render2DEvent e) {
        float offset = offsetValue.getValue().floatValue();
        dragging.setHeight((float) ((sizeValue.getValue() + offset) * 3) - offset);
        dragging.setWidth((float) ((sizeValue.getValue() + offset) * 3) - offset);

        if (keyBindForward == null) {
            keyBindForward = new Button(mc.gameSettings.keyBindForward);
            keyBindLeft = new Button(mc.gameSettings.keyBindLeft);
            keyBindBack = new Button(mc.gameSettings.keyBindBack);
            keyBindRight = new Button(mc.gameSettings.keyBindRight);
            keyBindJump = new Button(mc.gameSettings.keyBindJump);
        }

        float x = dragging.getX(), y = dragging.getY(), width = dragging.getWidth(), height = dragging.getHeight(), size = sizeValue.getValue().floatValue();

        Button.font = FontManager.MB22;

        float increment = size + offset;
        keyBindForward.render(x + width / 2f - size / 2f, y, size);
        keyBindLeft.render(x, y + increment, size);
        keyBindBack.render(x + increment, y + increment, size);
        keyBindRight.render(x + (increment * 2), y + increment, size);
        keyBindJump.render(x, y + increment * 2, width, size);
    }


    public static class Button {
        private static FontDrawer font;
        private final KeyBinding binding;
        private final Animation clickAnimation = new SmoothStepAnimation(125, 1);

        public Button(KeyBinding binding) {
            this.binding = binding;
        }

        public void renderForEffects(float x, float y, float size, ShaderEvent event) {
            renderForEffects(x, y, size, size, event);
        }

        public void renderForEffects(float x, float y, float width, float height, ShaderEvent event) {
            RoundedUtil.drawRound(x, y, width, height, radius.getValue().floatValue(), Color.BLACK);
        }

        public void render(float x, float y, float size) {
            render(x, y, size, size);
        }

        public void render(float x, float y, float width, float height) {
            Color color = ColorUtil.applyOpacity(Color.BLACK, opacity.getValue().floatValue());
            clickAnimation.setDirection(binding.isKeyDown() ? Direction.FORWARDS : Direction.BACKWARDS);

            RoundedUtil.drawRound(x, y, width, height, radius.getValue().floatValue(), color);
            float offsetX = 0;
            int offsetY = 0;

            font.drawCenteredString(Keyboard.getKeyName(binding.getKeyCode()), x + width / 2 + offsetX, y + height / 2 - font.getHeight() / 2f + offsetY, Color.WHITE.getRGB());

            if (!clickAnimation.finished(Direction.BACKWARDS)) {
                float animation = clickAnimation.getOutput().floatValue();
                Color color2 = ColorUtil.applyOpacity(Color.WHITE, (0.5f * animation));
                RenderUtil.scaleStart(x + width / 2f, y + height / 2f, animation);
                float diff = (height / 2f) - radius.getValue().floatValue();
                RoundedUtil.drawRound(x, y, width, height, ((height / 2f) - (diff * animation)), color2);
                RenderUtil.scaleEnd();
            }
        }
    }
}