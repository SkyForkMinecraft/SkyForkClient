package cn.cedo.render.targethud;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.DecelerateAnimation;
import cn.cedo.misc.ColorUtil;
import cn.cedo.misc.GradientColorWheel;
import cn.cedo.misc.Pair;
import cn.cedo.render.ESPUtil;
import cn.langya.RenderUtil;
import cn.langya.TargetManager;
import cn.langya.elements.Element;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.util.vector.Vector4f;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.render.Render3DEvent;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;

import java.awt.*;

public class TargetHUDMod extends Element {

    private final ComboValue targetHud = new ComboValue("Mode", "Tenacity", "Tenacity", "Old Tenacity", "Rise", "Exhibition", "Auto-Dox", "Akrien", "Astolfo", "Novoline", "Vape");
    private final BooleanValue trackTarget = new BooleanValue("Track Target", false);
    private final ComboValue trackingMode = new ComboValue("Tracking Mode", "Middle", "Middle", "Top", "Left", "Right");
    private final BooleanValue glow = new BooleanValue("Glow", false);

    private final GradientColorWheel colorWheel = new GradientColorWheel();

    public TargetHUDMod() {
        super(250,250);
        setWH(300,300);
        TargetHUD.init();
    }

    private EntityLivingBase target;

    private final Animation openAnimation = new DecelerateAnimation(175, .5);
    
    private Vector4f targetVector;


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
    public void onPreRenderEvent(Render2DEvent event) {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        TargetHUD currentTargetHUD = TargetHUD.get(targetHud.getValue());
        setWidth(currentTargetHUD.getWidth());
        setHeight(currentTargetHUD.getHeight());


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
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        this.setSuffix(targetHud.getValue(),TargetHUDMod.class);
        boolean tracking = trackTarget.getValue() && targetVector != null && target != mc.thePlayer;

        TargetHUD currentTargetHUD = TargetHUD.get(targetHud.getValue());

        if (target != null) {


            float trackScale = 1;
            
            if (tracking) {
                float newWidth = (targetVector.getZ() - targetVector.getX()) * 1.4f;
                trackScale = Math.min(1, newWidth / currentTargetHUD.getWidth());

                Pair<Float, Float> coords = getTrackedCoords();
                x = coords.getFirst();
                y = coords.getSecond();
            }


            RenderUtil.scaleStart(x + width / 2f, y + height / 2f,
                    (float) (.5 + openAnimation.getOutput().floatValue()) * trackScale);
            float alpha = Math.min(1, openAnimation.getOutput().floatValue() * 2);

            currentTargetHUD.render(x, y, alpha, target);


            RenderUtil.scaleEnd();
        }
    }


    @EventTarget
    public void onShaderEvent(ShaderEvent e) {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
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

            RenderUtil.scaleStart(x + width / 2f, y + height / 2f,
                    (float) (.5 + openAnimation.getOutput().floatValue()) * trackScale);
            float alpha = Math.min(1, openAnimation.getOutput().floatValue() * 2);

            currentTargetHUD.renderEffects(x, y, alpha, glow.getValue());

            RenderUtil.scaleEnd();
        }
    }


    @Enable
    public void onEnable() {
        target = null;
    }


    private Pair<Float, Float> getTrackedCoords() {
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