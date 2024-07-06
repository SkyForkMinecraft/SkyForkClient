package cn.cedo;

import org.union4dev.base.events.base.Event;

public class KeepSprintEvent implements Event {

    private boolean cancel;

    public KeepSprintEvent() {
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}
