package cn.cedo;


import cn.cedo.drag.Dragging;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.update.TickEvent;
import org.union4dev.base.value.impl.BooleanValue;

public class ScoreboardMod implements Access.InstanceAccess {

    public static final BooleanValue redNumbers = new BooleanValue("行数", false);

    public static final Dragging drag = Access.getInstance().getDragManager().createDrag("scoreboard", 0, 50);
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
