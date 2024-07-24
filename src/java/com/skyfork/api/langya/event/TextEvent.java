package com.skyfork.api.langya.event;

import com.skyfork.client.events.base.Cancellable;
import com.skyfork.client.events.base.Event;

/**
 * @author LangYa
 * @since 2024/6/5 下午9:02
 */

public class TextEvent implements Cancellable, Event {
    private boolean cancel;
    public String text;

    public TextEvent(String text) {
        this.text = text;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}
