package org.union4dev.base.events.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.union4dev.base.events.base.Event;

/**
 * @author TG_format
 * @since 2024/5/26 13:25
 */
@Getter@Setter@AllArgsConstructor
public class ChatEvent extends Event.EventCancellable {
    private String message;
}
