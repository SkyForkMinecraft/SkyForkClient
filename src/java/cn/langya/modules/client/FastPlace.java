package cn.langya.modules.client;


import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Disable;
import org.union4dev.base.events.update.UpdateEvent;
import org.union4dev.base.value.impl.NumberValue;

/**
 * @author LangYa
 * @since 2024/06/19/下午9:21
 */
public class FastPlace implements Access.InstanceAccess {
    public NumberValue speedValue = new NumberValue("Speed",0,3,3,1);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.rightClickDelayTimer = speedValue.getValue().intValue();
    }

    @Disable
    public void onDisable() {
        mc.rightClickDelayTimer = 3;
    }
}
