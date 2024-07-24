package com.skyfork.client.events.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.skyfork.client.events.base.Event;

/**
 * @author TG_format
 * @since 2024/5/26 13:25
 */
@Getter@Setter@AllArgsConstructor
public class ChatEvent extends Event.EventCancellable {
    private String message;
}
