package com.skyfork.api.superskidder;

import net.minecraft.entity.Entity;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.misc.AttackEvent;
import com.skyfork.client.events.update.TickEvent;

/**
 * @author LangYa
 * @since 2024/07/07/下午1:39
 */
public class ComboHandler implements Access.InstanceAccess {
    public static Entity target;
    public static int combo = 0;

    @EventTarget
    public void onTick(TickEvent e) {
        if (mc.thePlayer == null) return;

        if (mc.thePlayer.hurtTime == 1 || (target != null && mc.thePlayer.getDistanceToEntity(target) > 5)) {
            combo = 0;
        }

        if (target != null && target.isEntityAlive() && target.hurtResistantTime == 19) {
            combo++;
        }
    }

    @EventTarget
    public void onAttack(AttackEvent e) {
        target = e.getTarget();
    }

}
