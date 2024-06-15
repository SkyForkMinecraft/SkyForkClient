package cn.langya.modules.render;

import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Disable;
import org.union4dev.base.events.update.TickEvent;
import org.union4dev.base.value.impl.NumberValue;

public class MotionBlur implements Access.InstanceAccess {
    public static final NumberValue amount = new NumberValue("模糊度", 6, 1, 0,1);

    @Disable
    public void onDisable() {
        if (mc.entityRenderer.isShaderActive()) {
            mc.entityRenderer.stopUseShader();
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.thePlayer != null) {
            if (mc.entityRenderer.getShaderGroup() == null) {
                mc.entityRenderer.loadShader(new ResourceLocation("minecraft", "shaders/post/motion_blur.json"));
            }
            float uniform = 1f - Math.min(amount.getValue().intValue() / 10f, 0.9f);
            if (mc.entityRenderer.getShaderGroup() != null) {
                mc.entityRenderer.getShaderGroup().listShaders.get(0).getShaderManager().getShaderUniform("Phosphor")
                        .set(uniform, 0f, 0f);
            }
        }
    }
}
