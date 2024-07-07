package cn.langya.modules.client;

import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.update.TickEvent;

/**
 * @author LangYa
 * @since 2024/07/07/下午4:43
 */
public class NoHitClick implements Access.InstanceAccess {

    @EventTarget
    public void onTick(TickEvent event) {
        mc.leftClickCounter = 0;
    }
}
