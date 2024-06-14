package cn.langya.modules.misc;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumParticleTypes;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.misc.AttackEvent;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

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
