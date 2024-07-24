package com.skyfork.client.module.movement;

import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.annotations.module.Startup;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.events.update.TickEvent;

@Startup
public class Sprint implements Access.InstanceAccess {

    @EventTarget
    public void onUpdate(TickEvent event) {
        mc.gameSettings.keyBindSprint.pressed = true;
    }

}
