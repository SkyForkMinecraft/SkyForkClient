package com.skyfork.api.cedo;


import com.skyfork.api.cedo.drag.Dragging;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.update.TickEvent;
import com.skyfork.client.value.impl.BooleanValue;

public class ScoreboardMod implements Access.InstanceAccess {

    public static final BooleanValue redNumbers = new BooleanValue("行数", false);

    public static final Dragging drag = Access.getInstance().getDragManager().createDrag(ScoreboardMod.class,"scoreboard", 0, 50);
    private boolean set = false;

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.thePlayer == null) return;
        if (!set) {
            drag.setWidth(50);
            drag.setHeight(50);
            set = true;
        }
    }

}
