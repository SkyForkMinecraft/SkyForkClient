package cn.langya.hack.modules;

import cn.langya.modules.misc.particles.TimerUtils;
import cn.tenacity.PlayerMoveUpdateEvent;
import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
    private final BooleanValue legit = new BooleanValue("Legit", true);
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

    @EventTarget
    private void onMu(MotionUpdateEvent e) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            // 判断类型，等等打弓箭，物品就尴尬了
            if (!(entity instanceof EntityLivingBase)) continue;
            // 实体是当前玩家 或 实体死亡 或 实体和当前玩家的距离大于 range 就不进行下面的操作
            if (entity == mc.thePlayer || entity.isDead || entity.getDistanceToEntity(mc.thePlayer) > range.getValue())
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

        // 如果targets列表是空的且target不为空
        if (target != null) {
            float[] rotations = getRotations(target);
            float yaw = rotations[0];
            e.setYaw(yaw);
            e.setPitch(rotations[1]);
            if (legit.getValue()) {
                mc.thePlayer.rotationYaw = yaw;
                mc.thePlayer.rotationPitch = rotations[1];
            }
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
        if (moveFix.getValue() && target != null) {
            mc.thePlayer.rotationYaw = getRotations(target)[0];
        }
    }

    @EventTarget
    private void onJumpFixEvent(JumpEvent event) {
        if (moveFix.getValue() && target != null) mc.thePlayer.rotationYaw = getRotations(target)[0];
    }

}
