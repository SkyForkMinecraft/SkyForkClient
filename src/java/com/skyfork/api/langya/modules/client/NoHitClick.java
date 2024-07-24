package com.skyfork.api.langya.modules.client;

import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.update.TickEvent;

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
