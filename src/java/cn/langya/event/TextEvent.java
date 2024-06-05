package cn.langya.event;

import lombok.Getter;
import lombok.Setter;
import org.union4dev.base.events.base.Cancellable;
import org.union4dev.base.events.base.Event;

/**
 * @author LangYa
 * @since 2024/6/5 下午9:02
 */

public class TextEvent implements Cancellable, Event {
    private boolean cancel;
    @Getter
    @Setter
    private String text;

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
