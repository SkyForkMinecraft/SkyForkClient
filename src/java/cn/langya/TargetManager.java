package cn.langya;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.update.UpdateEvent;

public class TargetManager implements Access.InstanceAccess {
    public static EntityLivingBase target;

    @EventTarget
    void onUP(UpdateEvent e) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0 && !entity.isDead && mc.thePlayer.getDistanceToEntity(entity) < 3) {
                target = (EntityLivingBase) entity;
            }
        }
    }
}
