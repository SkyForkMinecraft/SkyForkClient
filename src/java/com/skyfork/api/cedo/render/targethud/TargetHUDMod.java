package com.skyfork.api.cedo.render.targethud;

import com.skyfork.api.cedo.animations.Animation;
import com.skyfork.api.cedo.animations.Direction;
import com.skyfork.api.cedo.animations.impl.DecelerateAnimation;
import com.skyfork.api.cedo.drag.Dragging;
import com.skyfork.api.cedo.misc.ColorUtil;
import com.skyfork.api.cedo.misc.GradientColorWheel;
import com.skyfork.api.cedo.misc.Pair;
import com.skyfork.api.cedo.render.ESPUtil;
import com.skyfork.api.imflowow.LoadWorldEvent;
import com.skyfork.api.langya.RenderUtil;
import com.skyfork.api.langya.TargetManager;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.util.vector.Vector4f;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.annotations.module.Enable;
import com.skyfork.client.events.render.PreRenderEvent;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.events.render.Render3DEvent;
import com.skyfork.client.events.render.ShaderEvent;
import com.skyfork.client.value.impl.BooleanValue;
import com.skyfork.client.value.impl.ComboValue;

import java.awt.*;

public class TargetHUDMod implements Access.InstanceAccess {

    private final ComboValue targetHud = new ComboValue("Mode", "Old Tenacity", "Old Tenacity", "ThunderHack", "Exhibition","Akrien", "Astolfo", "Novoline", "Vape");
    private final BooleanValue trackTarget = new BooleanValue("Track Target", false);
    private final ComboValue trackingMode = new ComboValue("Tracking Mode", "Middle", "Middle", "Top", "Left", "Right");

    private final GradientColorWheel colorWheel = new GradientColorWheel();

    public TargetHUDMod() {
        TargetHUD.init();
    }

    private EntityLivingBase target;
    private final Dragging drag = Access.getInstance().getDragManager().createDrag(this.getClass(),"targetHud", 300, 300);

    private final Animation openAnimation = new DecelerateAnimation(175, .5);

    private Vector4f targetVector;

    @EventTarget
    public void onWorldLoad(LoadWorldEvent event) {
        openAnimation.setDirection(Direction.BACKWARDS);
    }

    @EventTarget
    public void onRender3DEvent(Render3DEvent event) {
        if (trackTarget.getValue() && target != null) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    if (target.equals(entityLivingBase)) {
                        targetVector = ESPUtil.getEntityPositionsOn2D(entity);
                    }
                }
            }

        }
    }

    @EventTarget
    public void onPreRenderEvent(PreRenderEvent event) {
        TargetHUD currentTargetHUD = TargetHUD.get(targetHud.getValue());
        drag.setWidth(currentTargetHUD.getWidth());
        drag.setHeight(currentTargetHUD.getHeight());

        if (!(mc.currentScreen instanceof GuiChat)) {

            if (target == null && TargetManager.target != null) {
                target = TargetManager.target;
                openAnimation.setDirection(Direction.FORWARDS);
                
            } else if (TargetManager.target == null || target != TargetManager.target) {
                openAnimation.setDirection(Direction.BACKWARDS);
            }

            if (openAnimation.finished(Direction.BACKWARDS)) {
                TargetHUD.get(RiseTargetHUD.class).particles.clear();
                target = null;
            }
        } else {
            openAnimation.setDirection(Direction.FORWARDS);
            target = mc.thePlayer;
        }


        if (target != null) {
            colorWheel.setColorsForMode("Dark", ColorUtil.brighter(new Color(30, 30, 30), .65f));
            currentTargetHUD.setColorWheel(colorWheel);
        }

    }

    @EventTarget
    public void onRender2DEvent(Render2DEvent e) {
        this.setSuffix(targetHud.getValue(),this);
        boolean tracking = trackTarget.getValue() && targetVector != null && target != mc.thePlayer;

        TargetHUD currentTargetHUD = TargetHUD.get(targetHud.getValue());

        if (target != null) {


            float trackScale = 1;
            float x = drag.getXPos(), y = drag.getYPos();
            if (tracking) {
                float newWidth = (targetVector.getZ() - targetVector.getX()) * 1.4f;
                trackScale = Math.min(1, newWidth / currentTargetHUD.getWidth());

                Pair<Float, Float> coords = getTrackedCoords();
                x = coords.getFirst();
                y = coords.getSecond();
            }


            RenderUtil.scaleStart(x + drag.getWidth() / 2f, y + drag.getHeight() / 2f,
                    (float) (.5 + openAnimation.getOutput().floatValue()) * trackScale);
            float alpha = Math.min(1, openAnimation.getOutput().floatValue() * 2);

            currentTargetHUD.render(x, y, alpha, target);


            RenderUtil.scaleEnd();
        }
    }


    @EventTarget
    public void onShaderEvent(ShaderEvent e) {
        float x = drag.getXPos(), y = drag.getYPos();
        float trackScale = 1;
        TargetHUD currentTargetHUD = TargetHUD.get(targetHud.getValue());
        if (trackTarget.getValue() && targetVector != null && target != mc.thePlayer) {
            Pair<Float, Float> coords = getTrackedCoords();
            x = coords.getFirst();
            y = coords.getSecond();

            float newWidth = (targetVector.getZ() - targetVector.getX()) * 1.4f;
            trackScale = Math.min(1, newWidth / currentTargetHUD.getWidth());
        }


        if (target != null) {

            boolean glow = false;
            RenderUtil.scaleStart(x + drag.getWidth() / 2f, y + drag.getHeight() / 2f,
                    (float) (.5 + openAnimation.getOutput().floatValue()) * trackScale);
            float alpha = Math.min(1, openAnimation.getOutput().floatValue() * 2);

            currentTargetHUD.renderEffects(x, y, alpha, glow);

            RenderUtil.scaleEnd();
        }
    }


    @Enable
    public void onEnable() {
        target = null;
    }


    private Pair<Float, Float> getTrackedCoords() {
        float width = drag.getWidth(), height = drag.getHeight();
        float x = targetVector.getX(), y = targetVector.getY();
        float entityWidth = (targetVector.getZ() - targetVector.getX());
        float entityHeight = (targetVector.getW() - targetVector.getY());
        float middleX = x + entityWidth / 2f - width / 2f;
        float middleY = y + entityHeight / 2f - height / 2f;
        switch (trackingMode.getValue()) {
            case "Middle":
                return Pair.of(middleX, middleY);
            case "Top":
                return Pair.of(middleX, y - (height / 2f + height / 4f));
            case "Left":
                return Pair.of(x - (width / 2f + width / 4f), middleY);
            default:
                return Pair.of(x + entityWidth - (width / 4f), middleY);
        }
    }


}
