package com.skyfork.api.langya.modules.misc;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumParticleTypes;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.misc.AttackEvent;
import com.skyfork.client.value.impl.ComboValue;
import com.skyfork.client.value.impl.NumberValue;

public class MoreParticles implements Access.InstanceAccess {
    private final NumberValue crackSize = new NumberValue("粒子数量", 2, 0, 10, 1);
    private final ComboValue particleMode = new ComboValue("粒子模式","暴击粒子","暴击粒子","普通攻击粒子");

    @EventTarget
    void onA(AttackEvent e) {
        if (e.getTarget().isDead || !(e.getTarget() instanceof EntityLiving) || ((EntityLiving) e.getTarget()).getHealth() == 0) return;
        for (int index = 0; index < this.crackSize.getValue().intValue(); ++index) {
            switch (particleMode.getValue()) {
                case "暴击粒子": mc.effectRenderer.emitParticleAtEntity(e.getTarget(), EnumParticleTypes.CRIT);
                case "普通攻击粒子": mc.effectRenderer.emitParticleAtEntity(e.getTarget(), EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }
}
