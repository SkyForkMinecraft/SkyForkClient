package cn.langya.hack.modules;

import cn.langya.modules.misc.particles.TimerUtils;
import cn.cedo.PlayerMoveUpdateEvent;
import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.movement.JumpEvent;
import org.union4dev.base.events.movement.MotionUpdateEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class KillAura implements Access.InstanceAccess {
    private final NumberValue cps = new NumberValue("CPS", 12, 1, 20, 1);
    private final NumberValue range = new NumberValue("Range", 3.2, 3.0, 6, 0.1);
    private final BooleanValue moveFix = new BooleanValue("MoveFIx", true);
    private final List<Entity> targets = new CopyOnWriteArrayList<>();
    private Entity target;
    private final TimerUtils timer = new TimerUtils();

    // 我不到啊，我也抄袭的。
    private float[] getRotations(Entity ent) {
        final double x = ent.posX - mc.thePlayer.posX;
        double y = ent.posY - mc.thePlayer.posY;
        final double z = ent.posZ - mc.thePlayer.posZ;
        y /= mc.thePlayer.getDistanceToEntity(ent);
        final float yaw = (float) (-(Math.atan2(x, z) * 57.29577951308232));
        final float pitch = (float) (-(Math.asin(y) * 57.29577951308232));
        return new float[]{yaw, pitch};
    }

    // LiquidBounce
    public void applyStrafeToPlayer(PlayerMoveUpdateEvent event) {
        EntityPlayerSP player = mc.thePlayer;
        float yaw = getRotations(target)[0];
        float dif = ((MathHelper.wrapAngleTo180_float(player.rotationYaw - yaw - 23.5f - 135) + 180) / 45);
        int difInt = (int) dif;

        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();

        float calcForward = 0f;
        float calcStrafe = 0f;

        switch (difInt) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }

        if (calcForward > 1f || (calcForward < 0.9f && calcForward > 0.3f) || calcForward < -1f ||
                (calcForward > -0.9f && calcForward < -0.3f)) {
            calcForward *= 0.5f;
        }

        if (calcStrafe > 1f || (calcStrafe < 0.9f && calcStrafe > 0.3f) || calcStrafe < -1f ||
                (calcStrafe > -0.9f && calcStrafe < -0.3f)) {
            calcStrafe *= 0.5f;
        }

        float d = calcStrafe * calcStrafe + calcForward * calcForward;

        if (d >= 1.0E-4f) {
            d = MathHelper.sqrt_float(d);

            if (d < 1.0f) {
                d = 1.0f;
            }

            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;

            float yawSin = MathHelper.sin((float) (yaw * Math.PI / 180f));
            float yawCos = MathHelper.cos((float) (yaw * Math.PI / 180f));

            player.motionX += calcStrafe * yawCos - calcForward * yawSin;
            player.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }


    @EventTarget
    private void onMu(MotionUpdateEvent e) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            // 判断类型，等等打弓箭，物品就尴尬了
            if (!(entity instanceof EntityLivingBase)) continue;
            // 实体是当前玩家 或 实体死亡 或 实体和当前玩家的距离大于 range 就不进行下面的操作
            if (entity == mc.thePlayer || entity.isDead || ((EntityLivingBase) entity).getHealth() == 0 || entity.getDistanceToEntity(mc.thePlayer) > range.getValue())
                continue;
            // 给targets列表加进去
            //    targets.add(entity);
            target = entity;
        }
        // 然后再判断一次 防止添加完的目标死了还打
        // targets.removeIf(target -> target.isDead || target.getDistanceToEntity(mc.thePlayer) > range.getValue());
        /*
        for (Entity entity : targets) {
            // 筛选出最好的目标
            targets.sort(Comparator.comparingDouble(player -> mc.thePlayer.getDistanceToEntity(player)));
            target = entity;
            // 省略代码
        }

         */
        if (target.isDead || target.getDistanceToEntity(mc.thePlayer) > range.getValue()) target = null;


            // 如果targets列表是空的且target不为空
        if (target != null) {
            float[] rotations = getRotations(target);
            float yaw = rotations[0];
            e.setYaw(yaw);
            e.setPitch(rotations[1]);
            // 视觉
            mc.thePlayer.prevRenderYawOffset = yaw;
            mc.thePlayer.renderYawOffset = yaw;
            mc.thePlayer.prevRotationYawHead = yaw;
            mc.thePlayer.rotationYawHead = yaw;
            // 如果计时器的时间达到了 1000ms(1s) 除以 cps
            if (timer.hasReached(1000 / cps.getValue())) {
                // 攻击
                AttackOrder.sendFixedAttack(mc.thePlayer, target);
                // 重制计时器
                timer.reset();
            }
        }
    }

    @EventTarget
    private void onPlayerMoveUpdateEvent(PlayerMoveUpdateEvent event) {
        if (target == null) return;

        if (moveFix.getValue()) {
            event.setYaw(getRotations(target)[0]);
        }

        if (event.getForward() != 0f) {
            float yaw = getRotations(target)[0];
            float strafe = event.getStrafe();
            float forward = event.getForward();
            float friction = event.getFriction();

            float f = strafe * strafe + forward * forward;

            if (f >= 1.0E-4F) {
                f = MathHelper.sqrt_float(f);

                if (f < 1.0F) {
                    f = 1.0F;
                }

                f = friction / f;
                strafe *= f;
                forward *= f;

                float yawSin = MathHelper.sin((float) (yaw * Math.PI / 180F));
                float yawCos = MathHelper.cos((float) (yaw * Math.PI / 180F));

                mc.thePlayer.motionX += strafe * yawCos - forward * yawSin;
                mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin;
            }
            event.setCancel(true);
        } else if (event.getStrafe() != 0f) {
            applyStrafeToPlayer(event);
            event.setCancel(true);
        }
    }

    @EventTarget
    private void onJumpFixEvent(JumpEvent event) {
        event.setYaw(getRotations(target)[0]);
    }

}
