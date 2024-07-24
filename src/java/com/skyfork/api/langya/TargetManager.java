package com.skyfork.api.langya;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.movement.MotionUpdateEvent;

import java.util.ArrayList;
import java.util.List;

public class TargetManager implements Access.InstanceAccess {
    public static EntityLivingBase target;
    public static final List<EntityLivingBase> targets = new ArrayList<>();

    @EventTarget
    public void onMotionEvent(MotionUpdateEvent event) {
        sortTargets();

        if (!targets.isEmpty()) target = targets.get(0);

        if (mc.thePlayer.getDistanceToEntity(target) > 6 || target.getHealth() <= 0) {
            target = null;
        }

    }

    private void sortTargets() {
        targets.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(entity) <= 3.0 && mc.thePlayer != entityLivingBase) {
                    targets.add(entityLivingBase);
                }
            }
        }
    }

    public static EntityLivingBase getTarget(float range) {
        EntityLivingBase t = null;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0 && !entity.isDead && mc.thePlayer.getDistanceToEntity(entity) < range) {
                t = (EntityLivingBase) entity;
            }
        }
        return t;
    }
}
