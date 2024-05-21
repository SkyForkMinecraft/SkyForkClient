package org.union4dev.base.module.movement;

import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.update.TickEvent;

@Startup
public class Sprint implements Access.InstanceAccess {

    @EventTarget
    public void onUpdate(TickEvent event) {
        mc.gameSettings.keyBindSprint.pressed = true;
    }

}
