package cn.langya.modules.client;

import cn.cedo.misc.TimerUtil;
import com.google.common.base.Predicates;
import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.entity.Entity;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.movement.MotionUpdateEvent;
import org.union4dev.base.events.movement.MoveEvent;
import org.union4dev.base.value.impl.NumberValue;

import java.util.List;

/**
 * @author cubk, langya
 */
public class KillAura implements Access.InstanceAccess {

    public NumberValue maxCPSValue = new NumberValue("MaxCPS",13,1,20,1);
    public NumberValue minCPSValue = new NumberValue("MinCPS",8,1,20,1);
    public NumberValue rangeValue = new NumberValue("Range",3,3,5,0.1);

    public EntityPlayer target;
    private float[] angle;
    private TimerUtil attackTimer = new TimerUtil();

    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (angle != null) {
            if (event.getYaw() != angle[0]) {
                event.setYaw(angle[0]);
            }
            if (event.getPitch() != angle[1]) {
                event.setPitch(angle[1]);
            }
        }
    }


    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        setSuffix("Single",this);

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer && mc.thePlayer.getDistanceToEntity(entity) < rangeValue.getValue() && entity != mc.thePlayer && !entity.isDead && ((EntityPlayer) entity).getHealth() > 0){
                target = (EntityPlayer) entity;
                break;
            }
        }

        if (target == null) {
            angle = null;
            return;
        }
        if (target.getDistanceToEntity(mc.thePlayer) > rangeValue.getValue() || target.getHealth() <= 0 || target.isDead) {
            target = null;
            angle = null;
            return;
        }

        angle = getRotationsNeeded(target);

        if (event.getYaw() != angle[0]) {
            event.setYaw(angle[0]);
        }
        if (event.getPitch() != angle[1]) {
            event.setPitch(angle[1]);
        }

        if(attackTimer.hasTimeElapsed(1000 / getCps())) {
            if (isMouseOver(angle[0],angle[1],target,rangeValue.getValue().floatValue())) {
                AttackOrder.sendFixedAttack(mc.thePlayer, target);
                attackTimer.reset();
            }
        }

    }

    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = mc.timer.renderPartialTicks;
        final Entity entity = mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;
        Entity mcPointedEntity = null;

        if (entity != null && mc.theWorld != null) {

            mc.mcProfiler.startSection("pick");
            final double d0 = mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = mc.thePlayer.getVectorForRotation(pitch, yaw);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mcPointedEntity = pointedEntity;
                }
            }

            mc.mcProfiler.endSection();

            return mcPointedEntity == target;
        }

        return false;
    }

    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }

    private int getCps() {
        final double maxValue = (maxCPSValue.getMaximum() - maxCPSValue.getValue()) * 20;
        final double minValue = (minCPSValue.getMaximum() - minCPSValue.getValue()) * 20;
        return (int) (Math.random() * (maxValue - minValue) + minValue);
    }
}
