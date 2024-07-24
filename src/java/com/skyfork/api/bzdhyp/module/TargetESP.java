package com.skyfork.api.bzdhyp.module;

import com.skyfork.api.cedo.animations.Animation;
import com.skyfork.api.cedo.animations.impl.DecelerateAnimation;
import com.skyfork.api.cedo.render.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.misc.AttackEvent;
import com.skyfork.client.events.render.Render3DEvent;
import com.skyfork.client.value.impl.ComboValue;

import java.awt.*;
import java.util.Objects;

import static com.skyfork.client.Access.InstanceAccess.mc;

public class TargetESP {
    public static final ComboValue mode = new ComboValue("Mode", "Nurikzapen", "Nurikzapen", "Round");
    EntityLivingBase target;

    @EventTarget
    public void onAttack(AttackEvent event) {
        if (event.getTarget() != null) {
            try {
                target = (EntityLivingBase) event.getTarget();
            } catch (ClassCastException e) {
                target = null;
            }
        }
    }

    private final Animation auraESPAnim = new DecelerateAnimation(300, 1);

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (target != null) {
            if (target.isDead || mc.thePlayer.getSmoothDistanceToEntity(target) > 10) {
                target = null;
                return;
            }
            float dst = mc.thePlayer.getSmoothDistanceToEntity(target);
            try {
                RenderUtil.drawTargetESP2D(Objects.requireNonNull(RenderUtil.targetESPSPos(target)).x, Objects.requireNonNull(RenderUtil.targetESPSPos(target)).y, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
                        (1.0f - MathHelper.clamp_float(Math.abs(dst - 6.0f) / 60.0f, 0f, 0.75f)) * 1, 1, auraESPAnim.getOutput().floatValue());
            } catch (Exception e) {
                target = null;
            }
        }
    }
}
