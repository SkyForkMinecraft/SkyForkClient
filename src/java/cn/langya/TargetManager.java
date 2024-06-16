package cn.langya;

import com.yumegod.obfuscation.Native;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.update.UpdateEvent;

@Native
public class TargetManager implements Access.InstanceAccess {
    public static EntityLivingBase target;
    public static boolean canAdd;
    public static int add;

    @EventTarget
    void onUP(UpdateEvent e) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0 && !entity.isDead && mc.thePlayer.getDistanceToEntity(entity) < 3) {
                target = (EntityLivingBase) entity;
            }
        }

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity != mc.thePlayer && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0 && !entity.isDead && mc.thePlayer.getDistanceToEntity(entity) < 20)) {
                add++;
                canAdd = true;
            } else {
                canAdd = false;
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
